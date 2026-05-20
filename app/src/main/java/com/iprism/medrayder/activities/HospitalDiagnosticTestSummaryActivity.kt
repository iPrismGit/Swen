package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.TestsAdapter
import com.iprism.medrayder.databinding.ActivityHospitalDiagnosticTestSummaryBinding
import com.iprism.medrayder.databinding.ActivityLabTestSummaryBinding
import com.iprism.medrayder.databinding.LabTestBillDetailsBottomSheetBinding
import com.iprism.medrayder.databinding.OnlineOrderTypeBsBinding
import com.iprism.medrayder.databinding.TestsBottomSheetBinding
import com.iprism.medrayder.models.checkpackage.CheckPaymentRequest
import com.iprism.medrayder.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.medrayder.models.diagnosticbooking.DiagnosticBookingRequest
import com.iprism.medrayder.models.diagnostictests.TestsItem
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsRequest
import com.iprism.medrayder.models.diagnostictimings.Price
import com.iprism.medrayder.models.diagnostictimings.Response
import com.iprism.medrayder.models.hospitaldiagnosticbooking.HospitalDiagnosticBookingRequest
import com.iprism.medrayder.models.hospitalmedicinetimeslots.HospitalDiagnosticTimeRequest
import com.iprism.medrayder.models.labtestslots.LabTestSlotsRequest
import com.iprism.medrayder.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.medrayder.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.medrayder.models.onlinedoctorscoupons.CouponsItem
import com.iprism.medrayder.repository.CashFreePaymentRepository
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.HospitalDiagnosticsRepository
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.utils.DRY
import com.iprism.medrayder.utils.PaymentManager
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.CashFreePaymentViewModel
import com.iprism.medrayder.viewmodels.DiagnosticSummaryViewModel
import com.iprism.medrayder.viewmodels.HospitalDiagnosticSummaryViewModel
import com.iprism.medrayder.viewmodels.HospitalDiagnosticTimeSlotViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory
import java.io.ByteArrayOutputStream

class HospitalDiagnosticTestSummaryActivity : AppCompatActivity(), PaymentManager.PaymentResultListener  {

