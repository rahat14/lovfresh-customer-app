package com.lovfreshuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lovfreshuser.databinding.ActivityVerifyOtpBinding

class VerifyOtp : AppCompatActivity() {
    private lateinit var  binding: ActivityVerifyOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cm.ivBack.visibility = View.GONE
        binding.cm.toolBar.title = "Verify Yourself"


    }

    override fun onStart() {
        super.onStart()
    }


}