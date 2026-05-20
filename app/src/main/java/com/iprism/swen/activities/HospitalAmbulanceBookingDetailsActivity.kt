package com.iprism.swen.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityHospitalAmbulanceBookingDetailsBinding
import com.iprism.swen.models.hospitalambulancebookings.HistoryItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.DRY
import com.iprism.swen.utils.showToast

class HospitalAmbulanceBookingDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHospitalAmbulanceBookingDetailsBinding
    private var details : HistoryItem ? = null
    private val CALL_PHONE_PERMISSION_CODE = 1
    private var mobile = ""
    private var lat = ""
    private var lon = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalAmbulanceBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("details")) {
            details = intent.getSerializableExtra("details") as HistoryItem
            showBookingDetails(details!!)
        }
        handleBack()
        handleNeedHelp()
        handleCallLl()
        handlePickUpLocationLl()
        handleTracking()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleCallLl() {
        binding.callLl1.setOnClickListener(View.OnClickListener {
            makePhoneCall(mobile)
        })
    }

    private fun handleNeedHelp() {
        binding.needHelpTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        })
    }

    private fun handleTracking() {
        binding.trackAmbulanceTxt.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, TrackingActivity::class.java)
            intent.putExtra("details", details)
            startActivity(intent)
        })
    }

    private fun handlePickUpLocationLl() {
        binding.pickUpLocationLl.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showBookingDetails(details : HistoryItem) {
        mobile =  details.ambulanceDriverMobile
        binding.nameTxt.text  = details.name
        binding.bookingIdTxt.text = details.bookingId
        binding.mobileTxt.text = details.mobile
        binding.hospitalNameTxt.text = details.hospitalName
        binding.locationTxt.text = details.location
        binding.driverNameTxt.text = details.ambulanceDriverName
        binding.locationTxt.text = details.location
        binding.ambulanceRegTxt.text = details.vehicleNumber
        lat = details.pickUpLocation.lat.toString()
        lon = details.pickUpLocation.lon.toString()
        binding.pickupLocationTxt.text = listOf(
            details.pickUpLocation.hno,
            details.pickUpLocation.buildingNo,
            details.pickUpLocation.landmark,
            details.pickUpLocation.address
        ).filter { !it.isNullOrBlank() }
            .joinToString(", ")
        if (details.ambulanceDriverImage.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + details.ambulanceDriverImage)
                .into(binding.driverImg)
        }
        if (details.bookingStatus.equals("completed", true)) {
            binding.trackAmbulanceTxt.setBackgroundColor(Color.parseColor("#DADADA"))
            DRY.updateOrderStatus(
                this,
                "3",
                binding.stepPlaced,
                binding.stepProcessed,
                binding.stepCompleted,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.labelCompleted,
                binding.progressLine,
                binding.progressLine2
            )
        } else {
            DRY.updateOrderStatus(
                this,
                "1", // or dynamic from API
                binding.stepPlaced,
                binding.stepProcessed,
                binding.stepCompleted,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.labelCompleted,
                binding.progressLine,
                binding.progressLine2
            )
        }
        if (details.tripStatus.equals("ontheway", true)) {
            DRY.updateOrderStatus(
                this,
                "2",
                binding.stepPlaced,
                binding.stepProcessed,
                binding.stepCompleted,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.labelCompleted,
                binding.progressLine,
                binding.progressLine2
            )
        }
        if (details.ambulanceDriverName.isEmpty()) {
            binding.callLl.visibility = View.GONE
            binding.driverLl.visibility = View.GONE
            binding.ambulanceDetailsLl.visibility = View.GONE
        } else {
            binding.callLl.visibility = View.VISIBLE
            binding.driverLl.visibility = View.VISIBLE
            binding.ambulanceDetailsLl.visibility = View.VISIBLE
        }
    }

    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_PHONE_PERMISSION_CODE
            )
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_PHONE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(mobile)
            } else {
                showToast(getString(R.string.permissission_denied_make_calls))
            }
        }
    }
}