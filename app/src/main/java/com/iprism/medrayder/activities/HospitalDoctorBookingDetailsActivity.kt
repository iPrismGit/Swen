package com.iprism.medrayder.activities

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
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivityHospitalDoctorBookingDetailsBinding
import com.iprism.medrayder.databinding.ActivityOnlineDoctorBookingDetailsBinding
import com.iprism.medrayder.databinding.RatingBsBinding
import com.iprism.medrayder.models.onlinedoctorrating.OnlineDoctorRatingRequest
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.History
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsRequest
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.utils.DRY
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.HospitalDoctorBookingDetailsViewModel
import com.iprism.medrayder.viewmodels.OnlineDoctorBookingDetailsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class HospitalDoctorBookingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalDoctorBookingDetailsBinding
    private lateinit var viewModel: HospitalDoctorBookingDetailsViewModel
    private var bookingId : String = ""
    private var lat : String = ""
    private var lon : String = ""
    private var bookingDetails : History? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalDoctorBookingDetailsBinding.inflate(layoutInflater)
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
        val request = OnlineDoctorSingleBookingDetailsRequest(bookingId, userDetails[User.ID]!!.toInt(), userDetails[User.AUTH_TOKEN].toString(), userDetails[User.LANG].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getHospitalDoctorSingleBookingDetails(req)
        }
        Log.d("requestLoading", request.toString())
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

    private fun handleViewInMapsTxt() {
        binding.viewInMapsTxt.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
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
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalDoctorBookingDetailsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalDoctorBookingDetailsViewModel::class.java]
    }

    private fun observeBookingDetails() {
        viewModel.response.observe(this) { result ->
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
        } else if (bookingDetails.paymentType.equals("free", true)) {
            binding.paymentModeTxt.text = getString(R.string.free)
        } else {
            binding.paymentModeTxt.text = getString(R.string.wallet)
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
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
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
        val request = OnlineDoctorRatingRequest(bookingId, userDetails[User.ID]!!.toInt(), rating, userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.insertHospitalDoctorRating(req)
        }
        Log.d("requestLoading", request.toString())
    }
}