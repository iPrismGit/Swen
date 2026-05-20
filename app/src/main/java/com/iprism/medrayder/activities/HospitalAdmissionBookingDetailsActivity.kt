package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivityHospitalAdmissionBookingDetailsBinding
import com.iprism.medrayder.models.admitbookingdetails.AdmitBookingDetailsRequest
import com.iprism.medrayder.models.admitbookingdetails.Details
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsRequest
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.History
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.utils.DRY
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.HospitalAdmitBookingDetailsViewModel
import com.iprism.medrayder.viewmodels.HospitalDoctorBookingDetailsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class HospitalAdmissionBookingDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHospitalAdmissionBookingDetailsBinding
    private var bookingId = ""
    private lateinit var viewModel: HospitalAdmitBookingDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalAdmissionBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("id")) {
            bookingId = intent.getStringExtra("id")!!
        }
        handleBack()
        handleNeedHelp()
        initViewModel()
        observeBookingDetails()
        fetchBookings()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleNeedHelp() {
        binding.needHelpTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalAdmitBookingDetailsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalAdmitBookingDetailsViewModel::class.java]
    }

    private fun observeBookingDetails() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.scroolView.visibility = View.VISIBLE
                    showBookingDetails(result.data.response.details)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun fetchBookings() {
        val userDetails = getUserDetails()
        val request = AdmitBookingDetailsRequest(bookingId, userDetails[User.ID]!!.toInt(), userDetails[User.AUTH_TOKEN].toString(),  userDetails[User.LANG].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchAdmitBookingDetails(req)
        }
        Log.d("requestLoading", request.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun showBookingDetails(bookingDetails : Details) {
        binding.nameTxt.text  = bookingDetails.familyMembers[0].name
        binding.bookingIdTxt.text = bookingDetails.bookingId
        binding.mobileTxt.text = bookingDetails.familyMembers[0].mobile
        binding.emailIdTxt.text = bookingDetails.familyMembers[0].email
        binding.dobTxt.text  = bookingDetails.familyMembers[0].dob
        binding.genderTxt.text  = bookingDetails.familyMembers[0].gender.replaceFirstChar { it.uppercaseChar() }
        binding.hospitalNameTxt.text = bookingDetails.name
        binding.bookingIdTxt.text = bookingDetails.bookingId
        binding.taglineTxt.text = bookingDetails.tagLine
        binding.locationTxt.text = bookingDetails.location
        binding.slotDateTimeTxt.text = bookingDetails.date + ", " + bookingDetails.time
        if (bookingDetails.logo.isNotEmpty()) {
            binding.prNameTxt.text = bookingDetails.prName
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + bookingDetails.logo)
                .into(binding.hospitalImg)
        }
        if (bookingDetails.prImage.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + bookingDetails.prImage)
                .into(binding.prImg)
        } else {
            binding.prDetailsLl.visibility = View.GONE
        }
        if (bookingDetails.bookingStatus.equals("completed", true)) {
            DRY.updateTwoStepOrderStatus(
                this,
                "2",
                binding.stepPlaced,
                binding.stepProcessed,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.progressLine
            )
        } else {
            DRY.updateTwoStepOrderStatus(
                this,
                "1",
                binding.stepPlaced,
                binding.stepProcessed,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.progressLine
            )
        }
    }
}