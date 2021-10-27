package com.lovfreshuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.R
import com.lovfreshuser.models.ProductDetailsModel

class ProductListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductDetailsModel>() {

        override fun areItemsTheSame(
            oldItem: ProductDetailsModel,
            newItem: ProductDetailsModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductDetailsModel,
            newItem: ProductDetailsModel
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return viewholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product_list,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is viewholder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ProductDetailsModel>) {
        differ.submitList(list)
    }

    fun getList(): List<ProductDetailsModel> {
        return differ.currentList
    }


    class viewholder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ProductDetailsModel) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ProductDetailsModel)
    }
}
