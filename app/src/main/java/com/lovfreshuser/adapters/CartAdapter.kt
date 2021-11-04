package com.lovfreshuser.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lovfreshuser.HelperClass
import com.lovfreshuser.R
import com.lovfreshuser.database.OfflineDatabase
import com.lovfreshuser.database.models.CartLocalDbModel
import com.lovfreshuser.databinding.ItemAddCheckoutCartBinding
import com.lovfreshuser.ui.CartPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartAdapter(
    private val interaction: Interaction? = null,
    val page: CartPage
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartLocalDbModel>() {

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
                R.layout.item_add_checkout_cart,
                parent,
                false
            ),
            interaction,
            page
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

    fun submitList(list: List<CartLocalDbModel>) {
        differ.submitList(ArrayList(list))
    }

    class viewholder(
        itemView: View,
        private val interaction: Interaction?,
        private val cartPage: CartPage
    ) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemAddCheckoutCartBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(item: CartLocalDbModel) {

            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            binding.tvProductTitle.text = item.itemName
            binding.tvQuantity.text = "${item.quantity}"
            binding.tvProductQuantity.text = "${item.unitValue} ${item.unit}"
            binding.tvTotalAmt.text = "$ ${String.format("%.2f", (item.quantity * item.price))}"
            Glide.with(itemView.context).load(item.image).apply(
                RequestOptions().placeholder(R.drawable.fruit_placeholder)
                    .error(R.drawable.fruit_placeholder)
            ).into(binding.riProductImg)


            binding.ivDelete.setOnClickListener {
                // delete this
                val dilalogeBuilder = HelperClass.alertDeleteDialog(binding.root.context)
                dilalogeBuilder.setMessage(binding.root.context.getString(R.string.delete_alert_msg))
                dilalogeBuilder.setPositiveButton(
                    binding.root.context.getText(R.string.confirm_delete)
                ) { dialog: DialogInterface, id: Int ->
                    val database: OfflineDatabase = OfflineDatabase(binding.root.context)

                    CoroutineScope(Dispatchers.IO).launch {
                        database.cartDao().delete(item = item)
                        withContext(Dispatchers.Main) {
                            cartPage.itemRemoved(absoluteAdapterPosition, item)
                        }

                        dialog.cancel()
                    }
                }
                dilalogeBuilder.setNegativeButton(
                    binding.root.context.getText(R.string.cancel)
                ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
                updateItem(item, itemView.context)
                val alert1 = dilalogeBuilder.create()
                alert1.show()
            }

            binding.ivAdd.setOnClickListener {

                val newQty = item.quantity + 1
                if (item.stockQuantity >= newQty) {
                    binding.tvTotalAmt.text = "$ ${String.format("%.2f", (newQty * item.price))}"
                    item.quantity = newQty
                    updateItem(item, itemView.context)
                    binding.tvQuantity.text = "${newQty}"
                } else {
                    HelperClass.showWarningMsg("Stock Limited", itemView.context)
                }
            }
            binding.ivMinus.setOnClickListener {
                val newQty = item.quantity - 1
                if (newQty >= 1) {
                    binding.tvQuantity.text = "${newQty}"
                    binding.tvTotalAmt.text = "$ ${String.format("%.2f", (newQty * item.price))}"
                    item.quantity = newQty
                    updateItem(item, itemView.context)
                    binding.tvQuantity.text = "${newQty}"

                } else {
                    HelperClass.showWarningMsg("Can't Be less than 1", binding.root.context)
                }
            }

        }

        fun updateItem(item: CartLocalDbModel, ctx: Context) {
            val database = OfflineDatabase(ctx)
            CoroutineScope(Dispatchers.IO).launch {
                database.cartDao().updateItem(item)
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(ctx, "Item Updated", Toast.LENGTH_SHORT).show()
//                }
            }

        }

    }


    interface Interaction {
        fun onItemSelected(position: Int, item: CartLocalDbModel)
    }
}



