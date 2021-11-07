package com.lovfreshuser.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lovfreshuser.utils.HelperClass
import com.lovfreshuser.adapters.CartAdapter
import com.lovfreshuser.database.OfflineDatabase
import com.lovfreshuser.database.models.CartLocalDbModel
import com.lovfreshuser.databinding.ActivityCartPageBinding
import com.lovfreshuser.ui.pickUpDelivery.PickAndDeliveryPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartPage : AppCompatActivity(), CartAdapter.Interaction {
    private lateinit var mAdapter: CartAdapter
    private lateinit var binding: ActivityCartPageBinding
    private lateinit var localDb: OfflineDatabase
    var list: MutableList<CartLocalDbModel> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        localDb = OfflineDatabase(this)
        mAdapter = CartAdapter(this, this@CartPage)

        binding.rvCarts.apply {
            layoutManager = LinearLayoutManager(this@CartPage)
            adapter = mAdapter
        }
        getAllCarts()

        binding.btnBuy.setOnClickListener {
            startActivity(Intent(applicationContext , PickAndDeliveryPage::class.java))
        }

    }

    private fun getAllCarts() {
        CoroutineScope(Dispatchers.IO).launch {
            list = localDb.cartDao().getAll()
            withContext(Dispatchers.Main) {
                loadAllCarts(list)
            }
        }


    }

    private fun loadAllCarts(items: MutableList<CartLocalDbModel>) {

        mAdapter.submitList(items)
        binding.tvTotalAmount.text =
            "${HelperClass.convertAmountToString(HelperClass.getTotalOfCart(items))}"
    }

     fun updatePrice(totalPrice: Double) {
        binding.tvTotalAmount.text = "${HelperClass.convertAmountToString(totalPrice)}"
    }

    override fun onItemSelected(position: Int, item: CartLocalDbModel) {

    }

    public fun itemRemoved(pos: Int, item: CartLocalDbModel) {
        list.remove(item)
        mAdapter.submitList(list)
    }

}