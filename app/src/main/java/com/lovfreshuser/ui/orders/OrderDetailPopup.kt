package com.lovfreshuser.ui.orders

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.lovfreshuser.R
import com.lovfreshuser.adapters.OrderDetailListAdapter
import com.lovfreshuser.databinding.ActivityOrderDetailPopupBinding
import com.lovfreshuser.models.*
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.ui.vendors.VendorDetails
import com.lovfreshuser.utils.HelperClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OrderDetailPopup : AppCompatActivity(), OrderDetailListAdapter.Interaction {
    private lateinit var binding: ActivityOrderDetailPopupBinding
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    var vendorID = ""

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    private lateinit var orderAdapter: OrderDetailListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setFinishOnTouchOutside(false)
        orderAdapter = OrderDetailListAdapter(this@OrderDetailPopup)
        val orderID = intent.getIntExtra("ORDER_ID", 0)


        binding.rvOrder.apply {
            layoutManager = LinearLayoutManager(this@OrderDetailPopup)
            adapter = orderAdapter
        }

        downladData(orderID)

        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.btnCustCall.setOnClickListener {
            val intent = Intent(applicationContext, VendorDetails::class.java)
            intent.putExtra("MODEL", vendorID)
            startActivity(intent)
        }

    }

    private fun downladData(orderID: Int) {

        binding.loader.loadingPanel.visibility = View.VISIBLE
        val productListCall = ApiProvider.dataApi.getOrderDetails(orderID)
        productListCall.enqueue(object : Callback<OrderHistoryItem> {
            override fun onResponse(
                call: Call<OrderHistoryItem>,
                response: Response<OrderHistoryItem>
            ) {
                binding.loader.loadingPanel.visibility = View.GONE
                val model: OrderHistoryItem? = response.body()
                setDetails(model)

            }

            override fun onFailure(call: Call<OrderHistoryItem>, t: Throwable) {
                binding.loader.loadingPanel.visibility = View.GONE
                applicationContext?.let {
                    HelperClass.showErrorMsg(
                        "Error : ${t.localizedMessage}",
                        it
                    )
                }
            }

        })


    }

    private fun setDetails(orderModel: OrderHistoryItem?) {
        vendorID = orderModel?.vendor?.id.toString()

        if (orderModel != null) {
            val order_no = orderModel.orderNumber
            val vendor_id = orderModel.vendor?.id
            binding.tvOrderId.text = orderModel.orderNumber
            binding.tvOrderType.text = orderModel.orderType
            binding.tvPickupSlot.setText(
                HelperClass.convertDateFormat(orderModel.deliverDate)
                    .toString() + "-" + HelperClass.parseDateToddMMyyyy(orderModel.startTime) + "-" + HelperClass.parseDateToddMMyyyy(
                    orderModel.endTime
                )
            )
            binding.tvOrderStatus.setText(orderModel.status)
            binding.tvOrderName.setText(
                orderModel.user?.first_name.toString() + " " +
                        "${orderModel.user?.last_name}"

            )
            binding.tvOrderPhone.text = orderModel.user?.phone_number
            binding.tvOrderEmail.text = orderModel.user?.email

            //tvOrderDate.setText(BaseUtility.convertDateFormat(orderModel.getDeliverDate()));

//                    String order_date = orderModel.getCreatedAt();
//                    String splitDate = order_date.substring(0, 10);
//                    tvOrderDates.setText(splitDate);

            //tvOrderDate.setText(BaseUtility.convertDateFormat(orderModel.getDeliverDate()));

//                    String order_date = orderModel.getCreatedAt();
//                    String splitDate = order_date.substring(0, 10);
//                    tvOrderDates.setText(splitDate);
            binding.tvOrderDateTime.text =
                HelperClass.toDefaultFormattedDateStr(orderModel.createdAt)

            try {
                val delivery_charges =
                    String.format(Locale.ENGLISH, "%.2f", orderModel.deliveryCost?.toFloat())
                binding.tvDistanceFeeAmount.text = "$$delivery_charges"
            } catch (exception: Exception) {
                binding.tvDistanceFeeAmount.text = "$0"
            }


            //tvOrderTotal.setText("$" + orderModel.getTotal());


            //tvOrderTotal.setText("$" + orderModel.getTotal());
            binding.tvTotal.text = "$" + orderModel.total

            binding.tvDeliverDate.text = "Shipping Date : " + HelperClass.toDefaultFormattedDateStr(
                orderModel.deliverDate
            )
            binding.tvDeliverTime.text = "Pickup Time : " + orderModel.startTime
            if (orderModel.orderType?.contains("delivery") == true) {
                binding.tvDeliverTime.text =
                    getString(R.string.delivery_time) + " : " + orderModel.startTime
            }


            binding.tvShopName.text = orderModel.vendor?.title
            binding.tvShopAddress.text = orderModel.vendor?.address?.address

            if (orderModel.orderType.equals("pickup")) {
                if (orderModel.status.equals("created")) {
                    binding.tvOrderTitle.text = "Your order is waiting for shop confirmation"
                } else if (orderModel.status.equals("accepted")) {
                    binding.tvOrderTitle.text = "Your order is accepted"
                } else if (orderModel.status.equals("packed")) {
                    binding.tvOrderTitle.text = "Your order is ready for pickup."
                } else if (orderModel.status.equals("rejected")) {
                    binding.tvOrderTitle.text = "Your order is canceled"
                    binding.tvRejectReasonHeader.visibility = View.VISIBLE
                    binding.tvRejectReason.visibility = View.VISIBLE
                    binding.tvRejectReason.text = "${orderModel.rejectionReason}"
                } else {
                    binding.tvOrderTitle.text = "Your order is confirmed pickup"
                    binding.btnDownInvoice.visibility = View.VISIBLE
                    binding.btnCustCall.visibility = View.VISIBLE
                }
            } else {
                if (orderModel.status.equals("created")) {
                    binding.tvOrderTitle.text = "Your order is waiting for shop confirmation"
                } else if (orderModel.status.equals("accepted")) {
                    binding.tvOrderTitle.text = "Your order is accepted"
                } else if (orderModel.status.equals("packed")) {
                    binding.tvOrderTitle.text = "Your order is ready for deliver"
                } else if (orderModel.status.equals("rejected")) {
                    binding.tvOrderTitle.text = "Your order is canceled"
                    binding.tvRejectReasonHeader.visibility = View.VISIBLE
                    binding.tvRejectReason.visibility = View.VISIBLE
                    binding.tvRejectReason.text = "${orderModel.rejectionReason}"
                } else {
                    binding.tvOrderTitle.text = "Your order is confirmed deliver"
                    binding.btnDownInvoice.visibility = View.VISIBLE
                    binding.btnCustCall.visibility = View.VISIBLE
                }
            }

            var invoicePdf = ""

            val transactionModelArrayList: List<Transaction> = orderModel.transaction
            if (transactionModelArrayList != null && !transactionModelArrayList.isEmpty()) {
                invoicePdf = transactionModelArrayList[0].invoice.toString()
            }

            val addressModels: Address? = orderModel.address
            if (addressModels != null) {
                val address: String = addressModels.address.toString()
                binding.tvAddress.setText(address)
                if (address == null) {
                    binding.tvAddress.setVisibility(View.GONE)
                }
            } else {
            }


            val productModels = orderModel.orderProducts
            if (productModels != null && !productModels.isEmpty()) {
                orderAdapter.submitList(productModels)
            } else {
            }
        } else {
            HelperClass.showErrorMsg("asd", applicationContext)
        }
    }

    override fun onItemSelected(position: Int, item: OrderProduct) {

    }


}