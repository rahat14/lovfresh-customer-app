package com.lovfreshuser.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lovfreshuser.HelperClass
import com.lovfreshuser.databinding.ActivityProductMenuPageBinding
import com.lovfreshuser.models.ProductCategory
import com.lovfreshuser.models.Vendor_Product_Category
import com.lovfreshuser.networking.ApiProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductMenuPage : AppCompatActivity() {
    private lateinit var binding: ActivityProductMenuPageBinding
    private lateinit var ctx: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductMenuPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ctx = binding.root.context

        loadTitleCategory()
        binding.swipeLayout.setOnRefreshListener(this::fetchTimelineAsync)

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

    private fun fetchTimelineAsync() {
        loadTitleCategory(true)
    }

    private fun loadTitleCategory(isfromRefresh: Boolean = false) {

        val catCall =
            ApiProvider.dataApi.getProductCategoryOnTab("${intent.getIntExtra("basic_cat_id", 0)}")
        catCall.enqueue(object : Callback<Vendor_Product_Category> {
            override fun onResponse(
                call: Call<Vendor_Product_Category>,
                response: Response<Vendor_Product_Category>
            ) {

                val tab_list = response.body()
                if (tab_list != null && !tab_list.product_categories.isNullOrEmpty()) {
                    setupTab(tab_list.product_categories)
                } else {

                }

                if (isfromRefresh) {
                    binding.swipeLayout.isRefreshing = false
                }
                Log.d("TAG", "onResponse: ${response.body().toString()}")


            }

            override fun onFailure(call: Call<Vendor_Product_Category>, t: Throwable) {
                HelperClass.showErrorMsg("Error ${t.localizedMessage}", applicationContext)
            }

        })
    }

    private fun setupTab(productCategories: List<ProductCategory>) {
        // setup the adapter
        binding.foodMenuContainerViewPager.adapter = null
        val mAdapter = ProductPagerAdapter(this, productCategories)
        binding.foodMenuContainerViewPager.adapter = mAdapter

        TabLayoutMediator(
            binding.tabCategory,
            binding.foodMenuContainerViewPager
        ) { tab, position ->
            tab.text = productCategories[position].name
        }.attach()

    }

}