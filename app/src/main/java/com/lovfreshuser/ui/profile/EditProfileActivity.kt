package com.lovfreshuser.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lovfreshuser.Const
import com.lovfreshuser.R
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

        binding.topBar.ivBasket.visibility = View.GONE
        binding.topBar.tvEditBtn.visibility = View.GONE

        binding.topBar.ivBack.setOnClickListener {
            finish()
        }
        binding.topBar.tvEditBtn.setOnClickListener {
            startActivity(Intent(applicationContext, EditProfileActivity::class.java))
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
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.user_image)
            .into(binding.cmvProfile)



    }
}