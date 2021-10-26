package com.lovfreshuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ActivityProductMenuPageBinding

class ProductMenuPage : AppCompatActivity() {
    private  lateinit var  binding : ActivityProductMenuPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductMenuPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}