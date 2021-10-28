package com.lovfreshuser.ui

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.lovfreshuser.databinding.ActivityProductDetailsBinding
import com.lovfreshuser.models.ProductDetailsModel

class ProductDetails : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model: ProductDetailsModel = intent.getSerializableExtra("model") as ProductDetailsModel

        setDetails(model)

        binding.bar.ivBack.setOnClickListener {
            finish()
        }
        binding.tvProductTitle.text = "Product Details"

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

        } else {
            binding.tvPromoPrice.visibility = View.VISIBLE
            binding.tvCalAmt.visibility = View.GONE
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
            var aa = ArrayAdapter(this, R.layout.simple_spinner_item, model.product_attributes)
            aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spUnit.adapter = aa

            binding.spUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {

//                        unit_id = adapter.getItem(position).getUom().getId()
//                        unit_title = adapter.getItem(position).getUom().getName()
//                        unit_value = adapter.getItem(position).getUom().getValue()
//                        //tvProductAmt.setText(adapter.getItem(position).getPrice());
//                        // existingVolume = Integer.parseInt(adapter.getItem(position).getStock());
//                        val existingVolumes: Double =
//                            adapter.getItem(position).getStock().toDouble()
//                        existingVolume = (+existingVolumes).toInt()
//                        println("int  x = " + existingVolumes.toInt())

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
}