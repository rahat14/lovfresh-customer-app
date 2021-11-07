package com.lovfreshuser.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lovfreshuser.Const
import com.lovfreshuser.utils.HelperClass
import com.lovfreshuser.databinding.ActivityLoginPageBinding
import com.lovfreshuser.models.LoginResp
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPage : AppCompatActivity() {
    private lateinit var loginPageBinding: ActivityLoginPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginPageBinding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(loginPageBinding.root)

        loginPageBinding.cm.ivBack.visibility = View.GONE


        loginPageBinding.btnLogin.setOnClickListener {
            val email = loginPageBinding.edEmail.text.toString()
            val pass = loginPageBinding.edPassword.text.toString()
            if (email.isBlank() || pass.isBlank()) {
                HelperClass.showErrorMsg("Email Or Password can not be empty.", applicationContext)
            } else {
                // go with login feed ->
                makeUserLogin(email, pass)
            }
        }

        loginPageBinding.btnSignup.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterPage::class.java))
        }

    }

    fun Activity.showKbd(){
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.ime())
    }
    fun Activity.hideKbd(){
        WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
    }
    private fun makeUserLogin(email: String, pass: String) {
        hideKbd()
        val dialog = HelperClass.createProgressDialog(this@LoginPage, "Please Wait...")
        dialog.show()
        val data = ApiProvider.dataApi.loginUser(email, pass)

        data.enqueue(object : Callback<LoginResp> {
            override fun onResponse(call: Call<LoginResp>, response: Response<LoginResp>) {
                dialog.dismiss()
                if (response.code() == 200 && response.isSuccessful) {
                    SharedPrefManager.put(response.body()?.user, Const.USER_PREF)
                    SharedPrefManager.put(response.body()?.user?.token, Const.TOKEN)

                    HelperClass.showSuccessMsg(
                        response.body()?.message.toString(),
                        applicationContext
                    )


                } else if (response.code() == 400) {
                    HelperClass.showInfoMsg(
                        "Error : Unable to log in with provided credentials !!!",
                        applicationContext
                    )
                } else {
                    HelperClass.showInfoMsg("Error Code : ${response.code()}", applicationContext)
                }

            }

            override fun onFailure(call: Call<LoginResp>, t: Throwable) {
                dialog.dismiss()
                HelperClass.showInfoMsg("Error : ${t.message}", applicationContext)
            }

        })


    }

}