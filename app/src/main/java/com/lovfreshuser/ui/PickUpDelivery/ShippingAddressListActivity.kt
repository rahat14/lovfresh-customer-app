package com.lovfreshuser.ui.PickUpDelivery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lovfreshuser.databinding.ActivityPickAndDeliveryPageBinding

class ShippingAddressListActivity : AppCompatActivity() {
    private  lateinit var  binding: ActivityPickAndDeliveryPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickAndDeliveryPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}