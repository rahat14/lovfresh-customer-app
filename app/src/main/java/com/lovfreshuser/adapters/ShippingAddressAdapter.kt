package com.lovfreshuser.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ItemShippingAddressBinding
import com.lovfreshuser.models.ShippingAddressModel

class ShippingAddressAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShippingAddressModel>() {

        override fun areItemsTheSame(
            oldItem: ShippingAddressModel,
            newItem: ShippingAddressModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShippingAddressModel,
            newItem: ShippingAddressModel
        ): Boolean {
            return oldItem == newItem || oldItem.is_selected == newItem.is_selected
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return viewholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shipping_address,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is viewholder -> {
                holder.bind(differ.currentList[position], differ.currentList, differ)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ShippingAddressModel>) {
        differ.submitList(ArrayList(list))
    }

    class viewholder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemShippingAddressBinding.bind(itemView)
        fun bind(
            model: ShippingAddressModel,
            currentList: MutableList<ShippingAddressModel>,
            differ: AsyncListDiffer<ShippingAddressModel>
        ) {
            itemView.setOnClickListener {

                binding.rbSelect.isChecked = !binding.rbSelect.isChecked
                interaction?.onItemSelected(absoluteAdapterPosition, model)
            }
            binding.tvAddress.setText("")
            if (model.address != null && model.address.isNotEmpty()) {
                binding.tvAddress.setText(model.address)
            }
            binding.tvPhoneNo.setText("")
            if (model.mobile != null && model.mobile.isNotEmpty()) {
                binding.tvPhoneNo.setText("Phone: " + model.mobile)
            }
            binding.tvHouseNo.setText("")
            if (model.flat_number != null && model.flat_number.isNotEmpty()) {
                binding.tvHouseNo.setText(model.flat_number)
            }
            binding.tvEmail.setText("")
            if (model.email.isNotEmpty()) {
                binding.tvEmail.text = "Email: " + model.email
            }
           binding.rbSelect.isChecked = model.SelectedAddress
            Log.d("TAG", "bind: ${model.SelectedAddress}")
//
//            binding.rbSelect.setOnCheckedChangeListener { p0, p1 ->
//                for (item in currentList) {
//                    item.SelectedAddress = false
//                }
//                currentList[absoluteAdapterPosition].SelectedAddress = p1
//                differ.submitList(currentList)
//                bindingAdapter?.notifyDataSetChanged()
//            }




        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ShippingAddressModel)
    }
}
