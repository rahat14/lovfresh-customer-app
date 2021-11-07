package com.lovfreshuser.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.adapters.OrderHistoryAdapter
import com.lovfreshuser.databinding.FragmentOrderBinding
import com.lovfreshuser.models.OrderHistoryItem
import com.lovfreshuser.models.OrderHistoryResponse
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.utils.HelperClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderFragment(type: String) : Fragment(), OrderHistoryAdapter.Interaction {
    var currentPage = 1
    var totalPage = 0
    var type = ""
    var itemCount = 0
    var tab_cat_id = 0
    var isScrolling = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    private var postList: MutableList<OrderHistoryItem> = ArrayList()
    private lateinit var manager: LinearLayoutManager
    private lateinit var binding: FragmentOrderBinding
    private lateinit var mAdapter: OrderHistoryAdapter
    private val orderStatus = type
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(layoutInflater, container, false)
        manager = LinearLayoutManager(binding.root.context)
        mAdapter = OrderHistoryAdapter(this)
        binding.rvOrder.apply {
            layoutManager = manager
            adapter = mAdapter

        }
        initScrollListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadOrders(1, orderStatus)
    }

    private fun loadOrders(page: Int, orderStatus: String) {
        binding.loadingProgressbar.visibility = View.VISIBLE
        val productListCall = ApiProvider.dataApi.getPrevOrders(10, page, orderStatus.lowercase())
        productListCall.enqueue(object : Callback<OrderHistoryResponse> {
            override fun onResponse(
                call: Call<OrderHistoryResponse>,
                response: Response<OrderHistoryResponse>
            ) {
                binding.loadingProgressbar.visibility = View.GONE
                val model: OrderHistoryResponse? = response.body()
                if (model?.meta != null && !model.data?.results.isNullOrEmpty()) {
                    totalPage = model.meta!!.total!!
                    currentPage = model.meta!!.current_page!!
                    postList = ArrayList(mAdapter.getList())
                    response.body()?.data?.results?.let { postList.addAll(it) }
                    mAdapter.submitList(postList)
                }


            }

            override fun onFailure(call: Call<OrderHistoryResponse>, t: Throwable) {
                binding.loadingProgressbar.visibility = View.GONE
                context?.let { HelperClass.showErrorMsg("Error : ${t.localizedMessage}", it) }
            }

        })

    }

    override fun onItemSelected(position: Int, item: OrderHistoryItem) {
        // to do

    }


    private fun initScrollListener() {
        binding.rvOrder.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
            loadOrders(currentPage + 1, orderStatus)
        }

    }
}