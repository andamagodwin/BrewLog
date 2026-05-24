package app.andama.brewlog.ui

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import app.andama.brewlog.R
import app.andama.brewlog.data.local.Product
import app.andama.brewlog.databinding.ItemProductBinding

class ProductAdapter(
    private val context: Context,
    private val onEditClicked: (Product) -> Unit,
    private val onDeleteClicked: (Product) -> Unit,
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val items = mutableListOf<Product>()

    fun submitList(products: List<Product>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ProductViewHolder(
        private val binding: ItemProductBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textViewProductName.text = product.name
            binding.textViewProductOrigin.text = "Origin: ${product.origin}"
            binding.textViewProductDetails.text = "Roast: ${product.roastLevel} • Roast Date: ${product.roastDate}"
            binding.textViewProductPrice.text = "${formatCurrency(product.pricePerKg)} / kg"
            binding.textViewProductStock.text = "Stock: ${formatQuantity(product.stockKg)}"

            val freshness = resolveFreshnessBadge(calculateDaysSinceRoast(product.roastDate))
            binding.textViewFreshnessBadge.text = freshness.label
            ViewCompat.setBackgroundTintList(
                binding.textViewFreshnessBadge,
                ColorStateList.valueOf(ContextCompat.getColor(context, freshness.colorRes)),
            )

            binding.buttonEditProductRow.setOnClickListener { onEditClicked(product) }
            binding.buttonDeleteProductRow.setOnClickListener { onDeleteClicked(product) }
            binding.root.setOnClickListener { onEditClicked(product) }
        }
    }
}