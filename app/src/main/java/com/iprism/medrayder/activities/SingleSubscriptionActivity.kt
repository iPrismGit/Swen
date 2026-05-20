package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.PlansAdapter
import com.iprism.medrayder.databinding.ActivitySubscriptionBinding
import com.iprism.medrayder.databinding.SubscriptionDetailsBsBinding
import com.iprism.medrayder.interfaces.OnSubscriptionItemClickListener
import com.iprism.medrayder.models.addfamilymember.AddFamilyMemberRequest
import com.iprism.medrayder.models.addfamilymembersub.AddFamilyMemberSubRequest
import com.iprism.medrayder.models.checkpackage.CheckPaymentRequest
import com.iprism.medrayder.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.medrayder.models.subscription.HealthCardsItem
import com.iprism.medrayder.models.subscription.Response
import com.iprism.medrayder.repository.CashFreePaymentRepository
import com.iprism.medrayder.repository.SubscriptionRepository
import com.iprism.medrayder.utils.PaymentManager
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.CashFreePaymentViewModel
import com.iprism.medrayder.viewmodels.SingleSubscriptionViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class SingleSubscriptionActivity : AppCompatActivity(), PaymentManager.PaymentResultListener {

    private lateinit var binding : ActivitySubscriptionBinding
    private lateinit var viewModel: SingleSubscriptionViewModel
    private var details: Response? = null
    private var plan: HealthCardsItem? = null
    private var memberRequest: AddFamilyMemberRequest? = null
    private lateinit var cashFreePaymentViewModel: CashFreePaymentViewModel
    private lateinit var paymentManager: PaymentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentManager = PaymentManager(this, this)
        if (intent.hasExtra("request")) {
            memberRequest = intent.getSerializableExtra("request")!! as AddFamilyMemberRequest
        }
        handleBack()
        handlePayNowBtn()
        handleBillDetailsTxt()
        initViewModel()
        initCashFreeViewModel()
        observeResponse()
        observeBillSummaryResponse()
        observeSubscribeResponse()
        observeCashFreeResponse()
        observeCheckPaymentResponse()
        subscription()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handlePayNowBtn() {
        binding.payNowBtn.setOnClickListener(View.OnClickListener {
            createOrder(details!!.billSummary.totalAmount)
        })
    }

    private fun handleBillDetailsTxt() {
        binding.viewDetailsTxt.setOnClickListener(View.OnClickListener {
            showBillDetailsBottomSheet(this)
        })
    }

    private fun initViewModel() {
        val repository = SubscriptionRepository()
        val factory = ViewModelFactory { SingleSubscriptionViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SingleSubscriptionViewModel::class.java]
    }

    private fun initCashFreeViewModel() {
        val repository = CashFreePaymentRepository()
        val factory = ViewModelFactory { CashFreePaymentViewModel(repository) }
        cashFreePaymentViewModel = ViewModelProvider(this, factory)[CashFreePaymentViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    setupAddressList(result.data.response.healthCards)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeBillSummaryResponse() {
        viewModel.billSummaryResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    details = result.data.response
                    binding.ll.visibility = View.VISIBLE
                    binding.totalPriceTxt.text = "₹" + details!!.billSummary.totalAmount
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeSubscribeResponse() {
        viewModel.paymentResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    val resultIntent = Intent()
                    setResult(RESULT_OK, resultIntent)
                    finish()
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
                        subscribe(result.data.response.orderId)
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

    private fun setupAddressList(plans : List<HealthCardsItem>) {
        val plansAdapter = PlansAdapter(plans)
        binding.plansRv.layoutManager = LinearLayoutManager(this)
        binding.plansRv.adapter = plansAdapter
        plansAdapter.setOnSubscriptionItemClickListener(object : OnSubscriptionItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun onItemClicked(plan: HealthCardsItem) {
                this@SingleSubscriptionActivity.plan = plan
                fetchBillSummary()
            }
        })
    }

    private fun subscription() {
        val userDetails = getUserDetails()
        val request = AddFamilyMemberSubRequest("", memberRequest!!.image, memberRequest!!.gender, memberRequest!!.mobile, "view", "0", userDetails[User.LANG]!!, 0, "",0,  userDetails[User.ID]!!.toInt(), "0", memberRequest!!.dob, 0, memberRequest!!.name, memberRequest!!.bloodGroup, userDetails[User.AUTH_TOKEN].toString(), 0, memberRequest!!.email, "", memberRequest!!.coverageCategory.toString())
        viewModel.subscription(request)
        Log.d("requestLoading", request.toString())
    }

    private fun fetchBillSummary() {
        val userDetails = getUserDetails()
        val request = AddFamilyMemberSubRequest("", memberRequest!!.image, memberRequest!!.gender, memberRequest!!.mobile, "bill_summary", "0", userDetails[User.LANG]!!, plan!!.id, plan!!.duration,0,  userDetails[User.ID]!!.toInt(), "0", memberRequest!!.dob, plan!!.discountPrice, memberRequest!!.name, memberRequest!!.bloodGroup, userDetails[User.AUTH_TOKEN].toString(), 0, memberRequest!!.email, "", memberRequest!!.coverageCategory.toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchBillSummary(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun subscribe(transactionId : String) {
        val userDetails = getUserDetails()
        val ids = mutableListOf<Int>()
        for (i in 0 until details!!.familyMembers.size) {
            val member = details!!.familyMembers[i]
            ids.add(member.id)
        }
        val request = AddFamilyMemberSubRequest(transactionId, memberRequest!!.image, memberRequest!!.gender, memberRequest!!.mobile, "payment", details!!.billSummary.gst, userDetails[User.LANG]!!, plan!!.id, plan!!.duration,details!!.billSummary.totalPremium,  userDetails[User.ID]!!.toInt(), details!!.billSummary.totalAmount, memberRequest!!.dob, plan!!.discountPrice, memberRequest!!.name, memberRequest!!.bloodGroup, userDetails[User.AUTH_TOKEN].toString(), details!!.billSummary.familyMembersCount, memberRequest!!.email,  plan!!.name, memberRequest!!.coverageCategory.toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.subscribe(req)
        }
        Log.d("requestLoading", request.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun showBillDetailsBottomSheet(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val bsBinding = SubscriptionDetailsBsBinding.inflate(
            LayoutInflater.from(context))
        bottomSheetDialog.setContentView(bsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        bsBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        bsBinding.totalAmountTxt.text = "₹${details!!.billSummary.totalPremium}"
        bsBinding.coveredTxt.text = "${details!!.billSummary.familyMembersCount} ${if (details!!.billSummary.familyMembersCount == 1) getString(R.string.person) else getString(R.string.persons)}"
        bsBinding.gstTxt.text = "₹${details!!.billSummary.gst}"
        bsBinding.totalTxt.text = "₹${details!!.billSummary.totalAmount}"
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