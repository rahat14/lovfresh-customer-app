package com.lovfreshuser.ui.orders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrderContianerPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderFragment("")
            1 -> OrderFragment("Pending")
            2 -> OrderFragment("Completed")
            3 -> OrderFragment("rejected")

            else -> { // Note the block
                OrderFragment("")
            }
        }

    }
}