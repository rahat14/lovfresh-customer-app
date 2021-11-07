package com.lovfreshuser.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lovfreshuser.databinding.ActivitySettingsPageBinding

class SettingsPage : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topBar.tvToolbarTitle.setText("Settings")

        binding.topBar.toolBar.setOnClickListener {
            finish()
        }

        binding.tvNotification.setOnClickListener {
            startActivity(Intent(applicationContext , NotificaitonSettings::class.java))
        }



    }
}