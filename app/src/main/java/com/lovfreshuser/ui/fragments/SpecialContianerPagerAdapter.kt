package com.lovfreshuser.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SpecialContianerPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SpecialFoodMenuListFragment(1)
            1 -> SpecialFoodMenuListFragment(2)


            else -> { // Note the block
                HomeFragment()
            }
        }

    }
}