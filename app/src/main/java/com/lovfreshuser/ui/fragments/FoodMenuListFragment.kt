package com.lovfreshuser.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.HelperClass
import com.lovfreshuser.adapters.ProductListAdapter
import com.lovfreshuser.databinding.FragmentFoodMenuListBinding
import com.lovfreshuser.models.ProductDetailsModel
import com.lovfreshuser.models.ProductListResponse
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.ui.ProductDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FoodMenuListFragment : Fragment(), ProductListAdapter.Interaction {
    private lateinit var binding: FragmentFoodMenuListBinding
    private lateinit var manager: GridLayoutManager
    private lateinit var mAdapter: ProductListAdapter
    private var postList: MutableList<ProductDetailsModel> = ArrayList()
    var currentPage = 1
    var totalPage = 0
    var type = ""
    var itemCount = 0
    var tab_cat_id = 0
    var isScrolling = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0

    companion object {
        fun instance(cat_id: Int, type: String): FoodMenuListFragment {
            val data = Bundle()
            data.putInt("cat_id", cat_id)
            data.putString("type", type)
            return FoodMenuListFragment().apply {
                arguments = data
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFoodMenuListBinding.inflate(layoutInflater, container, false)
        manager = GridLayoutManager(context, 2)
        mAdapter = ProductListAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tab_cat_id = arguments?.getInt("cat_id", 0) ?: 0
        type = arguments?.getString("cat_id", "").toString()

        binding.rvProducts.apply {
            layoutManager = manager
            adapter = mAdapter
        }

        loadProductList(tab_cat_id, 1)
        initScrollListener()

    }

    private fun loadProductList(id: Int, page: Int) {
        binding.pbar.visibility = View.VISIBLE
        //?vendor=34&categories=131&page_size=3
        val productListCall =
            ApiProvider.dataApi.fetchProductList(34, id, 10, page, type )
        productListCall.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>
            ) {
                binding.pbar.visibility = View.GONE
                val model: ProductListResponse? = response.body()
                if (model?.meta != null && !model.data?.results.isNullOrEmpty()) {

                    totalPage = model.meta.total!!
                    currentPage = model.meta.current_page!!

                    postList = ArrayList(mAdapter.getList())
                    response.body()?.data?.results?.let { postList.addAll(it) }
                    mAdapter.submitList(postList)
                } else {

                }


            }

            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                binding.pbar.visibility = View.GONE
                context?.let { HelperClass.showErrorMsg("Error : ${t.localizedMessage}", it) }
            }

        })

    }

    override fun onItemSelected(position: Int, item: ProductDetailsModel) {

        val intent = Intent(context, ProductDetails::class.java)
        intent.putExtra("model", item)
        startActivity(intent)


    }

    private fun initScrollListener() {
        binding.rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) { // scroll down
                    currentItems = manager.childCount
                    totalItems = manager.itemCount
                    scrollOutItems = manager.findFirstVisibleItemPosition()
                    if (isScrolling && currentItems + scrollOutItems == totalItems) {
                        isScrolling = false
                        decideToLoadMore()
                    }
                }
            }
        })
    }

    private fun decideToLoadMore() {

        if (currentPage >= totalPage) {
            // last page
            context?.let { HelperClass.showWarningMsg("You Are At The Last Page", it) }

        } else {
            loadProductList(tab_cat_id, currentPage + 1)
        }

    }
}