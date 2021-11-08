package com.lovfreshuser.ui.notificaitons

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lovfreshuser.adapters.NotificaitonAdapter
import com.lovfreshuser.databinding.ActivityNotificationListBinding
import com.lovfreshuser.models.notification.NotificationOrderItem
import com.lovfreshuser.models.notification.NotificationResponse
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.ui.orders.OrderDetailPopup
import com.lovfreshuser.utils.HelperClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationList : AppCompatActivity(), NotificaitonAdapter.Interaction {
    private lateinit var binding: ActivityNotificationListBinding
    private lateinit var mAdapter: NotificaitonAdapter

    var currentPage = 1
    var totalPage = 0
    var type = ""
    var itemCount = 0
    var tab_cat_id = 0
    var isScrolling = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    private var postList: MutableList<NotificationOrderItem> = ArrayList()
    private lateinit var manager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manager = LinearLayoutManager(this)
        mAdapter = NotificaitonAdapter(this@NotificationList)
        binding.topBar.ivBack.setOnClickListener {
            finish()
        }
        binding.topBar.tvToolbarTitle.text = "Notifications"


        binding.rvStore.apply {
            layoutManager = manager
            adapter = mAdapter
        }

        initScrollListener()
        loadNotificaitons(1)


    }

    private fun loadNotificaitons(i: Int) {
        binding.loadingProgressbar.visibility = View.GONE
        if (i == 1) {
            binding.looader.loadingPanel.visibility = View.VISIBLE
        } else {
            binding.loadingProgressbar.visibility = View.VISIBLE
        }

        val productListCall = ApiProvider.dataApi.getNotifications(10, i)
        productListCall.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {
                binding.looader.loadingPanel.visibility = View.GONE
                binding.loadingProgressbar.visibility = View.GONE

                val model: NotificationResponse? = response.body()
                if (model?.meta != null && !model.data?.results.isNullOrEmpty()) {
                    totalPage = model.meta!!.total!!
                    currentPage = model.meta!!.current_page!!
                    postList = ArrayList(mAdapter.getList())
                    response.body()?.data?.results?.let { postList.addAll(it) }
                    mAdapter.submitList(postList)
                }


            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                binding.looader.loadingPanel.visibility = View.GONE
                binding.loadingProgressbar.visibility = View.GONE
                applicationContext?.let {
                    HelperClass.showErrorMsg(
                        "Error : ${t.localizedMessage}",
                        it
                    )
                }
            }

        })

    }


    private fun initScrollListener() {
        binding.rvStore.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
            applicationContext?.let { HelperClass.showWarningMsg("You Are At The Last Page", it) }

        } else {
            loadNotificaitons(currentPage + 1)
        }

    }

    override fun onItemSelected(position: Int, item: NotificationOrderItem) {
        val intent = Intent(applicationContext, OrderDetailPopup::class.java)
        intent.putExtra("ORDER_ID", item.id)
        startActivity(intent)
    }

}