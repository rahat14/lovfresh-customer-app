package com.lovfreshuser.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lovfreshuser.Const
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ActivityProfileScreenBinding
import com.lovfreshuser.models.User
import com.lovfreshuser.utils.SharedPrefManager

class ProfileScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val model: User? = SharedPrefManager.get(Const.USER_PREF)
        if (model != null) {
            setDetails(model)
        }


    }

    private fun setDetails(user: User) {


        binding.edEmail.setText("${user.email}")
        binding.edFirstName.setText("${user.first_name}")
        binding.edLastName.setText("${user.last_name}")
        binding.edPhone.setText("${user.phone_number}")
        binding.tvUserName.text = "${user.first_name} ${user.last_name}"

        Glide.with(applicationContext)
            .load(user.client_profile?.image)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.user_image)
            .into(binding.cmvProfile)


    }
}