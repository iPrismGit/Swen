package com.iprism.swen.activities

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
import com.iprism.swen.R
import com.iprism.swen.adapters.PlansAdapter
import com.iprism.swen.databinding.ActivitySubscriptionBinding
import com.iprism.swen.databinding.SubscriptionDetailsBsBinding
import com.iprism.swen.interfaces.OnSubscriptionItemClickListener
import com.iprism.swen.models.checkpackage.CheckPaymentRequest
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.swen.models.subscription.HealthCardsItem
import com.iprism.swen.models.subscription.Response
import com.iprism.swen.models.subscription.SubscriptionRequest
import com.iprism.swen.repository.CashFreePaymentRepository
import com.iprism.swen.repository.SubscriptionRepository
import com.iprism.swen.utils.PaymentManager
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.CashFreePaymentViewModel
import com.iprism.swen.viewmodels.SubscriptionViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class SubscriptionActivity : AppCompatActivity(), PaymentManager.PaymentResultListener {

    private lateinit var binding: ActivitySubscriptionBinding
    private lateinit var viewModel: SubscriptionViewModel
    private var details: Response? = null
    private var plan: HealthCardsItem? = null
    private var tag: String = ""
    private lateinit var cashFreePaymentViewModel: CashFreePaymentViewModel
    private lateinit var paymentManager: PaymentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentManager = PaymentManager(this, this)
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
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
            if (tag.equals("booking", true) || tag.equals("subscribe", true)) {
                finish()
            } else {
                /*val intent = Intent(this, AddressActivity::class.java)
                intent.putExtra("tag", "register")*/
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun handleBillDetailsTxt() {
        binding.viewDetailsTxt.setOnClickListener(View.OnClickListener {
            showBillDetailsBottomSheet(this)
        })
    }

    private fun handlePayNowBtn() {
        binding.payNowBtn.setOnClickListener(View.OnClickListener {
            createOrder(details!!.billSummary.totalAmount)
        })
    }

    private fun initViewModel() {
        val repository = SubscriptionRepository()
        val factory = ViewModelFactory { SubscriptionViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SubscriptionViewModel::class.java]
    }

    private fun initCashFreeViewModel() {
        val repository = CashFreePaymentRepository()
        val factory = ViewModelFactory { CashFreePaymentViewModel(repository) }
        cashFreePaymentViewModel =
            ViewModelProvider(this, factory)[CashFreePaymentViewModel::class.java]
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
                    if (tag.equals("booking", true)) {
                        val resultIntent = Intent()
                        resultIntent.putExtra("tag", "booking")
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    } else if (tag.equals("subscribe", true)) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finishAffinity()
                    } else {
                        /*val intent = Intent(this, AddressActivity::class.java)
                        intent.putExtra("tag", "register")*/
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
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
                    paymentManager.startPayment(
                        result.data.response.orderId,
                        result.data.response.paymentSessionId
                    )
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

    private fun setupAddressList(plans: List<HealthCardsItem>) {
        val plansAdapter = PlansAdapter(plans)
        binding.plansRv.layoutManager = LinearLayoutManager(this)
        binding.plansRv.adapter = plansAdapter
        plansAdapter.setOnSubscriptionItemClickListener(object : OnSubscriptionItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun onItemClicked(plan: HealthCardsItem) {
                this@SubscriptionActivity.plan = plan
                fetchBillSummary()
            }
        })
    }

    private fun subscription() {
        val userDetails = getUserDetails()
        val request = SubscriptionRequest(
            "",
            "",
            "view",
            "0",
            userDetails[User.LANG]!!,
            0,
            "",
            0,
            userDetails[User.ID]!!,
            "0",
            0,
            "",
            userDetails[User.AUTH_TOKEN].toString(),
            0
        )
        viewModel.subscription(request)
        Log.d("requestLoading", request.toString())
    }

    private fun fetchBillSummary() {
        val userDetails = getUserDetails()
        val request = SubscriptionRequest(
            "",
            "",
            "bill_summary",
            "0",
            userDetails[User.LANG]!!,
            plan!!.id,
            plan!!.duration,
            0,
            userDetails[User.ID]!!,
            "0",
            plan!!.discountPrice,
            plan!!.name,
            userDetails[User.AUTH_TOKEN].toString(),
            0
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchBillSummary(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun subscribe(transactionId: String) {
        val userDetails = getUserDetails()
        val ids = mutableListOf<Int>()
        for (i in 0 until details!!.familyMembers.size) {
            val member = details!!.familyMembers[i]
            ids.add(member.id)
        }
        val request = SubscriptionRequest(
            transactionId,
            ids.toString().replace("[", "").replace("]", "").replace(" ", ""),
            "payment",
            details!!.billSummary.gst,
            userDetails[User.LANG]!!,
            plan!!.id,
            plan!!.duration,
            details!!.billSummary.totalPremium,
            userDetails[User.ID]!!,
            details!!.billSummary.totalAmount,
            details!!.billSummary.price,
            plan!!.name,
            userDetails[User.AUTH_TOKEN].toString(),
            details!!.billSummary.familyMembersCount
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.subscribe(req)
        }
        Log.d("requestLoading", request.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun showBillDetailsBottomSheet(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val bsBinding = SubscriptionDetailsBsBinding.inflate(
            LayoutInflater.from(context)
        )
        bottomSheetDialog.setContentView(bsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        bsBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        bsBinding.totalAmountTxt.text = "₹${details!!.billSummary.totalPremium}"
        bsBinding.coveredTxt.text = "${details!!.billSummary.familyMembersCount} ${
            if (details!!.billSummary.familyMembersCount == 1) getString(R.string.person) else getString(
                R.string.persons
            )
        }"
        bsBinding.gstTxt.text = "₹${details!!.billSummary.gst}"
        bsBinding.totalTxt.text = "₹${details!!.billSummary.totalAmount}"
        bottomSheetDialog.show()
    }

    private fun createOrder(amount: String) {
        val userDetails = getUserDetails()
        val request = CreateCashFreeOrderRequest(amount, userDetails[User.ID]!!.toInt())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            cashFreePaymentViewModel.createCashFreeOrder(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun checkPaymentStatus(orderId: String) {
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