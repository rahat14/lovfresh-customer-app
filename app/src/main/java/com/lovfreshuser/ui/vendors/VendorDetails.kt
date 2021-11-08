package com.lovfreshuser.ui.vendors

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ActivityVedorDetailsBinding
import com.lovfreshuser.models.Vendor
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.networking.ApiService
import com.lovfreshuser.utils.HelperClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorDetails : AppCompatActivity() {
    private lateinit var binding: ActivityVedorDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVedorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val model = intent.getStringExtra("MODEL")
        if (model != null) {
            loadVendor("${model}")
        }
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.tvToolbarTitle.setText("Vendor Details")
    }


    private fun loadDetails(model: Vendor) {
        binding.tvEmail.text = "${model.email}"
        binding.tvPhone.text = "${model.mobile}"
        binding.tvShopNm.text = "${model.title}"
        binding.tvAddress.text = "${model.address?.address}"
        binding.tvDis.text = "${model.description}"
        try {
            Glide.with(applicationContext)
                .load(ApiService.IMAGE_URL + model.bannerImages?.get(0)?.image)
                .error(R.drawable.blank_img)
                .into(binding.riShopImg)

        } catch (Ex: Exception) {

        }

    }


    private fun loadVendor(id: String) {
        binding.loader.loadingPanel.visibility = View.VISIBLE
        val productListCall =
            ApiProvider.dataApi.getVendorDetails(id)
        productListCall.enqueue(object : Callback<List<Vendor>> {
            override fun onResponse(
                call: Call<List<Vendor>>,
                response: Response<List<Vendor>>
            ) {

                val model: List<Vendor>? = response.body()

                if (model.isNullOrEmpty() || model.size == 0) {

                } else {
                    binding.loader.loadingPanel.visibility = View.GONE
                    val vendor = model[0]
                    loadDetails(vendor)
                }


            }

            override fun onFailure(call: Call<List<Vendor>>, t: Throwable) {
                binding.loader.loadingPanel.visibility = View.GONE
                HelperClass.showErrorMsg("Error ${t.localizedMessage}", applicationContext)
            }

        })
    }
}