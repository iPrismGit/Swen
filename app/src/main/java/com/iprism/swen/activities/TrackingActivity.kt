package com.iprism.swen.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.iprism.swen.databinding.ActivityTrackingBinding
import com.iprism.swen.models.ambulancetracking.AmbulanceTrackingRequest
import com.iprism.swen.models.hospitalambulancebookings.HistoryItem
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.AmbulanceTrackingViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import com.iprism.swen.R

class TrackingActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityTrackingBinding
    private lateinit var viewModel: AmbulanceTrackingViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private var details: HistoryItem? = null

    private lateinit var sourceLatLng: LatLng
    private lateinit var destinationLatLng: LatLng

    private var deliveryMarker: Marker? = null
    private var userMarker: Marker? = null
    private var routePolyline: Polyline? = null
    private var arrivalCircle: Circle? = null

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateRunnable: Runnable
    private var isFirstTimeZoom = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("details")) {
            details = intent.getSerializableExtra("details") as HistoryItem
            showBookingDetails(details!!)
        }

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initViewModel()
        observeResponse()
        handleBack()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        userMarker = mMap.addMarker(
            MarkerOptions()
                .position(destinationLatLng)
                .title("You")
                .icon(getResizedBitmapDescriptor(R.drawable.profile_img, 80, 80))
        )

        sourceLatLng = destinationLatLng
        deliveryMarker = mMap.addMarker(
            MarkerOptions()
                .position(sourceLatLng)
                .title("Ambulance")
                .icon(getResizedBitmapDescriptor(R.drawable.ambulance_img, 80, 80))
        )

        updateCameraBounds(sourceLatLng, destinationLatLng)
        startDeliveryLocationUpdates()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { finish() }
    }

    private fun startDeliveryLocationUpdates() {
        updateRunnable = object : Runnable {
            override fun run() {
                fetchLatestAmbulanceLocation()
                handler.postDelayed(this, 5000)
            }
        }
        handler.post(updateRunnable)
    }

    private fun fetchLatestAmbulanceLocation() {
        val request = AmbulanceTrackingRequest(details!!.bookingId)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.trackAmbulance(req)
        }
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val newLatLng = LatLng(result.data.response.lat, result.data.response.lon)
                    sourceLatLng = newLatLng

                    animateMarkerTo(deliveryMarker, newLatLng)

                    // Only zoom once on first location update
                    if (isFirstTimeZoom) {
                        updateCameraBounds(newLatLng, destinationLatLng)
                        isFirstTimeZoom = false
                    }

                    drawPathPolylineFromGoogle(newLatLng, destinationLatLng)

                    getDrivingDistanceText(newLatLng, destinationLatLng, "AIzaSyAaf0xB28iKcaFh6Ex4bRFSaQzZtbu8GOg") { distance ->
                        binding.kmsTxt.text = "Distance: $distance"
                    }

                    // Show circle when within 50 meters
                    /*if (distance < 10) {
                        *//*binding.arrivalMsgLayout.visibility = View.VISIBLE
                        binding.arrivalMsg.text = "Ambulance has arrived nearby."*//*

                        if (arrivalCircle == null) {
                            arrivalCircle = mMap.addCircle(
                                CircleOptions()
                                    .center(sourceLatLng)
                                    .radius(10.0)
                                    .strokeColor(Color.parseColor("#4A90E2"))
                                    .fillColor(Color.parseColor("#304A90E2"))
                                    .strokeWidth(4f)
                            )
                        } else {
                            arrivalCircle?.center = sourceLatLng
                        }
                    } else {
                       *//* binding.arrivalMsgLayout.visibility = View.GONE*//*
                        arrivalCircle?.remove()
                        arrivalCircle = null
                    }*/
                }
                is UiState.Error -> showToast(result.message)
            }
        }
    }

    private fun updateCameraBounds(start: LatLng, end: LatLng) {
        try {
            val boundsBuilder = LatLngBounds.builder()
            boundsBuilder.include(start)
            boundsBuilder.include(end)
            val bounds = boundsBuilder.build()
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (width * 0.35).toInt()
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding))
        } catch (e: Exception) {
            Log.e("CameraBounds", "Error updating bounds: ${e.message}")
        }
    }

    private fun drawPathPolylineFromGoogle(current: LatLng, destination: LatLng) {
        val apiKey = "AIzaSyAaf0xB28iKcaFh6Ex4bRFSaQzZtbu8GOg"
        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=${current.latitude},${current.longitude}&destination=${destination.latitude},${destination.longitude}&sensor=false&mode=driving&key=$apiKey"

        thread {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val steps = decodePolylineFromResponse(response)
                runOnUiThread {
                    if (steps.isNotEmpty()) {
                        routePolyline?.remove()
                        routePolyline = mMap.addPolyline(
                            PolylineOptions()
                                .addAll(steps)
                                .color(Color.BLUE)
                                .width(10f)
                                .geodesic(true)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun decodePolylineFromResponse(response: String): List<LatLng> {
        val path = mutableListOf<LatLng>()
        try {
            val json = JSONObject(response)
            val routes = json.getJSONArray("routes")
            if (routes.length() > 0 && routes.getJSONObject(0).has("overview_polyline")) {
                val overviewPolyline =
                    routes.getJSONObject(0).getJSONObject("overview_polyline").getString("points")
                path.addAll(decodePolyline(overviewPolyline))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return path
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLat = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
            lat += dLat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLng = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
            lng += dLng

            poly.add(LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5))
        }
        return poly
    }

    private fun animateMarkerTo(marker: Marker?, finalPosition: LatLng) {
        val startLatLng = marker?.position ?: return
        val handler = Handler(Looper.getMainLooper())
        val start = System.currentTimeMillis()
        val duration: Long = 1000
        val interpolator = android.view.animation.LinearInterpolator()

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = System.currentTimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val lat = (finalPosition.latitude - startLatLng.latitude) * t + startLatLng.latitude
                val lng = (finalPosition.longitude - startLatLng.longitude) * t + startLatLng.longitude
                marker.position = LatLng(lat, lng)

                if (t < 1.0) handler.postDelayed(this, 16)
            }
        })
    }

    private fun getResizedBitmapDescriptor(resId: Int, width: Int, height: Int): BitmapDescriptor {
        val original = BitmapFactory.decodeResource(resources, resId)
        val resized = Bitmap.createScaledBitmap(original, width, height, false)
        return BitmapDescriptorFactory.fromBitmap(resized)
    }

    private fun showBookingDetails(details: HistoryItem) {
        binding.driverNameTxt.text = details.ambulanceDriverName
        if (details.ambulanceDriverImage.isNotEmpty()) {
            Glide.with(this).load(Constants.IMAGES_BASE_URL + details.ambulanceDriverImage).into(binding.driverImg)
        }
        destinationLatLng = LatLng(details.pickUpLocation.lat, details.pickUpLocation.lon)
    }

    private fun getDrivingDistanceText(
        origin: LatLng,
        destination: LatLng,
        apiKey: String,
        callback: (String) -> Unit
    ) {
        val url = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${destination.latitude},${destination.longitude}" +
                "&mode=driving&key=$apiKey"

        thread {
            try {
                val conn = URL(url).openConnection() as HttpURLConnection
                conn.connect()
                val result = conn.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(result)

                val routes = json.getJSONArray("routes")
                if (routes.length() > 0) {
                    val legs = routes.getJSONObject(0).getJSONArray("legs")
                    if (legs.length() > 0) {
                        val distance = legs.getJSONObject(0).getJSONObject("distance").getString("text")

                        Handler(Looper.getMainLooper()).post {
                            callback(distance)
                        }
                        return@thread
                    }
                }

                Handler(Looper.getMainLooper()).post {
                    callback("–")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback("–")
                }
            }
        }
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { AmbulanceTrackingViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AmbulanceTrackingViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
    }
}
