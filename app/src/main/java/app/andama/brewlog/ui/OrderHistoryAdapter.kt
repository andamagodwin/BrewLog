package app.andama.brewlog.ui

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import app.andama.brewlog.databinding.ItemOrderHistoryBinding
import app.andama.brewlog.data.local.OrderHistoryItem

class OrderHistoryAdapter(
    private val context: Context,
    private val onEditStatusClicked: (OrderHistoryItem) -> Unit,
    private val onDeleteClicked: (OrderHistoryItem) -> Unit,
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    private val items = mutableListOf<OrderHistoryItem>()

    fun submitList(historyItems: List<OrderHistoryItem>) {
        items.clear()
        items.addAll(historyItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class OrderHistoryViewHolder(
        private val binding: ItemOrderHistoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderHistoryItem) {
            binding.textViewHistoryCafeName.text = item.cafeName
            binding.textViewHistoryProductName.text = item.productName
            binding.textViewHistoryQuantityAndPrice.text = "${formatQuantity(item.quantityKg)} • ${formatCurrency(item.totalPrice)}"
            binding.textViewHistoryDate.text = item.orderDate

            val status = resolveOrderStatusBadge(item.orderStatus)
            binding.textViewHistoryStatusBadge.text = status.label
            ViewCompat.setBackgroundTintList(
                binding.textViewHistoryStatusBadge,
                ColorStateList.valueOf(ContextCompat.getColor(context, status.colorRes)),
            )

            binding.buttonEditOrderStatusRow.setOnClickListener { onEditStatusClicked(item) }
            binding.buttonDeleteOrderRow.setOnClickListener { onDeleteClicked(item) }
        }
    }
}