package com.lovfreshuser.ui.vendors

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.lovfreshuser.adapters.VendorItemListAdapter
import com.lovfreshuser.databinding.ActivityStoreSelectionPageBinding
import com.lovfreshuser.models.VendorItem
import com.lovfreshuser.networking.ApiProvider
import com.lovfreshuser.utils.HelperClass
import com.lovfreshuser.utils.PermissionUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreSelectionPage : AppCompatActivity(), VendorItemListAdapter.Interaction {
    private lateinit var binding: ActivityStoreSelectionPageBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var mAdapter: VendorItemListAdapter
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreSelectionPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAdapter = VendorItemListAdapter(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                if (lat == 0.0 || lon == 0.0) {
                    lat = lastLocation.latitude
                    lon = lastLocation.longitude
                    Log.d("TAG", "onLocationResult: $lat $lon  ")
                    loadStores(-33.8761815, 151.2463338)

                }

                // placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }

        }

        val isInfo = intent.getBooleanExtra("isInfo", false)
        if (isInfo) {
            binding.autoVendorSearch.visibility = View.GONE
        } else {
            binding.autoVendorSearch.visibility = View.VISIBLE
        }

        binding.rvStore.apply {
            layoutManager = GridLayoutManager(this@StoreSelectionPage, 2)
            adapter = mAdapter
        }

        binding.topBar.tvToolbarTitle.setText("Vendor List")

        binding.topBar.ivBack.setOnClickListener {
            finish()
        }


    }

    private fun loadStores(lat: Double, lon: Double) {
        val productListCall =
            ApiProvider.dataApi.searchVendor(lat.toString(), lon.toString())
        productListCall.enqueue(object : Callback<List<VendorItem>> {
            override fun onResponse(
                call: Call<List<VendorItem>>,
                response: Response<List<VendorItem>>
            ) {
                val model: List<VendorItem>? = response.body()

                if (model != null) {
                    mAdapter.submitList(model)
                }


            }

            override fun onFailure(call: Call<List<VendorItem>>, t: Throwable) {

                applicationContext?.let {
                    HelperClass.showErrorMsg(
                        "Error : ${t.localizedMessage}",
                        it
                    )
                }
            }

        })

    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState && this::locationCallback.isInitialized) {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun getPermission() {

        Dexter.withContext(this@StoreSelectionPage)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            createLocationRequest()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    // Remember to invoke this method when the custom rationale is closed
                    // or just by default if you don't want to use any custom rationale.
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Toast.makeText(applicationContext, "Something Went Wrong", Toast.LENGTH_LONG).show()
            }

            .check()

    }

    override fun onStart() {
        super.onStart()

        if (PermissionUtils.isAccessFineLocationGranted(applicationContext)) {
            createLocationRequest()
        } else {
            getPermission()
        }


    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    // startIntentSenderForResult(e.resolution.intentSender, REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null);
                    e.startResolutionForResult(
                        this@StoreSelectionPage,
                        REQUEST_CHECK_SETTINGS
                    )


                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }

        }

    }

    override fun onItemSelected(position: Int, item: VendorItem) {
        val intent = Intent(applicationContext, VendorDetails::class.java)
        intent.putExtra("MODEL", item.id.toString())
        startActivity(intent)
    }


}