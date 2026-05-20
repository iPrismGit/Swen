package com.iprism.swen.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityAddressBinding
import com.iprism.swen.models.address.AddAddressRequest
import com.iprism.swen.models.addresslist.AddressItem
import com.iprism.swen.repository.AddressRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.setEnabledState
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.AddAddressViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class AddressActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddressBinding
    private lateinit var viewModel: AddAddressViewModel
    private var tag = ""
    private var lat = ""
    private var lon = ""
    private var pincode = ""
    private var state = ""
    private var address = ""
    private var cityName = ""
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val REQUEST_LOCATION_PERMISSION = 1

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
        }
        if (!tag.equals("register")) {
            binding.crossImg.visibility = View.VISIBLE
        }
        checkpermission()
        Handler().postDelayed({
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            // Request location permission if not granted

            // Request location permission if not granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            } else {
                // Permission already granted, get current location
                //getLastLocation()
            }
        }, 2000)
        handleConfirmBtn()
        handleCrossImg()
        initViewModel()
        observeResponse()
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                lat = data!!.getStringExtra("lat")!!
                lon = data.getStringExtra("lon")!!
                address = data.getStringExtra("address")!!
                state = data.getStringExtra("state")!!
                pincode = data.getStringExtra("pincode")!!
                cityName = data.getStringExtra("cityName")!!
                binding.addressTxt.text = address
            }
        }
        handleAddressLl()
        handleBack()
    }

    private fun handleBack() {
        binding.crossImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleConfirmBtn() {
        binding.confirmBtn.setOnClickListener(View.OnClickListener {
            if (getAddress().isEmpty()) {
                showToast(getString(R.string.please_select_address))
            } else if (getAddressType().isEmpty()) {
                showToast(getString(R.string.please_select_address_type))
            } else if (!getAddressType().matches(Regex("^[a-zA-Z].*"))) {
                showToast(getString(R.string.please_enter_valid_address_type))
            } else if (getHno().isEmpty()) {
                showToast(getString(R.string.please_enter_hno))
            } else {
                address()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun handleAddressLl() {
        binding.addressLl.setOnClickListener(View.OnClickListener {
            islocation()
        })
    }

    private fun handleCrossImg() {
        binding.crossImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun address() {
        val userDetails = getUserDetails()
        val request = AddAddressRequest(pincode, getAddress(), getAddressType(), userDetails[User.ID]!!.toInt(), getHno(), lon, getBuilding(), state, userDetails[User.AUTH_TOKEN].toString(), getLandMark(), lat, cityName)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.addAddress(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun getAddress() : String {
        return binding.addressTxt.text.toString().trim()
    }

    private fun getAddressType() : String {
        return binding.addressTypeTxt.text.toString().trim()
    }

    private fun getHno() : String {
        return binding.hnoTxt.text.toString().trim()
    }

    private fun getBuilding() : String {
        return binding.buildingTxt.text.toString().trim()
    }

    private fun getLandMark() : String {
        return binding.landmarkTxt.text.toString().trim()
    }

    private fun initViewModel() {
        val repository = AddressRepository()
        val factory = ViewModelFactory { AddAddressViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AddAddressViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.confirmBtn.setEnabledState(false)
                }

                is UiState.Success -> {
                    binding.confirmBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    if (tag.equals("register", true)) {
                        /*startActivity(Intent(this, HomeActivity::class.java))*/
                        startActivity(Intent(this, SubscriptionActivity::class.java))
                        val user = User(this)
                        user.saveAddress()
                    } else if (tag.equals("home", true)) {
                        val addressItem = AddressItem("", 0, address, "", "", lon, "", 0, 0, "", 0, 0, "", "", lat, 0)
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("address", addressItem)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        val resultIntent = Intent()
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }

                is UiState.Error -> {
                    binding.confirmBtn.setEnabledState(true)
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get current location
                //getLastLocation()
            } else {
                // Permission denied
                Toast.makeText(this, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            /* if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                        LoginActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_LOCATION_PERMISSION
                );
            }*/
            ActivityCompat.requestPermissions(this, arrayOf<String>(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
                REQUEST_LOCATION_PERMISSION
            )
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this@RegisterActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }*/
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun islocation() {
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.interval = 2000
        mLocationRequest.smallestDisplacement = 2000f
        mLocationRequest.fastestInterval = 2000
        mLocationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val task = LocationServices.getSettingsClient(this@AddressActivity)
            .checkLocationSettings(builder.build())
        task.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
                Log.d("location Turned on", "yes")
                val intent = Intent(this, LocationActivity::class.java)
                launcher.launch(intent)
            } catch (exception: ApiException) {
                Log.d("location Turned on", "no")
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                this@AddressActivity,
                                101
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }
}