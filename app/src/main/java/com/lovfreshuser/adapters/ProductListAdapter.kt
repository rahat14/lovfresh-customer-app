package com.lovfreshuser.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ItemProductListBinding
import com.lovfreshuser.models.ProductDetailsModel
import com.lovfreshuser.networking.ApiService

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
            var unitValue = ""
            var unitTitle = ""
            var unitId = ""
            val binding = ItemProductListBinding.bind(itemView)
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }
            if (!item.promotional_price.isNullOrEmpty()) {
                binding.tvPromoPrice.text = ("$" + item.promotional_price)
                binding.tvCalAmt.text = ("${item.price}")
                binding.tvStandPrice.text = ("was $${item.price}")
                binding.tvAmount.visibility = View.GONE
                binding.tvStandPrice.visibility = View.VISIBLE
                binding.tvPromoPrice.visibility = View.VISIBLE
                binding.tvCalAmt.visibility = View.GONE

            } else {
                binding.tvAmount.visibility = View.VISIBLE
                binding.tvStandPrice.visibility = View.GONE
                binding.tvPromoPrice.visibility = View.GONE
                binding.tvCalAmt.visibility = View.GONE
                binding.tvAmount.text = ("$ " + item.price)
            }
            val productAttributes = item.product_attributes
            if (!productAttributes.isNullOrEmpty() && productAttributes.isNotEmpty()) {
                unitValue = productAttributes[0].uom?.value.toString()
                unitTitle = productAttributes[0].uom?.name.toString()
                unitValue = productAttributes[0].uom?.id.toString()
                binding.tvProductTitle.text = "${item.title} ($unitValue$unitTitle)"

            }
            val imageList = item.images
            if (!imageList.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load("${imageList[0].image}")
                    .placeholder(R.drawable.fruit_placeholder)
                    .error(R.drawable.fruit_placeholder)
                    .into(binding.riProductImgs)
             //   Log.d("TAG", "bind: ${imageList[0].image}")
            }


        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ProductDetailsModel)
    }
}
