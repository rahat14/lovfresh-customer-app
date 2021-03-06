package com.lovfreshuser.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.lovfreshuser.R
import com.lovfreshuser.utils.HelperClass

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            if (HelperClass.isUserLoggedIn()) {
                val mainIntent = Intent(this, HomeActivity::class.java)
                startActivity(mainIntent)
            } else {
                val mainIntent = Intent(this, LoginPage::class.java)
                startActivity(mainIntent)
            }
            finish()
        }, 700)
    }
}