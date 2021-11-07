package com.lovfreshuser.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lovfreshuser.databinding.ActivityNotificaitonSettingsBinding

class NotificaitonSettings : AppCompatActivity() {
    private lateinit var binding: ActivityNotificaitonSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificaitonSettingsBinding.inflate(layoutInflater)

        binding.toolbar.tvToolbarTitle.text = "Notificaiton Settings"

        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }


        setContentView(binding.root)
    }

    data class settingsModel(
        var sound: Boolean,
        var vibration: Boolean,
        var sms: Boolean
    )
}