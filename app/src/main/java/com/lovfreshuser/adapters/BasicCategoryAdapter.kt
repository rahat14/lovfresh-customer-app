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
import com.lovfreshuser.databinding.ItemBasicCategoryBinding
import com.lovfreshuser.models.BasicCategoryModel

class BasicCategoryAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BasicCategoryModel>() {

        override fun areItemsTheSame(
            oldItem: BasicCategoryModel,
            newItem: BasicCategoryModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BasicCategoryModel,
            newItem: BasicCategoryModel
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return viewholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_basic_category,
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

    fun submitList(list: List<BasicCategoryModel>) {
        differ.submitList(list)
    }

    class viewholder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BasicCategoryModel) {
            val binding = ItemBasicCategoryBinding.bind(itemView)
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }
            Glide.with(itemView.context)
                .load(item.image)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.basicCategoryIv)

          //  binding.basicCategoryTv.text = "${item.name}"


        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BasicCategoryModel)
    }
}
