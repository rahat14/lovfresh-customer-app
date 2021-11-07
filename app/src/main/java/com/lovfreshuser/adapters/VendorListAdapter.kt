package com.lovfreshuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ItemSearchStoreBinding
import com.lovfreshuser.models.VendorItem

class VendorItemListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VendorItem>() {

        override fun areItemsTheSame(oldItem: VendorItem, newItem: VendorItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VendorItem, newItem: VendorItem): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return vendorAdapter(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search_store,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is vendorAdapter -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<VendorItem>) {
        differ.submitList(list)
    }

    class vendorAdapter
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSearchStoreBinding.bind(itemView)
        fun bind(item: VendorItem) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            binding.tvAddress.text = "${item.address?.address}"
            binding.tvProductNm.text = "${item.title}"
            try {
                Glide.with(binding.root)
                    .load(item.banner_images?.get(0)?.image.toString())
                    .error(R.drawable.blank_img)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.riShopImg)
            } catch (Ex: Exception) {

            }


        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: VendorItem)
    }
}
