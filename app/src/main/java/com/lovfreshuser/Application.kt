package com.lovfreshuser

import android.app.Application
import com.lovfreshuser.utils.SharedPrefManager

class Application : Application() {
    private val ONESIGNAL_APP_ID = ""
    override fun onCreate() {
        super.onCreate()

        SharedPrefManager.with(this)
        // OneSignal Initialization
        // OneSignal.initWithContext(this)
        // OneSignal.setAppId(ONESIGNAL_APP_ID)

    }
}