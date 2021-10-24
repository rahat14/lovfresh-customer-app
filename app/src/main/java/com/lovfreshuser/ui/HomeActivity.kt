package com.lovfreshuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ActivityHomeBinding
import com.lovfreshuser.ui.fragments.ContianerPagerAdapter

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navHostFragment.adapter = ContianerPagerAdapter(this)
        binding.navHostFragment.isUserInputEnabled = false

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    binding.navHostFragment.setCurrentItem(0, false)
                }
                R.id.speacials -> {
                    binding.navHostFragment.setCurrentItem(1, false)
                }
                R.id.notification_fragment -> {
                    binding.navHostFragment.setCurrentItem(2, false)
                }
                R.id.accountFragment -> {
                    binding.navHostFragment.setCurrentItem(3, false)
                }
            }

            return@setOnItemSelectedListener true
        }
    }
}