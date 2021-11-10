package com.lovfreshuser.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.lovfreshuser.adapters.ProductListAdapter
import com.lovfreshuser.databinding.ActivitySearchBinding
import com.lovfreshuser.models.ProductDetailsModel
import com.lovfreshuser.models.ProductListResponse
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.ui.product.ProductDetails
import com.lovfreshuser.utils.HelperClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), ProductListAdapter.Interaction {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var mAdapter: ProductListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAdapter = ProductListAdapter(this)


        binding.rvSearchList.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
            adapter = mAdapter
        }
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.autoSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query?.lowercase())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // search(newText)
                return true
            }
        })


    }

    private fun search(query: String?) {
        binding.pbar.loadingPanel.visibility = View.VISIBLE
        //?vendor=34&categories=131&page_size=3
        val productListCall =
            query?.let {
                ApiProvider.dataApi.searchProducts(
                    HelperClass.getSelectedVendorID(),
                    it, 1100, 1
                )
            }
        productListCall?.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>
            ) {
                binding.pbar.loadingPanel.visibility = View.GONE
                val model: ProductListResponse? = response.body()

                response.body()?.data?.results?.let { mAdapter.submitList(it) }

            }

            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                binding.pbar.loadingPanel.visibility = View.GONE
                applicationContext?.let {
                    HelperClass.showErrorMsg(
                        "Error : ${t.localizedMessage}",
                        it
                    )
                }
            }

        })

    }

    override fun onItemSelected(position: Int, item: ProductDetailsModel) {
        val intent = Intent(applicationContext, ProductDetails::class.java)
        intent.putExtra("model", item)
        startActivity(intent)
    }

}