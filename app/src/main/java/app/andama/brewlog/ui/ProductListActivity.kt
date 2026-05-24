package app.andama.brewlog.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.andama.brewlog.data.local.BrewLogDao
import app.andama.brewlog.databinding.ActivityProductListBinding

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var dao: BrewLogDao
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = BrewLogDao(this)
        BrewLogSeedData.seedIfNeeded(dao)

        setSupportActionBar(binding.toolbarProductList)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        adapter = ProductAdapter(
            context = this,
            onEditClicked = { product -> openEditProduct(product.id) },
            onDeleteClicked = { product -> confirmDeleteProduct(product.id, product.name) },
        )

        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter

        binding.fabAddProduct.setOnClickListener {
            startActivity(Intent(this, AddEditProductActivity::class.java))
        }

        binding.buttonCreateOrder.setOnClickListener {
            startActivity(Intent(this, CreateCustomerOrderActivity::class.java))
        }

        binding.buttonViewOrders.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val products = dao.getAllProducts()
        adapter.submitList(products)
        binding.textViewProductListCount.text = "${products.size} batch${if (products.size == 1) "" else "es"}"
        binding.cardViewProductEmptyState.visibility = if (products.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        binding.recyclerViewProducts.visibility = if (products.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
    }

    private fun openEditProduct(productId: Long) {
        val intent = Intent(this, AddEditProductActivity::class.java).apply {
            putExtra(BrewLogIntents.EXTRA_PRODUCT_ID, productId)
        }
        startActivity(intent)
    }

    private fun confirmDeleteProduct(productId: Long, productName: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Batch")
            .setMessage("Delete $productName from inventory?")
            .setPositiveButton("Delete") { _, _ ->
                val deleted = dao.deleteProduct(productId) > 0
                Toast.makeText(this, if (deleted) "Product deleted" else "Unable to delete product", Toast.LENGTH_SHORT).show()
                adapter.submitList(dao.getAllProducts())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}