package com.lovfreshuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lovfreshuser.utils.HelperClass
import com.lovfreshuser.R
import com.lovfreshuser.database.OfflineDatabase
import com.lovfreshuser.databinding.ActivityHomeBinding
import com.lovfreshuser.ui.fragments.ContianerPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        binding.topBar.dd.setOnClickListener {
            startActivity(Intent(applicationContext , CartPage::class.java))
        }
    }

    private fun cartCount() {
        binding.topBar.tvCartCount.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val item = HelperClass.getCountOfCarts(OfflineDatabase(applicationContext))
            withContext(Dispatchers.Main) {
                binding.topBar.tvCartCount.text = "${item}"

            }
        }

    }

    override fun onResume() {
        super.onResume()
        cartCount()
    }
}