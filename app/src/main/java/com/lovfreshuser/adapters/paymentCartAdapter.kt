package com.lovfreshuser.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.utils.HelperClass
import com.lovfreshuser.R
import com.lovfreshuser.database.models.CartLocalDbModel
import com.lovfreshuser.databinding.ItemOrListBinding

class paymentCartAdapter(
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartLocalDbModel>() {

        override fun areItemsTheSame(
            oldItem: CartLocalDbModel,
            newItem: CartLocalDbModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CartLocalDbModel,
            newItem: CartLocalDbModel
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
                holder.bind(differ.currentList[position], differ.currentList)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<CartLocalDbModel>) {
        differ.submitList(ArrayList(list))
    }

    class viewholder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemOrListBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(item: CartLocalDbModel, currentList: List<CartLocalDbModel>) {

            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            binding.tvQuantity.text = "${item.quantity} x ${item.price}"

            binding.tvAmount.text =
                "$${HelperClass.convertAmountToString(item.price * item.quantity)} "

            binding.tvProductNm.text = "${item.itemName} "

        }


    }


    interface Interaction {
        fun onItemSelected(position: Int, item: CartLocalDbModel)
    }
}



