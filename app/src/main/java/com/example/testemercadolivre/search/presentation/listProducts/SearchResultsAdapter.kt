package com.example.testemercadolivre.search.presentation.listProducts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testemercadolivre.core.util.toCurrency
import com.example.testemercadolivre.databinding.ItemProductBinding
import com.example.testemercadolivre.search.domain.models.Product
import com.example.testemercadolivre.search.presentation.listProducts.SearchResultsAdapter.*

class SearchResultsAdapter(private val adapterListener: AdapterListener) :
    PagingDataAdapter<Product, ViewHolder>(SearchDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), adapterListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product?, itemClickListener: AdapterListener) {
            if (item != null) {
                binding.nameProduct.text = item.title
                binding.priceProduct.text = item.price.toCurrency()
                Glide.with(binding.root)
                    .load(item.thumbnail.replace("http:", "https:"))
                    .into(binding.imageProduct)
                binding.itemProduct.setOnClickListener { itemClickListener.onClick(item) }
            }
        }
    }

    interface AdapterListener {
        fun onClick(product: Product)
    }

    class SearchDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id
    }
}

