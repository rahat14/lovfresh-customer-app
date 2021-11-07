package com.lovfreshuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ItemRvTimeBinding
import com.lovfreshuser.models.TimeModel

class TimeAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dateSelectedPosition = -1

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TimeModel>() {

        override fun areItemsTheSame(oldItem: TimeModel, newItem: TimeModel): Boolean {
            return oldItem.strt_time_end_time == newItem.strt_time_end_time
        }

        override fun areContentsTheSame(oldItem: TimeModel, newItem: TimeModel): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return dateViewmodel(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv_time,
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

    fun submitList(list: List<TimeModel>) {
        differ.submitList(list)
    }

    class dateViewmodel
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRvTimeBinding.bind(itemView)

        fun bind(item: TimeModel) {
            itemView.setOnClickListener {
                interaction?.onTimeItemSelected(absoluteAdapterPosition, item)
            }

                // Is the button now checked?
                val checked =  binding.rbTime.isChecked

                // Check which radio button was clicked
                binding.rbTime.setOnClickListener {
                    interaction?.onTimeItemSelected(absoluteAdapterPosition, item)
                }


                binding.rbTime.text = "${item.strt_time_end_time}"


    }

    fun onRadioButtonClicked(view: View) {
      }
    }

    interface Interaction {
        fun onTimeItemSelected(position: Int, item: TimeModel)
    }
}
