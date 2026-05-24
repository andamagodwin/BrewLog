package app.andama.brewlog.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.andama.brewlog.data.local.BrewLogDao
import app.andama.brewlog.data.local.BrewLogContract
import app.andama.brewlog.databinding.ActivityOrderHistoryBinding

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding
    private lateinit var dao: BrewLogDao
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = BrewLogDao(this)
        adapter = OrderHistoryAdapter(
            context = this,
            onEditStatusClicked = { orderItem -> openEditStatusDialog(orderItem.id) },
            onDeleteClicked = { orderItem -> confirmDeleteOrder(orderItem.id, orderItem.cafeName) },
        )

        setSupportActionBar(binding.toolbarOrderHistory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarOrderHistory.setNavigationOnClickListener { finish() }

        binding.recyclerViewOrderHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOrderHistory.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val history = dao.getOrderHistory()
        binding.textViewOrderHistoryCount.text = "${history.size} order${if (history.size == 1) "" else "s"}"
        binding.cardViewOrderHistoryEmptyState.visibility = if (history.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        binding.recyclerViewOrderHistory.visibility = if (history.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
        adapter.submitList(history)
    }

    private fun openEditStatusDialog(orderId: Long) {
        val order = dao.getOrderById(orderId) ?: return
        val labels = arrayOf("Pending", "Shipped", "Cancelled")
        val statuses = intArrayOf(
            BrewLogContract.OrderStatus.PENDING,
            BrewLogContract.OrderStatus.SHIPPED,
            BrewLogContract.OrderStatus.CANCELLED,
        )
        var selectedIndex = statuses.indexOf(order.orderStatus).coerceAtLeast(0)

        AlertDialog.Builder(this)
            .setTitle("Update Order Status")
            .setSingleChoiceItems(labels, selectedIndex) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton("Save") { dialog, _ ->
                dialog.dismiss()
                val updated = dao.updateOrderStatus(orderId, statuses[selectedIndex.coerceIn(statuses.indices)]) > 0
                Toast.makeText(this, if (updated) "Order updated" else "Unable to update order", Toast.LENGTH_SHORT).show()
                adapter.submitList(dao.getOrderHistory())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmDeleteOrder(orderId: Long, cafeName: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Order")
            .setMessage("Delete the order for $cafeName?")
            .setPositiveButton("Delete") { _, _ ->
                val deleted = dao.deleteOrder(orderId) > 0
                Toast.makeText(this, if (deleted) "Order deleted" else "Unable to delete order", Toast.LENGTH_SHORT).show()
                adapter.submitList(dao.getOrderHistory())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}