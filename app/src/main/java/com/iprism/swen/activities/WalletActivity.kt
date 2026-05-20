package com.iprism.swen.activities

import NetworkRetryHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.R
import com.iprism.swen.adapters.PricesAdapter
import com.iprism.swen.databinding.ActivityWalletBinding
import com.iprism.swen.interfaces.OnWalletPriceActionListener
import com.iprism.swen.models.checkpackage.CheckPaymentRequest
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.swen.models.wallet.WalletRequest
import com.iprism.swen.repository.CashFreePaymentRepository
import com.iprism.swen.repository.WalletRepository
import com.iprism.swen.utils.PaymentManager
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.CashFreePaymentViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import com.iprism.swen.viewmodels.WalletViewModel


class WalletActivity : AppCompatActivity(), PaymentManager.PaymentResultListener {

    private lateinit var binding : ActivityWalletBinding
    private lateinit var viewModel: WalletViewModel
    private lateinit var cashFreePaymentViewModel: CashFreePaymentViewModel
    private lateinit var paymentManager: PaymentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentManager = PaymentManager(this, this)
        handleBackImg()
        setUpPrices()
        handleWalletHistoryImg()
        handleContinueBtn()
        initViewModel()
        initCashFreeViewModel()
        observeResponse()
        observeAddAmountResponse()
        observeCashFreeResponse()
        observeCheckPaymentResponse()
        wallet("0","view", "")
    }

    private fun handleBackImg() {
        binding.backImg.setOnClickListener { view: View? -> finish() }
    }

    private fun handleWalletHistoryImg() {
        binding.walletHistoryImg.setOnClickListener { view: View? ->
            val intent = Intent(this, WalletHistoryActivity::class.java);
            startActivity(intent)
        }
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (getAmount().isEmpty() || getAmount().startsWith("0")) {
                showToast(getString(R.string.please_enter_valid_amount))
            } else {
                //wallet(getAmount(), "recharge")
                createOrder(getAmount())
            }
        })
    }

    private fun getAmount() : String {
        return binding.amountEt.text.toString().trim()
    }

    private fun setPrices() : ArrayList<String> {
        val prices : ArrayList<String> = ArrayList()
        prices.add("₹${formatPrice("100")}")
        prices.add("₹${formatPrice("500")}")
        prices.add("₹${formatPrice("1000")}")
        prices.add("₹${formatPrice("1500")}")
        prices.add("₹${formatPrice("2000")}")
        prices.add("₹${formatPrice("2500")}")
        prices.add("₹${formatPrice("3000")}")
        prices.add("₹${formatPrice("3500")}")
        prices.add("₹${formatPrice("4000")}")
        prices.add("₹${formatPrice("4500")}")
        prices.add("₹${formatPrice("5000")}")
        return prices
    }

    private fun setUpPrices() {
        val pricesAdapter = PricesAdapter(setPrices())
        binding.pricesRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.pricesRv.adapter = pricesAdapter
        pricesAdapter.setOnWalletActionListener(object : OnWalletPriceActionListener {
            override fun onItemClicked(price: String) {
                binding.amountEt.setText(price)
            }
        })
    }

    private fun formatPrice(amountStr: String): String {
        var output = ""
        output = if (amountStr.length == 3) {
            amountStr
        } else if (amountStr.length == 4) {
            amountStr.substring(0, 1) + "," + amountStr.substring(1)
        } else if (amountStr.length == 5) {
            amountStr.substring(0, 2) + "," + amountStr.substring(2)
        } else if (amountStr.length == 6) {
            amountStr.substring(0, 1) + "," + amountStr.substring(1, 3) + "," + amountStr.substring(3)
        } else if (amountStr.length == 7) {
            amountStr.substring(0, 2) + "," + amountStr.substring(2, 4) + "," + amountStr.substring(4)
        } else if (amountStr.length == 8) {
            amountStr.substring(0, 1) + "," + amountStr.substring(1, 3) + "," + amountStr.substring(3, 5) + "," + amountStr.substring(5)
        } else {
            amountStr
        }
        return output
    }

    private fun initViewModel() {
        val repository = WalletRepository()
        val factory = ViewModelFactory { WalletViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[WalletViewModel::class.java]
    }

    private fun initCashFreeViewModel() {
        val repository = CashFreePaymentRepository()
        val factory = ViewModelFactory { CashFreePaymentViewModel(repository) }
        cashFreePaymentViewModel = ViewModelProvider(this, factory)[CashFreePaymentViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.amountTxt.text = "₹${result.data.response.walletAmount}"
                    binding.amountEt.setText("")
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeAddAmountResponse() {
        viewModel.addAmountResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    wallet("0", "view", "")
                }

                is UiState.Error -> {
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
                        wallet(result.data.response.orderAmount, "recharge", result.data.response.orderId)
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

    private fun wallet(amount : String, viewType : String, transactionId : String) {
        val userDetails = getUserDetails()
        val request = WalletRequest(transactionId, amount.toInt(), userDetails[User.ID]!!.toInt(), viewType, userDetails[User.AUTH_TOKEN].toString())
        if (viewType.equals("view", true)) {
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.wallet(req)
            }
        } else {
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.addWalletAmount(req)
            }
        }
        Log.d("requestLoading", request.toString())
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