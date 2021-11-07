package com.lovfreshuser.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lovfreshuser.utils.HelperClass
import com.lovfreshuser.databinding.FragmentSpecialsBinding
import com.lovfreshuser.models.ProductCategory
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.ui.product.ProductPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SpecialsFragment : Fragment() {

    private lateinit var binding: FragmentSpecialsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSpecialsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTitleCategory()
        /*
    canceliing drag when swiping
     */
        binding.foodMenuContainerViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                enableDisableSwipeRefresh(state == ViewPager2.SCROLL_STATE_IDLE);
                super.onPageScrollStateChanged(state)
            }
        })
    }

    private fun enableDisableSwipeRefresh(enable: Boolean) {
        if (binding.swipeLayout != null) {
            binding.swipeLayout.isEnabled = enable
        }
    }

    private fun loadTitleCategory(isfromRefresh: Boolean = false) {

        val catCall =
            ApiProvider.dataApi.getShopOnlineCategoryOnTab("34")
        catCall.enqueue(object : Callback<List<ProductCategory>> {
            override fun onResponse(
                call: Call<List<ProductCategory>>,
                response: Response<List<ProductCategory>>
            ) {

                val tab_list = response.body()
                if (tab_list != null && !tab_list.isNullOrEmpty()) {
                    setupTab(tab_list)
                } else {

                }

                if (isfromRefresh) {
                    //   binding.swipeLayout.isRefreshing = false
                }
                Log.d("TAG", "onResponse: ${response.body().toString()}")


            }

            override fun onFailure(call: Call<List<ProductCategory>>, t: Throwable) {
                context?.let { HelperClass.showErrorMsg("Error ${t.localizedMessage}", it) }
            }

        })
    }


    private fun setupTab(productCategories: List<ProductCategory>) {
        // setup the adapter
        binding.foodMenuContainerViewPager.adapter = null
        val mAdapter = activity?.let { ProductPagerAdapter(it, productCategories , "SPECIALS") }
        binding.foodMenuContainerViewPager.adapter = mAdapter

        TabLayoutMediator(
            binding.tabCategory,
            binding.foodMenuContainerViewPager
        ) { tab, position ->
            tab.text = productCategories[position].name
        }.attach()

    }


}