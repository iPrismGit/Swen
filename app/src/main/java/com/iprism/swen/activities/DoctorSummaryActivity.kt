package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityDoctorSummaryBinding
import com.iprism.swen.databinding.OnlineOrderTypeBsBinding
import com.iprism.swen.models.checkpackage.CheckPaymentRequest
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.swen.models.onlinedoctorbooking.OnlineDoctorBookingRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.models.onlinedoctors.DoctorsItem
import com.iprism.swen.models.onlinedoctorscoupons.CouponsItem
import com.iprism.swen.repository.CashFreePaymentRepository
import com.iprism.swen.repository.OnlineDoctorRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.PaymentManager
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.setEnabledState
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.CashFreePaymentViewModel
import com.iprism.swen.viewmodels.DoctorSummaryViewModel
import com.iprism.swen.viewmodels.DoctorTimeSlotViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class DoctorSummaryActivity : AppCompatActivity(), PaymentManager.PaymentResultListener {

    private lateinit var binding : ActivityDoctorSummaryBinding
    private var doctor : DoctorsItem? =  null
    private var date = ""
    private var convertDate = ""
    private var specialityId = ""
    private var time : TimesItem? = null
    private var familyMembersItem : FamilyMembersItem? =  null
    private lateinit var viewModel: DoctorTimeSlotViewModel
    private var coupon: CouponsItem? = null
    private lateinit var doctorSummaryViewModel: DoctorSummaryViewModel
    private lateinit var onlineDoctorBookingResponse: com.iprism.swen.models.onlinedoctorbookingdetails.OnlineDoctorBookingResponse
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var cashFreePaymentViewModel: CashFreePaymentViewModel
    private lateinit var paymentManager: PaymentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentManager = PaymentManager(this, this)
        if (intent.hasExtra("doctor")) {
            doctor = intent.getSerializableExtra("doctor") as DoctorsItem?
            time = intent.getSerializableExtra("time") as TimesItem?
            familyMembersItem = intent.getSerializableExtra("familyMember") as FamilyMembersItem?
            date = intent.getStringExtra("date")!!
            convertDate = intent.getStringExtra("convertDate")!!
            specialityId = intent.getStringExtra("specialityId")!!
            showDoctorDetails()
        }
        handleBookFreeBtn()
        handleBack()
        handlePayNowBtn()
        handleSelectBtn()
        initViewModel()
        initDoctorSummaryViewModel()
        initCashFreeViewModel()
        observeResponse()
        observeBookingResponse()
        observeCashFreeResponse()
        observeCheckPaymentResponse()
        val userDetails = getUserDetails()
        val request = OnlineDoctorBookingDetailsRequest(date, doctor!!.id, 0, 0, userDetails[User.ID]!!.toInt(), specialityId.toInt(), doctor!!.fee.toInt(), "view", userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
        viewModel.getOnlineDoctorBookingDetails(request)
        Log.d("request", request.toString())
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                coupon = data!!.getSerializableExtra("coupon") as CouponsItem
                binding.couponTxt.text = coupon!!.name
                val couponRequest = OnlineDoctorBookingDetailsRequest(date, doctor!!.id, coupon!!.percentage, coupon!!.id, userDetails[User.ID]!!.toInt(), specialityId.toInt(), doctor!!.fee.toInt(), "view", userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
                NetworkRetryHelper.checkAndCallWithRetry(this, couponRequest) { req ->
                    viewModel.getOnlineDoctorBookingDetails(req)
                }
                Log.d("request", couponRequest.toString())
            }
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handlePayNowBtn() {
        binding.payNowBtn.setOnClickListener(View.OnClickListener {
            showOrderTypeBs(this)
        })
    }

    private fun handleBookFreeBtn() {
        binding.bookFreeBtn.setOnClickListener(View.OnClickListener {
            val userDetails = getUserDetails()
            binding.bookFreeBtn.setEnabledState(false)
            if (coupon != null) {
                val request = OnlineDoctorBookingRequest(date, "", "0", onlineDoctorBookingResponse.couponPercentage, specialityId, doctor!!.fee, onlineDoctorBookingResponse.mobile, coupon!!.name, doctor!!.id.toString(), "free", onlineDoctorBookingResponse.couponId, userDetails[User.ID]!!, time!!.id.toString(), familyMembersItem!!.name, "online",time!!.time, userDetails[User.AUTH_TOKEN].toString(), onlineDoctorBookingResponse.couponDiscount, familyMembersItem!!.id.toString(), "0", userDetails[User.LANG].toString(), onlineDoctorBookingResponse.freeBookingStatus)
                NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                    doctorSummaryViewModel.bookOnlineDoctor(req)
                }
                Log.d("requestLoading", request.toString())
            } else {
                val request = OnlineDoctorBookingRequest(date, "", "0", "0", specialityId, doctor!!.fee, onlineDoctorBookingResponse.mobile, "", doctor!!.id.toString(), "free", "0", userDetails[User.ID]!!, time!!.id.toString(), familyMembersItem!!.name, "online",time!!.time, userDetails[User.AUTH_TOKEN].toString(), "0", familyMembersItem!!.id.toString(), "0", userDetails[User.LANG].toString(), onlineDoctorBookingResponse.freeBookingStatus)
                NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                    doctorSummaryViewModel.bookOnlineDoctor(req)
                }
                Log.d("requestLoading", request.toString())
            }
        })
    }

    private fun handleSelectBtn() {
        binding.selectBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, OffersActivity::class.java)
            intent.putExtra("tag", "onlineDoctor")
            launcher.launch(intent)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showDoctorDetails() {
        binding.nameTxt.text  = doctor!!.name
        binding.specialityTxt.text  = doctor!!.specialization
        binding.doctorPriceTxt.text  = "₹" + doctor!!.fee
        if (doctor!!.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + doctor!!.image)
                .into(binding.doctorImg)
        }
        binding.studyTxt.text  = doctor!!.qualification
        binding.ratingBar.rating = doctor!!.rating.toFloat()
        binding.consultationsCountTxt.text  = "${doctor!!.consultations} ${if (doctor!!.consultations.toInt() > 1) getString(R.string.consultations) else getString(R.string.consultation)}"
        binding.expTxt.text = "${doctor!!.exp} ${if (doctor!!.exp.toInt() > 1) getString(R.string.years) else getString(R.string.year)}"
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
                    binding.bookFreeBtn.setEnabledState(false)
                }

                is UiState.Success -> {
                    binding.bookFreeBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    onlineDoctorBookingResponse = result.data.response
                    binding.patientMobileTxt.text = result.data.response.mobile
                    binding.couponDiscountTxt.text = "₹" +  result.data.response.couponDiscount
                    binding.feeTxt.text = "₹" +  doctor!!.fee
                    binding.priceTxt.text = "₹" + result.data.response.consultationFee
                    if (result.data.response.freeBookingStatus.equals("yes", true)) {
                        binding.bookFreeBtn.visibility = View.VISIBLE
                        binding.priceLl.visibility = View.GONE
                        binding.payNowBtn.visibility = View.GONE
                        binding.couponsLl.visibility = View.GONE
                    }
                }

                is UiState.Error -> {
                    binding.bookFreeBtn.setEnabledState(true)
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeBookingResponse() {
        doctorSummaryViewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    startActivity(Intent(this, SuccessActivity::class.java))
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (result.message.equals("Your subscription has expired. Please renew.", true)) {
                        val intent = Intent(this, SubscriptionActivity::class.java)
                        intent.putExtra("tag", "booking")
                        startActivity(intent)
                    } else {
                        showToast(result.message)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeCashFreeResponse() {
        cashFreePaymentViewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    paymentManager.startPayment(result.data.response.orderId, result.data.response.paymentSessionId)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeCheckPaymentResponse() {
        cashFreePaymentViewModel.checkPaymentResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.response.orderStatus == "PAID") {
                        showToast("Payment confirmed")
                        val userDetails = getUserDetails()
                        if (coupon != null) {
                            val request = OnlineDoctorBookingRequest(date, result.data.response.orderId, onlineDoctorBookingResponse.consultationFee, onlineDoctorBookingResponse.couponPercentage, specialityId, doctor!!.fee, onlineDoctorBookingResponse.mobile, coupon!!.name, doctor!!.id.toString(), "online", onlineDoctorBookingResponse.couponId, userDetails[User.ID]!!, time!!.id.toString(), familyMembersItem!!.name, "online",time!!.time, userDetails[User.AUTH_TOKEN].toString(), onlineDoctorBookingResponse.couponDiscount, familyMembersItem!!.id.toString(), "0", userDetails[User.LANG].toString(), onlineDoctorBookingResponse.freeBookingStatus)
                            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                                doctorSummaryViewModel.bookOnlineDoctor(req)
                            }
                            Log.d("requestLoading", request.toString())
                        } else {
                            val request = OnlineDoctorBookingRequest(date, result.data.response.orderId, onlineDoctorBookingResponse.consultationFee, "0", specialityId, doctor!!.fee, onlineDoctorBookingResponse.mobile, "", doctor!!.id.toString(), "online", "0", userDetails[User.ID]!!, time!!.id.toString(), familyMembersItem!!.name, "online",time!!.time, userDetails[User.AUTH_TOKEN].toString(), "0", familyMembersItem!!.id.toString(), "0", userDetails[User.LANG].toString(), onlineDoctorBookingResponse.freeBookingStatus)
                            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                                doctorSummaryViewModel.bookOnlineDoctor(req)
                            }
                            Log.d("requestLoading", request.toString())
                        }
                    } else {
                        showToast("Payment failed: ${result.data.response.orderStatus}")
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    showToast("Payment verification failed")
                }
            }
        }
    }

    private fun initViewModel() {
        val repository = OnlineDoctorRepository()
        val factory = ViewModelFactory { DoctorTimeSlotViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DoctorTimeSlotViewModel::class.java]
    }

    private fun initCashFreeViewModel() {
        val repository = CashFreePaymentRepository()
        val factory = ViewModelFactory { CashFreePaymentViewModel(repository) }
        cashFreePaymentViewModel = ViewModelProvider(this, factory)[CashFreePaymentViewModel::class.java]
    }

    private fun initDoctorSummaryViewModel() {
        val repository = OnlineDoctorRepository()
        val factory = ViewModelFactory { DoctorSummaryViewModel(repository) }
        doctorSummaryViewModel = ViewModelProvider(this, factory)[DoctorSummaryViewModel::class.java]
    }

    private fun showOrderTypeBs(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val onlineOrderTypeBsBinding = OnlineOrderTypeBsBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(onlineOrderTypeBsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        onlineOrderTypeBsBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        onlineOrderTypeBsBinding.paymentGatewayLl.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
            createOrder(onlineDoctorBookingResponse.consultationFee)
        })
        onlineOrderTypeBsBinding.walletLl.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
            val userDetails = getUserDetails()
            if (coupon != null) {
                val request = OnlineDoctorBookingRequest(date, "12345", onlineDoctorBookingResponse.consultationFee, onlineDoctorBookingResponse.couponPercentage, specialityId, doctor!!.fee, onlineDoctorBookingResponse.mobile, coupon!!.name, doctor!!.id.toString(), "wallet", onlineDoctorBookingResponse.couponId, userDetails[User.ID]!!, time!!.id.toString(), familyMembersItem!!.name, "online",time!!.time, userDetails[User.AUTH_TOKEN].toString(), onlineDoctorBookingResponse.couponDiscount, familyMembersItem!!.id.toString(),  onlineDoctorBookingResponse.walletAmount, userDetails[User.LANG].toString(), onlineDoctorBookingResponse.freeBookingStatus)
                doctorSummaryViewModel.bookOnlineDoctor(request)
                Log.d("requestLoading", request.toString())
            } else {
                val request = OnlineDoctorBookingRequest(date, "12345", onlineDoctorBookingResponse.consultationFee, "0", specialityId, doctor!!.fee, onlineDoctorBookingResponse.mobile, "", doctor!!.id.toString(), "wallet", "0", userDetails[User.ID]!!, time!!.id.toString(), familyMembersItem!!.name, "online",time!!.time, userDetails[User.AUTH_TOKEN].toString(), "0", familyMembersItem!!.id.toString(),  onlineDoctorBookingResponse.walletAmount, userDetails[User.LANG].toString(), onlineDoctorBookingResponse.freeBookingStatus)
                doctorSummaryViewModel.bookOnlineDoctor(request)
                Log.d("requestLoading", request.toString())
            }
        })
        bottomSheetDialog.show()
    }

    private fun createOrder(amount : String) {
        val userDetails = getUserDetails()
        val request = CreateCashFreeOrderRequest(amount, userDetails[User.ID]!!.toInt())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            cashFreePaymentViewModel.createCashFreeOrder(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun checkPaymentStatus(orderId : String) {
        val request = CheckPaymentRequest(orderId)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            cashFreePaymentViewModel.checkPayment(req)
        }
        Log.d("requestLoading", request.toString())
    }

    override fun onSuccess(orderId: String) {
        checkPaymentStatus(orderId)
    }

    override fun onFailure(error: String) {
        showToast(error)
    }
}