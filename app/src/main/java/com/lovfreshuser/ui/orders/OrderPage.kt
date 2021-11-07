package com.lovfreshuser.ui.orders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.lovfreshuser.databinding.ActivityOrderPageBinding

class OrderPage : AppCompatActivity() {
    private val tabTitle = arrayOf("All", "Pending", "Completed", "Cancelled")
    private lateinit var binding: ActivityOrderPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mAdapter = OrderContianerPagerAdapter(this)
        binding.viewPager.adapter = mAdapter

        binding.include3.ivBack.setOnClickListener {
            finish()
        }
        binding.include3.tvToolbarTitle.text = "Order Details"

        TabLayoutMediator(
            binding.tabOrderCate,
            binding.viewPager
        ) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

    }
}