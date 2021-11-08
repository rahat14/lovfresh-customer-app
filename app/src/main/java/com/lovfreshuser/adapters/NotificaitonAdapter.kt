package com.lovfreshuser.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ItemNotificationStoreBinding
import com.lovfreshuser.models.notification.NotificationOrderItem
import com.lovfreshuser.utils.HelperClass

class NotificaitonAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NotificationOrderItem>() {

        override fun areItemsTheSame(
            oldItem: NotificationOrderItem,
            newItem: NotificationOrderItem
        ): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(
            oldItem: NotificationOrderItem,
            newItem: NotificationOrderItem
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return NotificaitonViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_notification_store,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotificaitonViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<NotificationOrderItem>) {
        differ.submitList(list)
    }


    fun getList(): List<NotificationOrderItem> {
        return differ.currentList
    }


    class NotificaitonViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        val holder =  ItemNotificationStoreBinding.bind(itemView)

        fun bind(item: NotificationOrderItem) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            holder.tvOrderdate.text = HelperClass.toDefaultFormattedDateStr(item.createdAt)
            holder.tvOrderType.text = item.orderType
            holder.tvOrderId.text = item.orderNumber
            holder.tvOrderStatus.text = item.getStatus
            holder.tvPickupSlot.text = "${HelperClass.convertDateFormat(item.deliverDate) + "-" + HelperClass.parseDateToddMMyyyy(
                item.startTime
            ) + "-" + HelperClass.parseDateToddMMyyyy(item.endTime)}"

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: NotificationOrderItem)
    }
}
