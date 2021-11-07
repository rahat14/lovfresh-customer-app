package com.lovfreshuser.ui.vendors

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ActivityVedorDetailsBinding
import com.lovfreshuser.models.VendorItem
import com.lovfreshuser.networking.ApiService
import java.lang.Exception

class VendorDetails : AppCompatActivity() {
    private lateinit var binding: ActivityVedorDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVedorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val model: VendorItem? = intent.getSerializableExtra("MODEL") as VendorItem?
        if (model != null) {
            loadDetails(model)
        }
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.tvToolbarTitle.setText("Vendor Details")
    }


    private fun loadDetails(model: VendorItem) {
        binding.tvEmail.setText("${model.address?.mobile}")
        binding.tvPhone.setText("${model.address?.mobile}")
        binding.tvShopNm.setText("${model.address?.full_name}")
        binding.tvAddress.setText("${model.address?.address}")
        binding.tvDis.setText("${model.description}")
        try {
            Glide.with(applicationContext)
                .load(ApiService.IMAGE_URL + model.banner_images?.get(0)?.image)
                .error(R.drawable.blank_img)
                .into(binding.riShopImg)

        }catch (Ex : Exception){

        }

    }
}