package com.lovfreshuser.ui.address

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lovfreshuser.adapters.ShippingAddressAdapter
import com.lovfreshuser.databinding.ActivityShippingAddressListBinding
import com.lovfreshuser.models.ShippingAddressModel
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.ui.pickUpDelivery.PaymentActivity
import com.lovfreshuser.utils.HelperClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShippingAddressListActivity : AppCompatActivity(), ShippingAddressAdapter.Interaction {
    private lateinit var binding: ActivityShippingAddressListBinding
    private lateinit var mAdapter: ShippingAddressAdapter
    var shippingAddress: ShippingAddressModel = ShippingAddressModel()
    var model: List<ShippingAddressModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isFromCart = intent.getBooleanExtra("isFromCart", false)

        if (isFromCart) {
            binding.btnContinue.visibility = View.VISIBLE


        }

        binding.btnContinue.setOnClickListener {
            val intent1 = Intent(this, PaymentActivity::class.java)
            intent1.putExtra("ADDRESS", shippingAddress)
            startActivity(intent1)


        }


        mAdapter = ShippingAddressAdapter(this)

        binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(applicationContext, AddShippingAddress::class.java))
        }

        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(this@ShippingAddressListActivity)
            adapter = mAdapter
        }


    }

    override fun onItemSelected(position: Int, item: ShippingAddressModel) {
        shippingAddress = item
        for (mo in model) {
            mo.SelectedAddress = false
        }

        model[position].SelectedAddress = true


        mAdapter.submitList(emptyList())
        mAdapter.submitList(ArrayList(model).toList())
        mAdapter.notifyDataSetChanged()


    }

    override fun onResume() {
        super.onResume()
        loadAddress()
    }

    private fun loadAddress() {
        binding.loader.loadingPanel.visibility = View.VISIBLE
        val productListCall =
            ApiProvider.dataApi.getAddress()
        productListCall.enqueue(object : Callback<List<ShippingAddressModel>> {
            override fun onResponse(
                call: Call<List<ShippingAddressModel>>,
                response: Response<List<ShippingAddressModel>>
            ) {

                model = response.body()!!

                if (model.isNullOrEmpty()) {

                } else {
                    binding.loader.loadingPanel.visibility = View.GONE
                    mAdapter.submitList(model)
                }


            }

            override fun onFailure(call: Call<List<ShippingAddressModel>>, t: Throwable) {
                binding.loader.loadingPanel.visibility = View.GONE
                HelperClass.showErrorMsg("Error ${t.localizedMessage}", applicationContext)
            }

        })
    }
}