package com.lovfreshuser.ui.product

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.lovfreshuser.utils.HelperClass
import com.lovfreshuser.database.OfflineDatabase
import com.lovfreshuser.database.models.CartLocalDbModel
import com.lovfreshuser.databinding.ActivityProductDetailsBinding
import com.lovfreshuser.models.ProductDetailsModel
import com.lovfreshuser.ui.CartPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetails : AppCompatActivity() {
    var unit_id = ""
    var unit_title = ""
    var unit_value = ""
    var qty = 1
    var productID = 0
    var existingVolume = 0
    var price: Double = 0.0
    var isAvailableInCart = false
    private lateinit var model: ProductDetailsModel
    private lateinit var localDb: OfflineDatabase
    private lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        localDb = OfflineDatabase(applicationContext)
        model = intent.getSerializableExtra("model") as ProductDetailsModel
        productID = model.id
        setDetails(model)
        setClickListeners()

        binding.bar.ivBack.setOnClickListener {
            finish()
        }
        binding.tvProductTitle.text = "Product Details"

        binding.bar.rlCartview.setOnClickListener {
            startActivity(Intent(applicationContext, CartPage::class.java))
        }

    }

    private fun setClickListeners() {
        binding.ivAdd.setOnClickListener {
            qty += 1
            binding.edQuantity.text = "$qty"
        }

        binding.ivMinus.setOnClickListener {
            if (qty != 1) {
                qty -= 1
                binding.edQuantity.text = "$qty"
            } else {
                HelperClass.showWarningMsg(
                    "Error : Quantity can't be smaller than 1 ",
                    applicationContext
                )
            }
        }

        binding.btnAddToBag.setOnClickListener {
            // add to the stock
            // create a model
            checkStock()

        }
    }

    private fun checkStock() {
        if (qty > existingVolume) {
            HelperClass.showErrorMsg(
                getString(com.lovfreshuser.R.string.out_of_stock),
                applicationContext
            )
        } else {
            val cartModel = createACartmodel(model)
            if (isAvailableInCart) {
                // update the stock
                updateModel(cartModel)
            } else {
                // insert in the database
                insertModel(cartModel)


            }
        }
    }

    private fun cartCount() {
        binding.bar.tvCartCount.visibility = View.VISIBLE


        CoroutineScope(Dispatchers.IO).launch {
            val item = HelperClass.getCountOfCarts(localDb)
            withContext(Dispatchers.Main) {
                binding.bar.tvCartCount.text = "${item}"

            }
        }

    }

    private fun insertModel(cartModel: CartLocalDbModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val item = localDb.cartDao().insertItem(cartModel)
            withContext(Dispatchers.Main) {
                cartCount()
                isAvailableInCart = true
                HelperClass.showSuccessMsg("Item Added In The Cart", applicationContext)
            }
        }

    }

    private fun updateModel(cartModel: CartLocalDbModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val item = localDb.cartDao().updateItem(cartModel)
            withContext(Dispatchers.Main) {
                HelperClass.showSuccessMsg("Item Updated In The Cart", applicationContext)
            }
        }


    }

    private fun createACartmodel(model: ProductDetailsModel): CartLocalDbModel {
        // cart model
        val cartModel = CartLocalDbModel()
        cartModel.image = if (model.images?.isNotEmpty() == true) {
            model.images[0].image.toString()
        } else ""
        cartModel.item_id = productID.toString()
        cartModel.itemName = model.title.toString()
        cartModel.price = price
        cartModel.quantity = qty
        cartModel.stockQuantity = existingVolume
        cartModel.vendorId = "34"
        cartModel.unit = unit_title
        cartModel.unitId = unit_id
        cartModel.unitValue = unit_value

        return cartModel

    }

    private fun setDetails(model: ProductDetailsModel) {
        binding.tvProductTitle.text = model.title

        if (model.short_description.isNullOrEmpty()) {
            binding.tvDescripation.visibility = View.GONE
            binding.tvDesDetail.visibility = View.GONE
        } else {
            binding.tvDescripation.visibility = View.VISIBLE
            binding.tvDesDetail.visibility = View.VISIBLE
            binding.tvDesDetail.text = "${model.short_description}"

        }

        val unitList = model.product_attributes
        if (!model.promotional_price.isNullOrEmpty()) {
            binding.tvPromoPrice.text = ("$" + model.promotional_price)
            binding.tvCalAmt.text = ("${model.price}")

            binding.tvPromoPrice.visibility = View.VISIBLE
            binding.tvCalAmt.visibility = View.GONE
            price = model.promotional_price.toDouble()

        } else {
            binding.tvPromoPrice.visibility = View.VISIBLE
            binding.tvCalAmt.visibility = View.GONE
            price = model.price.toString().toDouble()
            binding.tvPromoPrice.text = (" " + model.price)
        }

        if (model.type.equals("SCH")) {
            binding.tvSchedule.visibility = View.VISIBLE
            binding.tvStartDate.visibility = View.VISIBLE
            binding.tvEndDate.visibility = View.VISIBLE
            val start_Date: String? = model.start_date_time?.substring(0, 10)
            val end_Date: String? = model.end_date_time?.substring(0, 10)
            binding.tvStartDate.text = "Start Date:-$start_Date"
            binding.tvEndDate.text = "End Date:-$end_Date"


        } else {
            binding.tvSchedule.visibility = View.GONE
            binding.tvStartDate.visibility = View.GONE
            binding.tvEndDate.visibility = View.GONE
        }

        binding.tvUnitNm.text = "/" + model.quantity + unitList?.get(0)?.uom?.name
        if (!unitList.isNullOrEmpty()) {
            if (unitList.size != 1) {
                binding.unitView.visibility = View.VISIBLE
            } else {
                binding.unitView.visibility = View.INVISIBLE
            }
            val aa = ArrayAdapter(this, R.layout.simple_spinner_item, model.product_attributes)
            aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spUnit.adapter = aa

            binding.spUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {

                    unit_id = aa.getItem(position)?.uom?.id.toString()
                    unit_title = aa.getItem(position)?.uom?.name.toString()
                    unit_value = aa.getItem(position)?.uom?.value.toString()
                    //tvProductAmt.setText(adapter.getItem(position).getPrice());
                    // existingVolume = Integer.parseInt(adapter.getItem(position).getStock());
                    val newExistingVolumes =
                        aa.getItem(position)?.stock.toString().toDouble().toInt()
                    existingVolume = newExistingVolumes.toInt()


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        if (!model.images.isNullOrEmpty()) {
            val imageList = ArrayList<SlideUIModel>()
            for (item in model.images) {
                imageList.add(SlideUIModel("${item.image}", ""))
            }
            binding.imageSlide.setImageList(imageList = imageList)
            binding.imageSlide.startSliding()
        }


    }


    private fun checkIfProductExist(id: Int) {

        CoroutineScope(Dispatchers.IO).launch {
            val item = localDb.cartDao().findByFoodId("$id")
            isAvailableInCart = item != null
        }


    }

    override fun onResume() {
        super.onResume()
        checkIfProductExist(productID)
        cartCount()
    }
}