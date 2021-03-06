package com.jsh.practice.presentation.ui.first

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jsh.practice.databinding.ItemShopListBinding
import com.jsh.practice.presentation.entity.PresenterShop
import com.jsh.practice.presentation.ui.first.MainAdapter.*
import com.jsh.practice.presentation.util.toStringLabel
import timber.log.Timber


class MainAdapter(private val viewModel: FirstViewModel):
    ListAdapter<PresenterShop, FirstViewHolder>(ShopDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstViewHolder {
        return FirstViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FirstViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun getItemCount(): Int {
        Timber.e("item count: ${super.getItemCount()}")
        return super.getItemCount()
    }

    class FirstViewHolder private constructor(val binding: ItemShopListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: FirstViewModel, item: PresenterShop ) {

            binding.shopImage.clipToOutline = true
            binding.viewModel = viewModel
            binding.shop = item
            binding.label = item.shopLabel.toStringLabel()
        }

        companion object {
            fun from(parent: ViewGroup): FirstViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemShopListBinding.inflate(layoutInflater, parent, false)

                return FirstViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class ShopDiffCallback : DiffUtil.ItemCallback<PresenterShop>() {
    override fun areItemsTheSame(oldItem: PresenterShop, newItem: PresenterShop): Boolean {
        return oldItem.shopId == newItem.shopId
    }

    override fun areContentsTheSame(oldItem: PresenterShop, newItem: PresenterShop): Boolean {
        return oldItem == newItem
    }
}