package com.lovfreshuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ItemRvDateBinding
import com.lovfreshuser.models.DateModel

class DateAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dateSelectedPosition = -1

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DateModel>() {

        override fun areItemsTheSame(oldItem: DateModel, newItem: DateModel): Boolean {
            return oldItem.day_int == newItem.day_int && oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DateModel, newItem: DateModel): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return dateViewmodel(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv_date,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is dateViewmodel -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<DateModel>) {
        differ.submitList(list)
    }

    class dateViewmodel
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRvDateBinding.bind(itemView)

        fun bind(item: DateModel) {
            itemView.setOnClickListener {
                interaction?.onDateItemSelected(absoluteAdapterPosition, item)
            }
            binding.tvDate.text = "${item.month} ${item.day_int} \n ${item.day_str}"

        }
    }

    interface Interaction {
        fun onDateItemSelected(position: Int, item: DateModel)
    }
}
