package com.lovfreshuser.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lovfreshuser.Const
import com.lovfreshuser.databinding.ActivityEditProfileBinding
import com.lovfreshuser.models.User
import com.lovfreshuser.utils.SharedPrefManager

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model: User? = SharedPrefManager.get(Const.USER_PREF)
        if (model != null) {
            setDetails(model)
        }

    }

    private fun setDetails(user: User) {

        binding.tvUserName.setText("${user.first_name} ${user.last_name}")
        binding.edEmail.setText("${user.email}")
        binding.edFirstName.setText("${user.first_name}")
        binding.edLastName.setText("${user.last_name}")
        binding.edPhone.setText("${user.phone_number}")

        Glide.with(applicationContext)
            .load(user.client_profile?.image)
            .into(binding.cmvProfile)


    }
}