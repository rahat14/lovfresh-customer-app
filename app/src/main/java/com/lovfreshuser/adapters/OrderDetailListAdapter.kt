package com.lovfreshuser.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ItemOrListBinding
import com.lovfreshuser.models.OrderProduct
import com.lovfreshuser.utils.HelperClass

class OrderDetailListAdapter(
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderProduct>() {

        override fun areItemsTheSame(
            oldItem: OrderProduct,
            newItem: OrderProduct
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OrderProduct,
            newItem: OrderProduct
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return viewholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_or_list,
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

    fun submitList(list: List<OrderProduct>) {
        differ.submitList(ArrayList(list))
    }

    class viewholder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemOrListBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(item: OrderProduct) {

            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            binding.tvQuantity.text = "${item.quantity} x ${item.price}"

            val qty = item.quantity?.toDouble()
            val price: Double? = item.price?.toDoubleOrNull()
            if (price != null && qty != null) {

                binding.tvAmount.text = "$${HelperClass.convertAmountToString(price * qty)} "

            }


            binding.tvProductNm.text = "${item.product?.title} "

        }


    }


    interface Interaction {
        fun onItemSelected(position: Int, item: OrderProduct)
    }
}



