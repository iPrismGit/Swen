package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityAppointmentDoctorSummaryBinding
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.models.onlinedoctorscoupons.CouponsItem
import com.iprism.swen.models.surgeondoctorbooking.SurgeonDoctorBookingRequest
import com.iprism.swen.models.surgeondoctorbookingdetails.Response
import com.iprism.swen.models.surgeondoctorbookingdetails.SurgeonDoctorBookingDetailsRequest
import com.iprism.swen.models.surgeonsymptomdoctors.DoctorsItem
import com.iprism.swen.repository.SurgeonSymptomsDoctorRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.setEnabledState
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.SurgeonDoctorSummaryViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import kotlin.text.toInt
import kotlin.toString

class SurgeonDoctorSummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentDoctorSummaryBinding
    private var doctor: DoctorsItem? = null
    private var date = ""
    private var convertDate = ""
    private var specialityId = ""
    private var hospitalId = ""
    private var time: TimesItem? = null
    private var familyMembersItem: FamilyMembersItem? = null
    private var coupon: CouponsItem? = null
    private lateinit var viewModel: SurgeonDoctorSummaryViewModel
    private lateinit var response: Response
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppointmentDoctorSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra("doctor")) {
            doctor = intent.getSerializableExtra("doctor") as DoctorsItem?
            time = intent.getSerializableExtra("time") as TimesItem?
            familyMembersItem = intent.getSerializableExtra("familyMember") as FamilyMembersItem?
            date = intent.getStringExtra("date")!!
            convertDate = intent.getStringExtra("convertDate")!!
            specialityId = intent.getStringExtra("specialityId")!!
            hospitalId = intent.getStringExtra("hospitalId")!!
            showDoctorDetails()
        }
        handleBack()
        handleCouponsLl()
        handlePayNowBtn()
        initViewModel()
        observeResponse()
        observeBookingResponse()
        val userDetails = getUserDetails()
        val request = SurgeonDoctorBookingDetailsRequest(
            specialityId.toInt(),
            date,
            doctor!!.id,
            0,
            0,
            userDetails[User.ID]!!.toInt(),
            doctor!!.fee,
            "view",
            userDetails[User.AUTH_TOKEN].toString(),
            hospitalId.toInt(),
            familyMembersItem!!.id
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchSurgeonDoctorBookingDetails(req)
        }
        Log.d("request", request.toString())
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    coupon = data!!.getSerializableExtra("coupon") as CouponsItem
                    binding.couponTxt.text = coupon!!.name
                    val couponRequest = SurgeonDoctorBookingDetailsRequest(
                        specialityId.toInt(),
                        date,
                        doctor!!.id,
                        coupon!!.percentage,
                        coupon!!.id,
                        userDetails[User.ID]!!.toInt(),
                        doctor!!.fee,
                        "view",
                        userDetails[User.AUTH_TOKEN].toString(),
                        hospitalId.toInt(),
                        familyMembersItem!!.id
                    )
                    NetworkRetryHelper.checkAndCallWithRetry(this, couponRequest) { req ->
                        viewModel.fetchSurgeonDoctorBookingDetails(req)
                    }
                    Log.d("request", couponRequest.toString())
                }
            }
    }

    private fun handlePayNowBtn() {
        binding.payNowBtn.setOnClickListener { view ->
            bookAppointmentDoctor()
        }
    }

    private fun handleCouponsLl() {
        binding.couponsLl.setOnClickListener { view ->
            val intent = Intent(this, OffersActivity::class.java)
            intent.putExtra("tag", "doctor")
            launcher.launch(intent)
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDoctorDetails() {
        binding.nameTxt.text = doctor!!.name
        binding.specialityTxt.text = doctor!!.specialization
        binding.doctorPriceTxt.text = "₹" + doctor!!.fee
        if (doctor!!.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + doctor!!.image)
                .into(binding.doctorImg)
        }
        binding.studyTxt.text = doctor!!.qualification
        binding.ratingBar.rating = doctor!!.rating.toFloat()
        binding.consultationsCountTxt.text = "${doctor!!.consultations} ${
            if (doctor!!.consultations.toInt() > 1) getString(R.string.consultations) else getString(
                R.string.consultation
            )
        }"
        binding.expTxt.text = "${doctor!!.exp} ${
            if (doctor!!.exp.toInt() > 1) getString(R.string.years) else getString(R.string.year)
        }"
        binding.dateTimeTxt.text = "$convertDate | ${time!!.time}"
        binding.patientNameTxt.text = familyMembersItem!!.name
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    response = result.data.response
                    binding.patientMobileTxt.text = result.data.response.mobile
                    binding.couponDiscountTxt.text = "₹" + result.data.response.couponDiscount
                    binding.feeTxt.text = "₹" + doctor!!.fee
                    binding.priceTxt.text = "₹" + result.data.response.consultationFee
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
                    binding.payNowBtn.setEnabledState(false)
                }

                is UiState.Success -> {
                    binding.payNowBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    val intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", "Surgeon Doctor Booking")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.payNowBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    showToast(result.message)
                }
            }
        }
    }

    private fun initViewModel() {
        val repository = SurgeonSymptomsDoctorRepository()
        val factory = ViewModelFactory { SurgeonDoctorSummaryViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SurgeonDoctorSummaryViewModel::class.java]
    }

    private fun bookAppointmentDoctor() {
        val userDetails = getUserDetails()
        if (coupon != null) {
            val request = SurgeonDoctorBookingRequest(
                specialityId.toInt(),
                date,
                "12345",
                response.consultationFee,
                response.couponPercentage,
                doctor!!.fee,
                response.mobile,
                hospitalId.toInt(),
                doctor!!.id,
                "online",
                response.couponId,
                userDetails[User.ID]!!.toInt(),
                time!!.id,
                familyMembersItem!!.name,
                "offline",
                time!!.time,
                userDetails[User.AUTH_TOKEN].toString(),
                response.couponDiscount,
                familyMembersItem!!.id,
                response.freeBookingStatus
            )
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.bookSurgeonDoctor(req)
            }
            Log.d("requestLoading", request.toString())
        } else {
            val request = SurgeonDoctorBookingRequest(
                specialityId.toInt(),
                date,
                "12345",
                response.consultationFee,
                0,
                doctor!!.fee,
                response.mobile,
                hospitalId.toInt(),
                doctor!!.id,
                "online",
                0,
                userDetails[User.ID]!!.toInt(),
                time!!.id,
                familyMembersItem!!.name,
                "offline",
                time!!.time,
                userDetails[User.AUTH_TOKEN].toString(),
                "0",
                familyMembersItem!!.id,
                response.freeBookingStatus
            )
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.bookSurgeonDoctor(req)
            }
            Log.d("requestLoading", request.toString())
        }
    }
}