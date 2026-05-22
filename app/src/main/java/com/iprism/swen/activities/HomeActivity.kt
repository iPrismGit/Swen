package com.iprism.swen.activities

import ViewPagerAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.iprism.swen.InAppUpdate
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityHomeBinding
import com.iprism.swen.fragments.LocationBottomSheet
import com.iprism.swen.fragments.MenuDialogFragment
import com.iprism.swen.models.addresslist.AddressItem
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.viewmodels.LocationViewModel
import com.iprism.swen.viewmodels.SearchTextViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val VOICE_REQUEST_CODE = 1001
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var searchTextViewModel: SearchTextViewModel
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val REQUEST_LOCATION_PERMISSION = 1
    var lat = ""
    var lon = ""
    var tag = ""
    private var address: AddressItem? = null
    private var isSubscribe: Boolean = false

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InAppUpdate.initUpdate(this)
        binding.addressTxt.isSelected = true
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
        handleBottomNav()
        handleMenuImg()
        handleNotificationsImg()
        handleCartImg()
        handleLocationLl()
        handleWalletImg()
        handleMicSearchImg()
        handleSearchLL()
        searchTextViewModel = ViewModelProvider(this)[SearchTextViewModel::class.java]
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        if (intent.hasExtra("address")) {
            address = intent.getSerializableExtra("address") as AddressItem
            locationViewModel.setLocation(address!!.lat.toDouble(), address!!.lon.toDouble())
            binding.addressTxt.text = address!!.address
            lat = address!!.lat
            lon = address!!.lon
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            // Request location permission if not granted

            // Request location permission if not granted
            if (applicationContext != null) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                } else {
                    islocation()
                    // Permission already granted, get current location
                    getLastLocation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        InAppUpdate.initResume(this)
    }

    private fun handleBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            binding.searchEt.setText("")
            /*if (!isSubscribe) {
                val intent = Intent(this, SubscriptionActivity::class.java)
                intent.putExtra("tag", "subscribe")
                startActivity(intent)
                Toast.makeText(this, "Please subscribe to use this feature", Toast.LENGTH_SHORT)
                    .show()
                false   // prevent navigation
            } else {
                when (item.itemId) {
                    R.id.home -> binding.viewPager.setCurrentItem(0, false)
                    R.id.hospitals -> binding.viewPager.setCurrentItem(1, false)
                    R.id.medicines -> binding.viewPager.setCurrentItem(2, false)
                    R.id.lab_tests -> binding.viewPager.setCurrentItem(3, false)
                    R.id.diagnostic -> binding.viewPager.setCurrentItem(4, false)
                }
                true
            }*/
            when (item.itemId) {
                R.id.home -> binding.viewPager.setCurrentItem(0, false)
                R.id.hospitals -> binding.viewPager.setCurrentItem(1, false)
                R.id.medicines -> binding.viewPager.setCurrentItem(2, false)
                R.id.lab_tests -> binding.viewPager.setCurrentItem(3, false)
                R.id.diagnostic -> binding.viewPager.setCurrentItem(4, false)
            }
            true
        }
    }


    private fun handleMenuImg() {
        binding.menuImg.setOnClickListener(View.OnClickListener {
            val menuDialogFragment = MenuDialogFragment.newInstance(isSubscribe)
            menuDialogFragment.show(supportFragmentManager, "")
        })
    }

    private fun handleNotificationsImg() {
        binding.notificationsImg.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        })
    }

    private fun handleSearchLL() {
        binding.searchLl.setOnClickListener(View.OnClickListener {
            setSearch("")
        })
    }

    private fun handleWalletImg() {
        binding.walletImg.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        })
    }

    private fun handleLocationLl() {
        binding.locationLl.setOnClickListener(View.OnClickListener {
            //startActivity(Intent(this, AddressActivity::class.java))
            val menuDialogFragment = LocationBottomSheet("home")
            menuDialogFragment.show(supportFragmentManager, "location_dialog")
        })
    }

    private fun handleCartImg() {
        binding.cartImg.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ProductsCartActivity::class.java))
        })
    }

    private fun handleMicSearchImg() {
        binding.micSearchImg.setOnClickListener(View.OnClickListener {
            checkAudioPermissionAndStart()
        })
    }

    fun changeFragment(position: Int) {
        binding.viewPager.setCurrentItem(position, false)
        binding.bottomNav.menu.getItem(position).isChecked = true
    }

    private fun checkAudioPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                200
            )
        } else {
            startVoiceInput()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            200 -> { // Microphone permission
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startVoiceInput()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.permission_denied_to_use_microphone),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            LOCATION_PERMISSION_REQUEST_CODE -> { // Location permission
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                    val menuDialogFragment = LocationBottomSheet("locationMain")
                    menuDialogFragment.show(supportFragmentManager, "location_dialog")
                }
            }
        }
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search")
        try {
            startActivityForResult(intent, VOICE_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                getString(R.string.voice_search_not_supported_on_device),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        InAppUpdate.initResult(this, requestCode, resultCode)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = result?.get(0) ?: ""
            setSearch(spokenText.trim())
        }
    }

    private fun setSearch(search: String) {
        /*if (!isSubscribe) {
            val intent = Intent(this, SubscriptionActivity::class.java)
            intent.putExtra("tag", "subscribe")
            startActivity(intent)
            Toast.makeText(this, "Please subscribe to use this feature", Toast.LENGTH_SHORT)
                .show()
        } else {
            val currentItem = binding.viewPager.currentItem
            if (currentItem == 0) {
                val intent = Intent(this, HospitalsSearchActivity::class.java)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                intent.putExtra("search", search)
                startActivity(intent)
            } else if (currentItem == 1) {
                val intent = Intent(this, HospitalsSearchActivity::class.java)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                intent.putExtra("search", search)
                startActivity(intent)
            } else if (currentItem == 2) {
                *//*searchTextViewModel.setSearchData(search, "medicine", lat, lon)*//*
                val intent = Intent(this, PharmacySearchActivity::class.java)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                intent.putExtra("search", search)
                startActivity(intent)
            } else if (currentItem == 3) {
                val intent = Intent(this, LabTestsSearchActivity::class.java)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                intent.putExtra("search", search)
                startActivity(intent)
            } else if (currentItem == 4) {
                val intent = Intent(this, DiagnosticSearchActivity::class.java)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                intent.putExtra("search", search)
                startActivity(intent)
            }
        }*/
        val currentItem = binding.viewPager.currentItem
        if (currentItem == 0) {
            val intent = Intent(this, HospitalsSearchActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lon", lon)
            intent.putExtra("search", search)
            startActivity(intent)
        } else if (currentItem == 1) {
            val intent = Intent(this, HospitalsSearchActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lon", lon)
            intent.putExtra("search", search)
            startActivity(intent)
        } else if (currentItem == 2) {
            //*searchTextViewModel.setSearchData(search, "medicine", lat, lon)*//*
            val intent = Intent(this, PharmacySearchActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lon", lon)
            intent.putExtra("search", search)
            startActivity(intent)
        } else if (currentItem == 3) {
            val intent = Intent(this, LabTestsSearchActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lon", lon)
            intent.putExtra("search", search)
            startActivity(intent)
        } else if (currentItem == 4) {
            val intent = Intent(this, DiagnosticSearchActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lon", lon)
            intent.putExtra("search", search)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(
                this,
                OnSuccessListener<Location?> { location ->
                    /*if (location != null) {
                        lat = location.latitude.toString()
                        lon = location.longitude.toString()
                        getCityStateFromCoordinates(lat.toDouble(), lon.toDouble(), this)
                        Log.d("locationlatlon", "$lat $lon")
                    } else {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
                        islocation()
                    }*/
                    requestLocationUpdates()
                })
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkpermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            /* if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                        LoginActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_LOCATION_PERMISSION
                );
            }*/
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(
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


    private fun islocation() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 2000
        locationRequest.smallestDisplacement = 2000f
        locationRequest.fastestInterval = 2000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())
            .addOnCompleteListener { task: Task<LocationSettingsResponse?> ->
                try {
                    task.getResult(ApiException::class.java)
                    Log.d("location Turned on", "yes")
                } catch (exception: ApiException) {
                    Log.d("location Turned on", "no")
                    if (exception.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            val resolvable = exception as ResolvableApiException
                            val intentSenderRequest: IntentSenderRequest =
                                IntentSenderRequest.Builder(resolvable.resolution).build()
                            resolutionLauncher.launch(intentSenderRequest)
                        } catch (e: java.lang.ClassCastException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
            .setMinUpdateIntervalMillis(3000L)
            .build()
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                if (location != null) {
                    lat = location.latitude.toString()
                    lon = location.longitude.toString()
                    locationViewModel.setLocation(lat.toDouble(), lon.toDouble())
                    getFullAddressFromCoordinates(
                        lat.toDouble(),
                        lon.toDouble(),
                        getUserDetails()[User.LANG].toString()
                    )
                    fusedLocationClient.removeLocationUpdates(this)
                    Log.d("latlon", "Updated Location: $lat, $lon")
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun getFullAddressFromCoordinates(lat: Double, lon: Double, languageCode: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(this@HomeActivity, Locale(languageCode))
            Log.d("languageCode", languageCode)

            try {
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val fullAddress =
                        address.getAddressLine(0) ?: getString(R.string.city_not_found)
                    withContext(Dispatchers.Main) {
                        binding.addressTxt.text = fullAddress
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.addressTxt.text = getString(R.string.city_not_found)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    binding.addressTxt.text = getString(R.string.city_not_found)
                }
            }
        }
    }

    private val resolutionLauncher = registerForActivityResult<IntentSenderRequest, ActivityResult>(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            Log.d("LocationSettings1", "User enabled location services")
        } else {
            Log.d("LocationSettings1", "User denied location services")
            val menuDialogFragment = LocationBottomSheet("location")
            menuDialogFragment.show(supportFragmentManager, "location_dialog")
        }
    }

    fun askPermission() {
        islocation()
    }

    @SuppressLint("SetTextI18n")
    fun fetchNotificationCount(count: Int) {
        if (count == 0) {
            binding.notificationCountCv.visibility = View.GONE
        } else if (count > 10) {
            binding.notificationCountCv.visibility = View.VISIBLE
            binding.notificationCountTxt.text = "10+"
        } else {
            binding.notificationCountCv.visibility = View.VISIBLE
            binding.notificationCountTxt.text = count.toString()
        }
    }

    override fun onBackPressed() {
        val currentItem = binding.viewPager.currentItem
        if (currentItem != 0) {
            changeFragment(0)
        } else {
            super.onBackPressed()
        }
    }

    fun setSubscribe(subStatus: Boolean) {
        isSubscribe = subStatus
    }

    fun isSubscribe(): Boolean {
        return isSubscribe
    }
}