package com.lovfreshuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ItemOrderListBinding
import com.lovfreshuser.models.OrderHistoryItem
import com.lovfreshuser.utils.HelperClass

class OrderHistoryAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderHistoryItem>() {

        override fun areItemsTheSame(
            oldItem: OrderHistoryItem,
            newItem: OrderHistoryItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OrderHistoryItem,
            newItem: OrderHistoryItem
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return orderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_order_list,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is orderViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<OrderHistoryItem>) {
        differ.submitList(list)
    }

    fun getList(): List<OrderHistoryItem> {
        return differ.currentList
    }

    class orderViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemOrderListBinding.bind(itemView)
        fun bind(item: OrderHistoryItem) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            binding.orderDate.setText(HelperClass.toDefaultFormattedDateStr(item.createdAt))
            binding.orderId.text = item.orderNumber
            binding.orderStatus.text = item.getStatus
            binding.tvCompOrderDate.text =
                item.deliverDate + "\n" + HelperClass.parseDateToddMMyyyy(item.startTime) + " to " + HelperClass.parseDateToddMMyyyy(
                    item.endTime
                )

        }
    }


    interface Interaction {
        fun onItemSelected(position: Int, item: OrderHistoryItem)
    }
}
