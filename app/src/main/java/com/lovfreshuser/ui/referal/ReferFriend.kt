package com.lovfreshuser.ui.referal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lovfreshuser.BuildConfig
import com.lovfreshuser.databinding.ActivityReferFriendBinding

class ReferFriend : AppCompatActivity() {
    private lateinit var binding: ActivityReferFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.top.ivBack.setOnClickListener {
            finish()
        }

        binding.top.tvToolbarTitle.setText("Refer Friends")
        binding.btnSend.setOnClickListener {
            referAFriend()
        }

    }

    fun referAFriend() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LovFreshCustomer")
            var shareMessage = "\nCheck out the App at:\n\n"
            shareMessage =
                """
            ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
            
            Use Refer Code :
            """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: Exception) {
            e.toString()
        }
    }
}