package app.andama.brewlog.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.andama.brewlog.data.local.BrewLogContract
import app.andama.brewlog.data.local.BrewLogDao
import app.andama.brewlog.data.local.Customer
import app.andama.brewlog.data.local.Product
import app.andama.brewlog.databinding.ActivityCreateCustomerOrderBinding

class CreateCustomerOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCustomerOrderBinding
    private lateinit var dao: BrewLogDao
    private var customers: List<Customer> = emptyList()
    private var products: List<Product> = emptyList()
    private var selectedCustomer: Customer? = null
    private var selectedProduct: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCustomerOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = BrewLogDao(this)
        BrewLogSeedData.seedIfNeeded(dao)
        setupInputFilters()
        loadDropdownData()

        binding.editTextQuantityKg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                updateLiveTotal()
            }
        })

        binding.buttonSubmitOrder.setOnClickListener { submitOrder() }
    }

    private fun loadDropdownData() {
        customers = dao.getAllCustomers()
        products = dao.getAllProducts()

        if (customers.isEmpty() || products.isEmpty()) {
            Toast.makeText(this, "Seed data is missing", Toast.LENGTH_SHORT).show()
            return
        }

        val customerNames = customers.map { it.cafeName }
        binding.autoCompleteTextViewCustomer.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1, customerNames),
        )
        binding.autoCompleteTextViewCustomer.setOnItemClickListener { _, _, position, _ ->
            selectedCustomer = customers[position]
        }

        val productNames = products.map { it.name }
        binding.autoCompleteTextViewProduct.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1, productNames),
        )
        binding.autoCompleteTextViewProduct.setOnItemClickListener { _, _, position, _ ->
            selectedProduct = products[position]
            binding.textViewSelectedProductStock.text = "Available stock: ${formatQuantity(selectedProduct?.stockKg ?: 0.0)}"
            updateLiveTotal()
        }

        selectedCustomer = customers.first()
        selectedProduct = products.first()
        binding.autoCompleteTextViewCustomer.setText(selectedCustomer?.cafeName, false)
        binding.autoCompleteTextViewProduct.setText(selectedProduct?.name, false)
        binding.textViewSelectedProductStock.text = "Available stock: ${formatQuantity(selectedProduct?.stockKg ?: 0.0)}"
        updateLiveTotal()
    }

    private fun setupInputFilters() {
        binding.editTextQuantityKg.filters = arrayOf(DecimalDigitsInputFilter(5, 2))
    }

    private fun updateLiveTotal() {
        val quantity = binding.editTextQuantityKg.text?.toString()?.toDoubleOrNull() ?: 0.0
        val product = selectedProduct
        if (product == null || quantity <= 0.0) {
            binding.textViewOrderTotalValue.text = formatCurrency(0.0)
            return
        }

        val total = quantity * product.pricePerKg
        binding.textViewOrderTotalValue.text = formatCurrency(total)
    }

    private fun submitOrder() {
        val customer = selectedCustomer
        val product = selectedProduct
        val quantity = parsePositiveDoubleOrNull(binding.editTextQuantityKg.text)

        binding.textInputLayoutCustomerOrder.setErrorMessage(null)
        binding.textInputLayoutProductOrder.setErrorMessage(null)
        binding.textInputLayoutQuantityKg.setErrorMessage(null)

        if (customer == null) {
            binding.textInputLayoutCustomerOrder.setErrorMessage("Select a cafe")
            return
        }

        if (product == null) {
            binding.textInputLayoutProductOrder.setErrorMessage("Select a batch")
            return
        }

        if (quantity == null) {
            binding.textInputLayoutQuantityKg.setErrorMessage("Enter a valid quantity")
            return
        }

        if (quantity > product.stockKg) {
            binding.textInputLayoutQuantityKg.setErrorMessage("Quantity exceeds available stock")
            return
        }

        val totalPrice = quantity * product.pricePerKg
        val orderId = dao.placeOrder(customer.id, product.id, quantity, totalPrice)
        if (orderId == -1L) {
            Toast.makeText(this, "Order could not be placed", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Order submitted", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, OrderHistoryActivity::class.java))
        finish()
    }
}