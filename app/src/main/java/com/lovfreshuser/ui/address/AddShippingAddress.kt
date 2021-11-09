package com.lovfreshuser.ui.address

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.JsonObject
import com.lovfreshuser.R
import com.lovfreshuser.databinding.ActivityAddShippingAddressBinding
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.utils.HelperClass
import com.lovfreshuser.utils.HelperClass.Companion.createAddressJson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddShippingAddress : AppCompatActivity() {
    private lateinit var binding: ActivityAddShippingAddressBinding
    var address_value = 1
    var latitude: Double? = 0.0
    var longitude: Double? = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddShippingAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Places.
        Places.initialize(applicationContext, getString(R.string.places_key))

        binding.btnSubmit.setOnClickListener {

        }
        binding.toolbar1.ivBack.setOnClickListener {
            finish()
        }
        binding.rgAddressType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_home -> {
                    address_value = 1
                }
                R.id.rb_work -> {
                    address_value = 2
                }
                R.id.rb_other -> {
                    address_value = 3

                }
            }
        }

        binding.edStreetAdd.setOnClickListener {
            // Specify the fields to return.
            val placeFields = Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.ID,
                Place.Field.LAT_LNG,
                Place.Field.NAME
            )
// Start the autocomplete intent.
            // Start the autocomplete intent.
            val intent1 =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields)
                    .build(this)
            startActivityForResult(
                intent1,
                100
            )
        }


        binding.btnSubmit.setOnClickListener {
            val name = binding.edFullName.text.toString()
            val street = binding.edStreetAdd.text.toString()
            val flat = binding.edFlatNo.text.toString()
            val city = binding.edCity.text.toString()
            val state = binding.edState.text.toString()
            val zip = binding.edZipCode.text.toString()
            val phone_no = binding.edPhone.text.toString()

            if (TextUtils.isEmpty(name)) {
                binding.edFullName.setError(getString(R.string.error_field_required))
            } else if (TextUtils.isEmpty(street)) {
                binding.edStreetAdd.setError(getString(R.string.error_field_required))
            } else if (TextUtils.isEmpty(state)) {
                binding.edState.setError(getString(R.string.error_field_required))
            } else if (TextUtils.isEmpty(city)) {
                binding.edCity.setError(getString(R.string.error_field_required))
            } else if (TextUtils.isEmpty(zip)) {
                binding.edZipCode.setError(getString(R.string.error_field_required))
            } else if (TextUtils.isEmpty(phone_no)) {
                binding.edPhone.setError(getString(R.string.error_field_required))
            } else {
                val address: String = street + " " + city + " " + state + " " + zip


                val model = createAddressJson(
                    name,
                    address,
                    phone_no,
                    flat,
                    "",
                    street,
                    address_value.toString(),
                    (latitude).toString(),
                    (longitude).toString()
                )

                createAddressApi(model)

            }


        }


    }

    private fun createAddressApi(model: JSONObject) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = model.toString().toRequestBody(mediaType)

        val productListCall = ApiProvider.dataApi.createAddress(requestBody)
        productListCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                if (response.isSuccessful && response.code() == 201) {
                    HelperClass.showSuccessMsg("Address Created", applicationContext)
                    finish()
                } else {
                    HelperClass.showErrorMsg("Error : ${response.code()}", applicationContext)
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                binding.looader.loadingPanel.visibility = View.GONE
//                binding.loadingProgressbar.visibility = View.GONE
                applicationContext?.let {
                    HelperClass.showErrorMsg(
                        "Error : ${t.localizedMessage}",
                        it
                    )
                }
            }

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                assert(data != null)
                val place = Autocomplete.getPlaceFromIntent(data!!)

                latitude = Objects.requireNonNull(place.latLng)?.latitude
                longitude = Objects.requireNonNull(place.latLng)?.longitude
                binding.edStreetAdd.setText(place.address)

                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    val addresses = latitude?.let {
                        longitude?.let { it1 ->
                            geocoder.getFromLocation(
                                it,
                                it1, 1
                            )
                        }
                    }
                    if (addresses?.size!! > 0) {
                        val stateName = addresses[0].adminArea
                        val cityName = addresses[0].locality
                        val postalCode = addresses[0].postalCode
                        if (stateName != null) {
                            binding.edState.setText(stateName)
                        }
                        if (cityName != null) {
                            binding.edCity.setText(cityName)
                        }
                        if (postalCode != null) {
                            binding.edZipCode.setText(postalCode)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                assert(data != null)
                val status = Autocomplete.getStatusFromIntent(
                    data!!
                )

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}