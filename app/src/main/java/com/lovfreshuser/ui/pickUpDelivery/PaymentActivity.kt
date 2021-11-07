package com.lovfreshuser.ui.pickUpDelivery


import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lovfreshuser.utils.HelperClass
import com.lovfreshuser.R
import com.lovfreshuser.adapters.DateAdapter
import com.lovfreshuser.adapters.TimeAdapter
import com.lovfreshuser.adapters.paymentCartAdapter
import com.lovfreshuser.database.OfflineDatabase
import com.lovfreshuser.database.models.CartLocalDbModel
import com.lovfreshuser.databinding.ActivityPaymentBinding
import com.lovfreshuser.models.DateModel
import com.lovfreshuser.models.ListOfDate
import com.lovfreshuser.models.TimeModel
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.utils.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PaymentActivity : AppCompatActivity(), paymentCartAdapter.Interaction,
    DateAdapter.Interaction, TimeAdapter.Interaction {
    private val TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000

    private val TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
    private var isDelete = false
    private val DIVIDER_MODULO = 5 // means divider position is every 5th symbol beginning with 1

    private val DIVIDER_POSITION =
        DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0

    private val DIVIDER = '-'
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var mAdapter: paymentCartAdapter
    private lateinit var dateDialog: Dialog
    private lateinit var timeDialog: Dialog
    private var cartList: List<CartLocalDbModel> = emptyList()
    private var placeDate: String = ""
    private var placeTime: String = ""
    private var start_t: String = ""
    private var end_t: String = ""

    private val cardNumber: String = ""
    private var cvc = ""
    private var exp_month = ""
    private var exp_year = ""
    private var order_type: String = ""
    private var delivery_charges: Double = 0.0
    private var totalAmountOfCart: Double = 0.0
    private var complAddress: String = ""
    private var mobileNo: String = ""
    private var address_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dateDialog = Dialog(this@PaymentActivity)
        dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        timeDialog = Dialog(this@PaymentActivity)
        timeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mAdapter = paymentCartAdapter(this)

        binding.rvOrder.apply {
            layoutManager = LinearLayoutManager(this@PaymentActivity)
            adapter = mAdapter
        }
        loadCartList()

        binding.rbPromoCode.setOnClickListener(View.OnClickListener { view ->
            val checked = (view as CheckBox).isChecked
            if (checked) {
                setBottomSheet(binding.rbPromoCode)
            }
        })

        binding.tvDate.setOnClickListener {

            val model: ListOfDate? = SharedPrefManager.get("slots")
            model?.listOfSlot?.let { it1 -> selectDateDialog(it1) }
        }
        binding.tvTime.setOnClickListener {
            val model: ListOfDate? = SharedPrefManager.get("slots")
            model?.listOfSlot?.let { it1 -> selectDateDialog(it1) }
        }
        cardCardEditFormat()
    }

    private fun cardCardEditFormat() {

        binding.edCardNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // noop
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // noop
            }

            override fun afterTextChanged(s: Editable) {
                if (!isInputCorrect(
                        s,
                        TOTAL_SYMBOLS,
                        DIVIDER_MODULO,
                        DIVIDER
                    )
                ) {
                    s.replace(
                        0,
                        s.length,
                        buildCorrectString(
                            getDigitArray(s, TOTAL_DIGITS),
                            DIVIDER_POSITION,
                            DIVIDER
                        )
                    )
                }
            }

            private fun isInputCorrect(
                s: Editable,
                totalSymbols: Int,
                dividerModulo: Int,
                divider: Char
            ): Boolean {
                var isCorrect = s.length <= totalSymbols // check size of entered string
                for (i in 0 until s.length) { // check that every element is right
                    isCorrect = if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect and (divider == s[i])
                    } else {
                        isCorrect and Character.isDigit(s[i])
                    }
                }
                return isCorrect
            }

            private fun buildCorrectString(
                digits: CharArray,
                dividerPosition: Int,
                divider: Char
            ): String {
                val formatted = StringBuilder()
                for (i in digits.indices) {
                  try {
                      if (digits[i].toChar().digitToInt() != 0) {
                          formatted.append(digits[i])
                          if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                              formatted.append(divider)
                          }
                      }
                  }catch (Ex : java.lang.Exception){

                  }
                }
                return formatted.toString()
            }

            private fun getDigitArray(s: Editable, size: Int): CharArray {
                val digits = CharArray(size)
                var index = 0
                var i = 0
                while (i < s.length && index < size) {
                    val current = s[i]
                    if (Character.isDigit(current)) {
                        digits[index] = current
                        index++
                    }
                    i++
                }
                return digits
            }
        })
        binding.edExperyDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (i1 == 0) isDelete = false else isDelete = true
            }

            override fun afterTextChanged(s: Editable) {
                val source = s.toString()
                val length = source.length
                val stringBuilder = StringBuilder()
                stringBuilder.append(source)
                Log.e("esp_date", source)
                try {
                    val items1 = source.split("/").toTypedArray()
                    val d1 = items1[0]
                    val d2 = items1[1]
                    Log.e("month", "$d1 $d2")
                    exp_month = d1
                    exp_year = d2
                } catch (e: Exception) {
                    Log.e("errorMags", e.message!!)
                }
                if (length > 0 && length == 3) {
                    if (isDelete) stringBuilder.deleteCharAt(length - 1) else stringBuilder.insert(
                        length - 1,
                        "/"
                    )
                    binding.edExperyDate.setText(stringBuilder.toString())
                    binding.edExperyDate.text?.let { binding.edExperyDate.setSelection(it.length) }
                    Log.e("test$s", "afterTextChanged: append $length")
                } else {
                }
            }
        })
    }

    private fun loadCartList() {
        val database = OfflineDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            val list = database.cartDao().getAll()
            cartList = list
            totalAmountOfCart = HelperClass.getTotalOfCart(cartList)
            withContext(Dispatchers.Main) {
                mAdapter.submitList(list)
                setTotal()
            }
        }

    }

    override fun onItemSelected(position: Int, item: CartLocalDbModel) {

    }

    private fun selectDateDialog(
        dateModels: List<DateModel>
    ) {
        dateDialog.setCancelable(false)
        dateDialog.setContentView(R.layout.dialog_date)
        val recyclerView: RecyclerView = dateDialog.findViewById(R.id.rv_date)
        val mAdapter = DateAdapter(this)
        mAdapter.submitList(dateModels)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = mAdapter

        val btnCountinue = dateDialog.findViewById<Button>(R.id.btn_continue)
        btnCountinue.setOnClickListener {
            val placedate: String = placeDate
            if (TextUtils.isEmpty(placedate)) {
                HelperClass.showErrorMsg(getString(R.string.select_date), applicationContext)
            } else {
                binding.tvTime.text = HelperClass.convertDateFormat(placedate)

//                selectTimeDialog(
//                    SessionManager(applicationContext).GetsSaveDateModel() as ArrayList<TimeModel?>,
//                    itemSelectTimeInterface
//                )

                dateDialog.dismiss()
            }
        }


        dateDialog.show()
    }


    private fun selectTimeDialog(
        timeModels: List<TimeModel>
    ) {
        timeDialog.setCancelable(false)
        timeDialog.setContentView(R.layout.dialog_time)
        val recyclerView: RecyclerView = timeDialog.findViewById(R.id.rv_time)
        val adapter = TimeAdapter(this)
        adapter.submitList(timeModels)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val btnCountinue = timeDialog.findViewById<Button>(R.id.btn_continue)
        btnCountinue.setOnClickListener {

//            if (TextUtils.isEmpty(placetime)) {
//                BaseUtility.toastMsg(applicationContext, getString(R.string.select_time))
//            } else {
//                tvTime.setText(BaseUtility.parseDateToddMMyyyy(placetime))
//                alertDialog.dismiss()
//            }
        }
        timeDialog.show()
    }


    override fun onDateItemSelected(position: Int, item: DateModel) {
        dateDialog.dismiss()
        binding.tvDate.text = HelperClass.convertDateFormat(item.date)
        //HelperClass.showErrorMsg("${item.time.size}" , applicationContext)
        selectTimeDialog(item.time)
    }

    private fun setTotal() {
        order_type = intent.getStringExtra("ORDER_TYPE").toString()
        delivery_charges = intent.getDoubleExtra("DELIVERY_COST", 0.0)

        val totalAmount = totalAmountOfCart

        binding.tvDistanceFeeAmount.setText("$" + HelperClass.convertAmountToString(delivery_charges))
        binding.tvTotalAmount.text =
            "$" + HelperClass.convertAmountToString(totalAmount + delivery_charges)


    }

    private fun loadDateTime() {
        val productListCall =
            ApiProvider.dataApi.getTimeSlots(HelperClass.getSelectedVendorID(), "6")
        productListCall.enqueue(object : Callback<List<DateModel>> {
            override fun onResponse(
                call: Call<List<DateModel>>,
                response: Response<List<DateModel>>
            ) {

                val model: List<DateModel>? = response.body()

                if (!model.isNullOrEmpty()) {
                    val listOfDate = ListOfDate(model)
                    SharedPrefManager.put(listOfDate, "slots")
                }


            }

            override fun onFailure(call: Call<List<DateModel>>, t: Throwable) {


            }

        })
    }

    override fun onStart() {
        super.onStart()
        loadDateTime()
    }

    override fun onTimeItemSelected(position: Int, item: TimeModel) {
        timeDialog.dismiss()
        binding.tvTime.text = "${HelperClass.parseDateToddMMyyyy(item.start_time)}"
        placeTime = item.strt_time_end_time
        start_t = item.start_time
        end_t = item.end_time
    }

    private fun setBottomSheet(rbPromoCode: CheckBox) {
        val bottomSheetDialog = BottomSheetDialog(this)
        if (rbPromoCode.isChecked) {
            bottomSheetDialog.setContentView(R.layout.content_promo_code)
            val promocode: LinearLayout? =
                bottomSheetDialog.findViewById<LinearLayout>(R.id.ll_promo_code)
            val rvCoupans: RecyclerView? =
                bottomSheetDialog.findViewById<RecyclerView>(R.id.rv_coupans)

//            promoCodeAdapter = PromoCodeAdapter(this, this)
//            rvCoupans.layoutManager = LinearLayoutManager(this)
//            rvCoupans.adapter = promoCodeAdapter
//            getCoupanApi()
            promocode?.setOnClickListener {
                rbPromoCode.isChecked = false
                bottomSheetDialog.dismiss()
            }
            val edPromocode = bottomSheetDialog.findViewById<EditText>(R.id.ed_promo_code)
            val tvApply: TextView? = bottomSheetDialog.findViewById<TextView>(R.id.tv_apply)
            tvApply?.setOnClickListener { // edPromocode.setError(null);
                val promo_code = edPromocode?.getText().toString()
//                if (!TextUtils.isEmpty(promo_code)) {
//                    postPromoCodeApi(promo_code)
//                    bottomSheetDialog.dismiss()
//                } else {
//                    BaseUtility.toastMsg(this@PaymentActivity, "Promo Code is required")
//                }
            }
            bottomSheetDialog.show()
            // bottomSheetDialog.setCancelable(false);
        } else {
            rbPromoCode.isChecked = false
            bottomSheetDialog.dismiss()
        }
    }


}