package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.ecmuser.models.doctorrating.DoctorRatingRequest
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityAppointmentDoctorBookingDetailsBinding
import com.iprism.swen.databinding.RatingBsBinding
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.History
import com.iprism.swen.models.surgeondoctorsinglebookingdetails.SurgeonDoctorSingleBookingDetailsRequest
import com.iprism.swen.repository.SurgeonSymptomsDoctorRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.DRY
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.SurgeonDoctorBookingsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class SurgeonDoctorBookingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentDoctorBookingDetailsBinding
    private lateinit var viewModel: SurgeonDoctorBookingsViewModel
    private var bookingId : String = ""
    private var bookingDetails : History? = null
    private var lat : String = ""
    private var lon : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentDoctorBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("bookingId")) {
            bookingId = intent.getStringExtra("bookingId")!!
        }
        handleBack()
        handleNeedHelp()
        handleViewPrescriptionLL()
        handleViewInMapsTxt()
        initViewModel()
        observeBookingDetails()
        observeRatingResponse()
        val userDetails = getUserDetails()
        val request = SurgeonDoctorSingleBookingDetailsRequest(
            bookingId,
            userDetails[User.ID]!!.toInt(),
            userDetails[User.AUTH_TOKEN].toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchSurgeonDoctorBookingDetails(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleViewInMapsTxt() {
        binding.viewInMapsTxt.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
        })
    }

    private fun handleNeedHelp() {
        binding.needHelpTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        })
    }

    private fun handleViewPrescriptionLL() {
        binding.viewPrescriptionLl.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ViewPrescriptionActivity::class.java)
            intent.putExtra("details", bookingDetails)
            startActivity(intent)
        })
    }

    private fun initViewModel() {
        val repository = SurgeonSymptomsDoctorRepository()
        val factory = ViewModelFactory { SurgeonDoctorBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SurgeonDoctorBookingsViewModel::class.java]
    }

    private fun observeBookingDetails() {
        viewModel.bookingDetails.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.scroollView.visibility = View.VISIBLE
                    showBookingDetails(result.data.response.history)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeRatingResponse() {
        viewModel.ratingResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    showToast("Rating Added Successfully")
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showBookingDetails(bookingDetails : History) {
        this.bookingDetails = bookingDetails
        binding.nameTxt.text  = bookingDetails.name
        binding.specialityTxt.text  = bookingDetails.specialization
        binding.namePateintTxt.text = bookingDetails.patientName
        binding.bookingIdTxt.text = bookingDetails.bookingId
        binding.slotDateTimeTxt.text = bookingDetails.date + ", " + bookingDetails.time
        binding.mobileTxt.text = bookingDetails.mobile
        binding.emailIdTxt.text = bookingDetails.email
        binding.emailIdTxt.text = bookingDetails.email
        binding.dobTxt.text  = bookingDetails.dob
        binding.genderTxt.text  = bookingDetails.gender.replaceFirstChar { it.uppercaseChar() }
        if (bookingDetails.paymentType.equals("online", true)) {
            binding.paymentModeTxt.text = getString(R.string.online)
        }
        binding.itemTotalTxt.text = "₹" + bookingDetails.fee
        binding.totalDiscountTxt.text = "₹" + bookingDetails.couponDiscount
        binding.totalAmountTxt.text = "₹" + bookingDetails.consultationFee
        if (bookingDetails.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + bookingDetails.image)
                .into(binding.doctorImg)
        }
        binding.qualificationTxt.text  = bookingDetails.qualification
        binding.consultationsCountTxt.text  = "${bookingDetails.consultations} ${if (bookingDetails.consultations.toInt() > 1) getString(R.string.consultations) else getString(R.string.consultation)}"
        binding.expTxt.text = "${bookingDetails.exp} ${if (bookingDetails.exp.toInt() > 1) getString(R.string.years) else getString(R.string.year)}"
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
            binding.viewPrescriptionLl.visibility = View.VISIBLE
            if (bookingDetails.ratingId.equals("0", true)) {
                showRating()
            }
        } else {
            binding.viewPrescriptionLl.visibility = View.GONE
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
        if (bookingDetails.consultType.equals("online", true)) {
            binding.consultTypeTxt.text = getString(R.string.online_consultation)
            binding.viewInMapsLl.visibility = View.GONE
        } else {
            binding.consultTypeTxt.text = getString(R.string.hospital_visit)
        }
        binding.hospitalNameTxt.text = bookingDetails.hospitalName
        binding.hospitalAddressTxt.text = bookingDetails.location
        lat = bookingDetails.lat
        lon = bookingDetails.lon
    }

    @SuppressLint("SetTextI18n")
    private fun showRating() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val ratingBsBinding = RatingBsBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog.setContentView(ratingBsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        ratingBsBinding.nameTxt.text  = bookingDetails!!.name
        if (bookingDetails!!.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + bookingDetails!!.image)
                .into(ratingBsBinding.doctorImg)
        }
        ratingBsBinding.qualificationTxt.text  = bookingDetails!!.qualification
        ratingBsBinding.consultationsCountTxt.text  = "${bookingDetails!!.consultations} ${if (bookingDetails!!.consultations.toInt() > 1) getString(R.string.consultations) else getString(R.string.consultation)}"
        ratingBsBinding.expTxt.text = "${bookingDetails!!.exp} ${if (bookingDetails!!.exp.toInt() > 1) getString(R.string.years) else getString(R.string.year)}"
        ratingBsBinding.specialityTxt.text  = bookingDetails!!.specialization
        ratingBsBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        ratingBsBinding.submitBtn.setOnClickListener(View.OnClickListener {
            if (ratingBsBinding.ratingBar2.rating.toInt() == 0) {
                showToast(getString(R.string.please_give_rating))
            } else {
                bottomSheetDialog.cancel()
                insertRating(ratingBsBinding.ratingBar2.rating.toInt())
            }
        })
        bottomSheetDialog.show()
    }

    private fun insertRating(rating: Int) {
        val userDetails = getUserDetails()
        val request = DoctorRatingRequest(
            bookingId,
            userDetails[User.ID]!!.toInt(),
            rating,
            "surgery_doctor_bookings",
            userDetails[User.AUTH_TOKEN].toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.insertDoctorRating(req)
        }
        Log.d("requestLoading", request.toString())
    }
}