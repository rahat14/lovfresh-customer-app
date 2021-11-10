package com.lovfreshuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.lovfreshuser.databinding.ActivityVerifyOtpBinding
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.ui.vendors.StoreSelectionPage
import com.lovfreshuser.utils.HelperClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyOtp : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding
    var otp = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cm.ivBack.visibility = View.GONE
        binding.cm.toolBar.title = "Verify Yourself"


        binding.tvResend.setOnClickListener {
            otp = HelperClass.getOTP()
            makeUserLogin(intent.getStringExtra("phone").toString(), otp)

        }

        //call for otp

        binding.btnSubmit.setOnClickListener {
            if (binding.edOtp.text.toString() == otp) {
                // veritfied
                startActivity(Intent(applicationContext, StoreSelectionPage::class.java))
                finish()

            } else {
                HelperClass.showErrorMsg("OTP doesn't Match !!", applicationContext)
            }
        }


    }

    override fun onStart() {
        super.onStart()
        otp = HelperClass.getOTP()
        makeUserLogin(intent.getStringExtra("phone").toString(), otp)
    }

    private fun makeUserLogin(email: String, pass: String) {

        val dialog = HelperClass.createProgressDialog(this@VerifyOtp, "Please Wait...")
        dialog.show()
        val data = ApiProvider.dataApi.sendOtp(email, pass)

        data.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                if (response.code() == 200 && response.isSuccessful) {

                } else if (response.code() == 400) {
                    HelperClass.showInfoMsg(
                        "Error : Unable to log in with provided credentials !!!",
                        applicationContext
                    )
                } else {
                    HelperClass.showInfoMsg("Error Code : ${response.code()}", applicationContext)
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
                HelperClass.showInfoMsg("Error : ${t.message}", applicationContext)
            }

        })


    }

}