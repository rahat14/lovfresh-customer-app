package com.lovfreshuser.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lovfreshuser.Const
import com.lovfreshuser.R
import com.lovfreshuser.databinding.FragmentAccountBinding
import com.lovfreshuser.models.User
import com.lovfreshuser.ui.orders.OrderPage
import com.lovfreshuser.ui.profile.ProfileScreenActivity
import com.lovfreshuser.ui.referal.ReferFriend
import com.lovfreshuser.ui.settings.SettingsPage
import com.lovfreshuser.ui.vendors.StoreSelectionPage
import com.lovfreshuser.utils.SharedPrefManager


class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)


        binding.tvChangeStore.setOnClickListener {
            startActivity(Intent(context, StoreSelectionPage::class.java))
        }

        binding.tvReferFriend.setOnClickListener {
            startActivity(Intent(context, ReferFriend::class.java))
        }
        binding.tvOrderHistory.setOnClickListener {
            startActivity(Intent(context, OrderPage::class.java))
        }

        binding.tvSetting.setOnClickListener {
            startActivity(Intent(context, SettingsPage::class.java))
        }

        binding.tvProfile.setOnClickListener {
            startActivity(Intent(context, ProfileScreenActivity::class.java))
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            val uri =
                Uri.parse("https://docs.google.com/document/d/11c1ErmHT_LpUHyDZzWsvobJgLo5TxTVE_cTeXOs5Wo8") // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

        }
        binding.tvStoreInfo.setOnClickListener {
            val intent = Intent(context, StoreSelectionPage::class.java)
            intent.putExtra("isInfo", true)
            startActivity(intent)
        }

        val model: User? = SharedPrefManager.get(Const.USER_PREF)
        if (model != null) {
            setDetails(model)
        }


        return binding.root
    }

    private fun setDetails(user: User) {
        binding.tvUserName.setText("${user.first_name} ${user.last_name}")
        Glide.with(this@AccountFragment)
            .load(user.client_profile?.image)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.user_image)
            .into(binding.cmvProfile)

    }

}