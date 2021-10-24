package com.lovfreshuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.gson.JsonObject
import com.lovfreshuser.Const
import com.lovfreshuser.HelperClass
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ActivityRegisterPageBinding
import com.lovfreshuser.models.LoginResp
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPage : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cm.toolBar.title = "Register"
        binding.cm.ivBack.visibility = View.GONE

        binding.cm.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSignup.setOnClickListener {

            val pass = binding.edPassword.text.toString()
            val con_pass = binding.edConfPass.text.toString()
            val fname = binding.edFirstNm.text.toString()
            val sname = binding.edLastNm.text.toString()
            var phone = binding.edPhone.text.toString()
            val code = binding.ccp.selectedCountryCode
            val email = binding.edEmail.text.toString()

            if (binding.cbTermCondi.isChecked) {

                if (email.isNotBlank() && HelperClass.isValidEmail(email)) {
                    if (phone.isNotBlank() && HelperClass.isValidPhone(phone)) {
                        phone = code + phone
                        if (HelperClass.isValidPasswordz(pass)) {
                            if (fname.isNotBlank() && sname.isNotBlank()) {
                                if (pass == con_pass) {
                                    signUp(fname, sname, phone, pass , email)

                                } else {
                                    binding.edConfPass.error =
                                        getString(R.string.error_password_mismatch)
                                }

                            } else {
                                HelperClass.showWarningMsg(
                                    "First Name Or Last name Can't Be Empty.",
                                    applicationContext
                                )
                            }
                        } else {
                            binding.edPassword.error = getString(R.string.error_invalid_pass)
                        }
                    } else {
                        binding.edPhone.error = getString(R.string.invalid_mobile)
                    }
                } else {
                    binding.edEmail.error = getString(R.string.error_invalid_email)
                    binding.edEmail.requestFocus()
                }


            } else {
                HelperClass.showWarningMsg(
                    "Please Accept Our Terms & Conditions ",
                    applicationContext
                )
            }


        }

    }

    fun goToNextPage() {
        val intent = Intent(applicationContext, VerifyOtp::class.java)
        startActivity(intent)
    }


    private fun signUp(fname: String, sname: String, phone: String, pass: String, email: String) {
        //hide keyboard
        WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
        val dialog = HelperClass.createProgressDialog(this@RegisterPage, "Signing Up...")
        dialog.show()
        val data = ApiProvider.dataApi.signUpUser(pass , pass , email , phone , fname , sname)

        data.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                if (response.code() == 201 && response.isSuccessful) {

                    goToNextPage()
                }
                else {
                    HelperClass.showInfoMsg("Error : ${response.code()} Please Check Your Entered Data", applicationContext)
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
                HelperClass.showInfoMsg("Error : ${t.message}", applicationContext)
            }

        })

    }


}