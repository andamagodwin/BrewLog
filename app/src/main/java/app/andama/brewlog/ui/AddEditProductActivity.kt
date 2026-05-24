package app.andama.brewlog.ui

import android.os.Bundle
import android.text.InputFilter
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.andama.brewlog.R
import app.andama.brewlog.data.local.BrewLogContract
import app.andama.brewlog.data.local.BrewLogDao
import app.andama.brewlog.data.local.Product
import app.andama.brewlog.databinding.ActivityAddEditProductBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEditProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditProductBinding
    private lateinit var dao: BrewLogDao
    private var editingProductId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = BrewLogDao(this)
        setupRoastLevelDropdown()
        setupInputFilters()

        editingProductId = intent.getLongExtra(BrewLogIntents.EXTRA_PRODUCT_ID, -1L)
        if (editingProductId != -1L) {
            loadExistingProduct(editingProductId)
        } else {
            binding.buttonDeleteProduct.visibility = android.view.View.GONE
        }

        binding.buttonSaveProduct.setOnClickListener { saveProduct() }
        binding.buttonDeleteProduct.setOnClickListener { confirmDelete() }
    }

    private fun setupRoastLevelDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, BrewLogContract.RoastLevel.ALL)
        binding.autoCompleteTextViewRoastLevel.setAdapter(adapter)
        if (binding.autoCompleteTextViewRoastLevel.text.isNullOrBlank()) {
            binding.autoCompleteTextViewRoastLevel.setText(BrewLogContract.RoastLevel.LIGHT, false)
        }
    }

    private fun setupInputFilters() {
        binding.editTextProductName.filters = arrayOf(InputFilter.LengthFilter(80))
        binding.editTextProductOrigin.filters = arrayOf(InputFilter.LengthFilter(80))
        binding.editTextPricePerKg.filters = arrayOf(DecimalDigitsInputFilter(5, 2))
        binding.editTextStockKg.filters = arrayOf(DecimalDigitsInputFilter(6, 2))
    }

    private fun loadExistingProduct(productId: Long) {
        val product = dao.getProductById(productId) ?: return
        binding.textViewProductFormTitle.text = "Edit Coffee Batch"
        binding.buttonSaveProduct.text = "Update"
        binding.buttonDeleteProduct.visibility = android.view.View.VISIBLE

        binding.editTextProductName.setText(product.name)
        binding.editTextProductOrigin.setText(product.origin)
        binding.autoCompleteTextViewRoastLevel.setText(product.roastLevel, false)
        binding.editTextPricePerKg.setText(product.pricePerKg.toString())
        binding.editTextStockKg.setText(product.stockKg.toString())

        val calendar = Calendar.getInstance().apply {
            time = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(product.roastDate) ?: time
        }
        binding.datePickerRoastDate.updateDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
    }

    private fun saveProduct() {
        val name = binding.editTextProductName.text?.toString().orEmpty().trim()
        val origin = binding.editTextProductOrigin.text?.toString().orEmpty().trim()
        val roastLevel = binding.autoCompleteTextViewRoastLevel.text?.toString().orEmpty().trim()
        val pricePerKg = parsePositiveDoubleOrNull(binding.editTextPricePerKg.text)
        val stockKg = parsePositiveDoubleOrNull(binding.editTextStockKg.text)

        binding.textInputLayoutProductName.setErrorMessage(null)
        binding.textInputLayoutProductOrigin.setErrorMessage(null)
        binding.textInputLayoutRoastLevel.setErrorMessage(null)
        binding.textInputLayoutPricePerKg.setErrorMessage(null)
        binding.textInputLayoutStockKg.setErrorMessage(null)

        if (name.isBlank()) {
            binding.textInputLayoutProductName.setErrorMessage("Batch name is required")
            return
        }

        if (origin.isBlank()) {
            binding.textInputLayoutProductOrigin.setErrorMessage("Origin is required")
            return
        }

        if (roastLevel.isBlank()) {
            binding.textInputLayoutRoastLevel.setErrorMessage("Choose a roast level")
            return
        }

        if (pricePerKg == null) {
            binding.textInputLayoutPricePerKg.setErrorMessage("Enter a valid price")
            return
        }

        if (stockKg == null) {
            binding.textInputLayoutStockKg.setErrorMessage("Enter a valid stock quantity")
            return
        }

        val roastDate = formatProductDate(
            binding.datePickerRoastDate.year,
            binding.datePickerRoastDate.month,
            binding.datePickerRoastDate.dayOfMonth,
        )

        val product = Product(
            id = if (editingProductId == -1L) 0L else editingProductId,
            name = name,
            origin = origin,
            roastLevel = roastLevel,
            roastDate = roastDate,
            pricePerKg = pricePerKg,
            stockKg = stockKg,
        )

        val success = if (editingProductId == -1L) {
            dao.insertProduct(product) != -1L
        } else {
            dao.updateProduct(product) > 0
        }

        if (success) {
            Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Unable to save product", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDelete() {
        if (editingProductId == -1L) return

        AlertDialog.Builder(this)
            .setTitle("Delete Coffee Batch")
            .setMessage("Remove this product from inventory?")
            .setPositiveButton("Delete") { _, _ ->
                val deleted = dao.deleteProduct(editingProductId) > 0
                if (deleted) {
                    Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Unable to delete product", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}