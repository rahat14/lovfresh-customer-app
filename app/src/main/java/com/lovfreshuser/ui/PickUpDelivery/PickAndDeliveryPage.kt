package com.lovfreshuser.ui.PickUpDelivery

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lovfreshuser.databinding.ActivityPickAndDeliveryPageBinding

class PickAndDeliveryPage : AppCompatActivity() {
    private lateinit var binding: ActivityPickAndDeliveryPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickAndDeliveryPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardHome.setOnClickListener {
            val delivery_status = "delivery"
            val intent = Intent(applicationContext, ShippingAddressListActivity::class.java)
            intent.putExtra("ORDER_TYPE", delivery_status)
            intent.putExtra("DELIVERY_COST", "5.00")
            //SessionManager(context).setSeelction(model.getId())
            startActivity(intent)
        }
        binding.cardPickup.setOnClickListener {
            val delivery_status = "pickup"
            val intent = Intent(applicationContext, PaymentActivity::class.java)
            intent.putExtra("ORDER_TYPE", delivery_status)
            intent.putExtra("DELIVERY_COST", "0.0")
           // SessionManager(context).setSeelction(model.getId())
            startActivity(intent)
        }


    }

}