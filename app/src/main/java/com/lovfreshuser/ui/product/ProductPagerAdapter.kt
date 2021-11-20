package com.lovfreshuser.ui.product

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lovfreshuser.models.ProductCategory

class ProductPagerAdapter(fragmentActivity: FragmentActivity, tabItemList: List<ProductCategory> , type : String = "") :
    FragmentStateAdapter(fragmentActivity) {
    val list = tabItemList
    val type = type

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return FoodMenuListFragment.instance(list[position].id , type)
    }
}