    private lateinit var binding : ActivityHospitalDiagnosticTestSummaryBinding
    private lateinit var viewModel: HospitalDiagnosticSummaryViewModel
    private var details : Response? = null
    private var tag = ""
    private var date = ""
    private var convertDate = ""
    private var id = ""
    private var testId = ""
    private var lat = ""
    private var lon = ""
    private var time : TimesItem? = null
    private var price : Price? = null
    private var selectedFamilyMembers  = ArrayList<FamilyMembersItem>()
    private var subscriptionStatus = ""
    private var imageUri = ""
    private var image = ""
    private var coupon: CouponsItem? = null
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var request : HospitalDiagnosticTimeRequest
    private lateinit var cashFreePaymentViewModel: CashFreePaymentViewModel
    private lateinit var paymentManager: PaymentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalDiagnosticTestSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentManager = PaymentManager(this, this)
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
            date = intent.getStringExtra("date")!!
            convertDate = intent.getStringExtra("convertDate")!!
            id = intent.getStringExtra("id")!!
            testId = intent.getStringExtra("testId")!!
            selectedFamilyMembers = intent.getSerializableExtra("selectedFamilyMembers") as ArrayList<FamilyMembersItem>
            time = intent.getSerializableExtra("time") as TimesItem?
            price = intent.getSerializableExtra("price") as Price?
            if (intent.hasExtra("imageUri")) {
                imageUri = intent.getStringExtra("imageUri")!!
                image = convertUriToBase64(this, stringToUri(imageUri))!!
            }
        }
        handleBack()
        handlePayNowBtn()
        handleBillDetailsTxt()
        handleLocationLl()
        handleSelectBtn()
        initViewModel()
        initCashFreeViewModel()
        observeResponse()
        observeBookingResponse()
        observeCashFreeResponse()
        observeCheckPaymentResponse()
        handleIncludeTestTxt()
        val userDetails = getUserDetails()
        val selectedIds = selectedFamilyMembers.map { it.id.toString() }
        request = HospitalDiagnosticTimeRequest(date, "", userDetails[User.ID]!!.toInt(), price!!.discontPrice, selectedFamilyMembers.size, "view", userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), id.toInt(), testId.toInt(), selectedIds.toString().replace("[", "").replace("]", "").replace(" ", ""), "0", "0")
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getDetails(req)
        }
        Log.d("request", request.toString())
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data!!.hasExtra("tag")) {
                    NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                        viewModel.getDetails(req)
                    }
                    Log.d("request", request.toString())
                } else {
                    coupon = data.getSerializableExtra("coupon") as CouponsItem
                    binding.couponTxt.text = coupon!!.name
                    request = HospitalDiagnosticTimeRequest(date, "", userDetails[User.ID]!!.toInt(), price!!.discontPrice, selectedFamilyMembers.size, "view", userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), id.toInt(), testId.toInt(), selectedIds.toString().replace("[", "").replace("]", "").replace(" ", ""), coupon!!.id.toString(), coupon!!.percentage.toString())
                    NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                        viewModel.getDetails(req)
                    }
                    Log.d("request", request.toString())
                }
            }
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleLocationLl() {
        binding.locationLl.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
        })
    }

    private fun handleIncludeTestTxt() {
        binding.numberOfTestsTxt.setOnClickListener(View.OnClickListener {
            showTestsSheet(details!!.mainData.tests)
        })
    }

    private fun handlePayNowBtn() {
        binding.payNowBtn.setOnClickListener(View.OnClickListener {
            if (subscriptionStatus.equals("no", true)) {
                val intent = Intent(this, SubscriptionActivity::class.java)
                intent.putExtra("tag", "booking")
                launcher.launch(intent)
            } else {
                showOrderTypeBs(this)
            }
        })
    }

    private fun handleBillDetailsTxt() {
        binding.viewDetailsTxt.setOnClickListener(View.OnClickListener {
            showBillDetailsBottomSheet(this)
        })
    }

    private fun handleSelectBtn() {
        binding.selectBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, OffersActivity::class.java)
            intent.putExtra("tag", "hospitalDiagnostic")
            launcher.launch(intent)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showDetails(details : Response) {
        subscriptionStatus = details.subscriptionStatus
        binding.nameTxt.text  = details.mainData.name
        if (details.mainData.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + details.mainData.image)
                .into(binding.testImg)
        }
        binding.personCountTxt.text = "${price!!.patientCount} ${if (price!!.patientCount == 1) getString(R.string.patient) else getString(R.string.patients)}"
        binding.numberOfTestsTxt.text = getString(R.string.includes) + " ${details.mainData.tests.size} ${if (details.mainData.tests.size == 1) getString(R.string.test) else getString(R.string.tests)}"
        binding.previousPriceTxt.text = "₹${details.mainData.onePerson}"
        binding.reportsTxt.text = getString(R.string.reports_in) + " ${details.mainData.reportIn} ${getString(R.string.hrs)}"
        if (details.mainData.fasting.equals("yes", true)) {
            binding.fastingTxt.text = getString(R.string.fasting_required)
        } else {
            binding.fastingTxt.text = getString(R.string.fasting_not_required)
        }
        binding.numberOfTestsTxt.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.previousPriceTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        binding.patientNameTxt.text = details.name
        binding.patientMobileTxt.text = details.mobile
        binding.priceTxt.text = "₹${details.consultationFee}"
        binding.testPriceTxt.text = "₹${details.mainData.onePersonDiscount}"
        binding.slotDateTimeTxt.text = "$convertDate | ${time!!.time}"
        binding.locationLl.visibility = View.VISIBLE
        binding.locationTxt.text = details.mainData.location
        lat = details.mainData.lat
        lon = details.mainData.lon
    }

    private fun initViewModel() {
        val repository = HospitalDiagnosticsRepository()
        val factory = ViewModelFactory { HospitalDiagnosticSummaryViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalDiagnosticSummaryViewModel::class.java]
    }

    private fun initCashFreeViewModel() {
        val repository = CashFreePaymentRepository()
        val factory = ViewModelFactory { CashFreePaymentViewModel(repository) }
        cashFreePaymentViewModel = ViewModelProvider(this, factory)[CashFreePaymentViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.scrollView.visibility = View.VISIBLE
                    binding.divider15.visibility = View.VISIBLE
                    binding.payNowLl.visibility = View.VISIBLE
                    details = result.data.response
                    showDetails(details!!)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeBookingResponse() {
        viewModel.bookingResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    startActivity(Intent(this, SuccessActivity::class.java))
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
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
                        bookDiagnostic("online", result.data.response.orderId)
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

    @SuppressLint("SetTextI18n")
    private fun showBillDetailsBottomSheet(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val labTestSelectMembersBottomSheetBinding = LabTestBillDetailsBottomSheetBinding.inflate(
            LayoutInflater.from(context))
        bottomSheetDialog.setContentView(labTestSelectMembersBottomSheetBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        labTestSelectMembersBottomSheetBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        labTestSelectMembersBottomSheetBinding.coupontDiscountTxt.text = "₹${details!!.couponDiscount}"
        labTestSelectMembersBottomSheetBinding.flatDiscountLl.visibility = View.GONE
        labTestSelectMembersBottomSheetBinding.couponDiscountLl.visibility = View.VISIBLE
        labTestSelectMembersBottomSheetBinding.totalTxt.text = "₹${details!!.consultationFee}"
        labTestSelectMembersBottomSheetBinding.itemTotalTxt.text = "₹${price!!.discontPrice}"
        bottomSheetDialog.show()
    }

    private fun bookDiagnostic(paymentType : String, transactionId : String) {
        val userDetails = getUserDetails()
        val request : HospitalDiagnosticBookingRequest
        val selectedIds = selectedFamilyMembers.map { it.id.toString() }
        if (coupon != null) {
            request = HospitalDiagnosticBookingRequest(date, "12345", image, details!!.consultationFee, details!!.couponPercentage, price!!.discontPrice, selectedFamilyMembers.size, details!!.mobile, id.toInt(), coupon!!.name, paymentType, details!!.couponId.toInt(), userDetails[User.ID]!!.toInt(), time!!.id, details!!.name, time!!.time, userDetails[User.AUTH_TOKEN].toString(), details!!.couponDiscount, testId.toInt(), selectedIds.toString().replace("[", "").replace("]", "").replace(" ", ""), details!!.walletAmount, userDetails[User.LANG].toString())
        } else {
            request = HospitalDiagnosticBookingRequest(date, "12345", image, details!!.consultationFee, "0", price!!.discontPrice, selectedFamilyMembers.size, details!!.mobile, id.toInt(), "", paymentType, 0, userDetails[User.ID]!!.toInt(), time!!.id, details!!.name, time!!.time, userDetails[User.AUTH_TOKEN].toString(), details!!.couponDiscount, testId.toInt(), selectedIds.toString().replace("[", "").replace("]", "").replace(" ", ""), details!!.walletAmount, userDetails[User.LANG].toString())
        }
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.bookDiagnostic(req)
        }
        Log.d("requestLoading", request.toString())
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
            createOrder(details!!.consultationFee)
            bottomSheetDialog.cancel()
        })
        onlineOrderTypeBsBinding.walletLl.setOnClickListener(View.OnClickListener {
            bookDiagnostic("wallet", "")
            bottomSheetDialog.cancel()
        })
        bottomSheetDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showTestsSheet(tests : List<TestsItem>) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val testsBottomSheetBinding = TestsBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog.setContentView(testsBottomSheetBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        testsBottomSheetBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        testsBottomSheetBinding.numberOfTestsTxt.text = getString(R.string.contains) + " ${tests.size} " + getString(R.string.tests)
        val testsAdapter = TestsAdapter(tests)
        Log.d("tests", tests.toString())
        testsBottomSheetBinding.testsRv.layoutManager = LinearLayoutManager(this)
        testsBottomSheetBinding.testsRv.adapter = testsAdapter
        bottomSheetDialog.show()
    }

    private fun convertUriToBase64(context: Context, fileUri: Uri?): String? {
        if (fileUri == null) return ""

        return try {
            val mimeType = context.contentResolver.getType(fileUri)

            return if (mimeType?.startsWith("image/") == true) {
                // Compress the image (without resizing)
                val inputStream = context.contentResolver.openInputStream(fileUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap!!.compress(
                    Bitmap.CompressFormat.JPEG,
                    70,
                    byteArrayOutputStream
                )
                val byteArray = byteArrayOutputStream.toByteArray()

                Base64.encodeToString(byteArray, Base64.NO_WRAP)
            } else {
                // For PDFs or other file types
                val inputStream = context.contentResolver.openInputStream(fileUri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                if (bytes != null) {
                    Base64.encodeToString(bytes, Base64.NO_WRAP)
                } else {
                    ""
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


    private fun stringToUri(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
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