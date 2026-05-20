package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityAmbulanceBookingBinding
import com.iprism.swen.models.ambulancebooking.AmbulanceBookingRequest
import com.iprism.swen.models.ambulancebooking.Response
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.AmbulanceBookingViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class AmbulanceBookingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAmbulanceBookingBinding
    private lateinit var viewModel: AmbulanceBookingViewModel
    private var hospitalId : String = ""
    private var responseBooking : Response? = null
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmbulanceBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("hospitalId")) {
            hospitalId = intent.getStringExtra("hospitalId")!!
        }
        handleChangeTxt()
        handleBackImg()
        handleConfirmTxt()
        initViewModel()
        observeResponse()
        observeBookingResponse()
        bookHospitalAmbulance("", "", "", "view")
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                bookHospitalAmbulance("", "", "", "view")
            }
        }
    }

    private fun handleChangeTxt() {
        binding.changeTxt.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            launcher.launch(intent)
        })
    }

    private fun handleBackImg() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleConfirmTxt() {
        binding.confirmBtn.setOnClickListener(View.OnClickListener {
            binding.confirmBtn.isClickable = false
            binding.confirmBtn.isActivated = false
            binding.confirmBtn.isEnabled = false
            bookHospitalAmbulance(responseBooking!!.address.id.toString(), responseBooking!!.userDetails.name, responseBooking!!.userDetails.mobile, "book")
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { AmbulanceBookingViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AmbulanceBookingViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.scrollView.visibility = View.VISIBLE
                    binding.progress.hideProgress()
                    responseBooking = result.data.response
                    binding.patientNameTxt.text = result.data.response.userDetails.name
                    binding.patientMobileTxt.text = result.data.response.userDetails.mobile
                    binding.addressTypeTxt.text = result.data.response.address.addressType
                    binding.addressTxt.text = listOf(
                        result.data.response.address.hno,
                        result.data.response.address.buildingNo,
                        result.data.response.address.landmark,
                        result.data.response.address.address
                    ).filter { !it.isNullOrBlank() }
                        .joinToString(", ")
                    binding.confirmBtn.visibility = View.VISIBLE
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeBookingResponse() {
        viewModel.bookingResponse.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.confirmBtn.isClickable = true
                    binding.confirmBtn.isActivated = true
                    binding.confirmBtn.isEnabled = true
                    val intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", getString(R.string.amb_booked_successfully))
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (result.message.equals("Your subscription has expired. Please renew.", true)) {
                        val intent = Intent(this, SubscriptionActivity::class.java)
                        intent.putExtra("tag", "booking")
                        launcher.launch(intent)
                    } else {
                        showToast(result.message)
                    }
                    binding.confirmBtn.isClickable = true
                    binding.confirmBtn.isActivated = true
                    binding.confirmBtn.isEnabled = true
                }
            }
        }
    }

    private fun bookHospitalAmbulance(addressId : String, name : String, mobile : String, viewType : String) {
        val userDetails = getUserDetails()
        val request = AmbulanceBookingRequest(
            userDetails[User.ID]!!.toInt(),
            addressId,
            name,
            mobile,
            viewType,
            userDetails[User.AUTH_TOKEN].toString(),
            hospitalId.toInt(),
            userDetails[User.LANG].toString()
        )
        if (viewType.equals("view", true)) {
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.viewHospitalAmbulanceBooking(req)
            }
        } else if (viewType.equals("book", true)){
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.bookHospitalAmbulance(req)
            }
        }
        Log.d("requestLoading", request.toString())
    }
}