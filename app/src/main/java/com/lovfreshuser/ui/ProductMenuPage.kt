package com.lovfreshuser.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

    }

    private fun loadTitleCategory() {
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