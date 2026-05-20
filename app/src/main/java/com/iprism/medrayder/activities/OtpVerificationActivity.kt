package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivityOtpVerificationBinding
import com.iprism.medrayder.models.login.LoginRequest
import com.iprism.medrayder.models.resendotp.ResendOtpRequest
import com.iprism.medrayder.repository.AuthRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.setEnabledState
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.LoginViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpVerificationBinding
    private lateinit var viewModel: LoginViewModel
    private var otp = ""
    private var mobile = ""
    private var referralCode = ""
    private var countDownTime = ""
    private var countDownTimer: CountDownTimer? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("otp")) {
            otp = intent.getStringExtra("otp")!!
            mobile = intent.getStringExtra("mobile")!!
            referralCode = intent.getStringExtra("referralCode")!!
            binding.mobileTxt.text = "+91$mobile"
        }
        handleContinueBtn()
        handleBack()
        initViewModel()
        observeLoginResponse()
        countDown()
        handleResendTxt()
        observeResendOtpResponse()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (getOtp() != otp) {
                showToast(getString(R.string.please_enter_valid_otp))
            } else {
                val request = LoginRequest(mobile, "verified", "", referralCode)
                NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                    viewModel.login(req)
                }
            }
        })
    }

    private fun handleResendTxt() {
        binding.resendTxt.setOnClickListener(View.OnClickListener {
            if (!binding.countDownTimeTxt.text.toString().equals("00:00", true)) {
                showToast("${getString(R.string.please_try_after)} $countDownTime ${getString(R.string.to_resend_otp)}")
            } else {
                val request = ResendOtpRequest(mobile)
                NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                    viewModel.resendOtp(req)
                }
            }
        })
    }

    private fun getOtp() : String {
        return binding.otpEt.text.toString().trim()
    }

    private fun initViewModel() {
        val repository = AuthRepository()
        val factory = ViewModelFactory { LoginViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun observeLoginResponse() {
        viewModel.loginResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.continueBtn.setEnabledState(false)
                }

                is UiState.Success -> {
                    countDownTimer!!.cancel()
                    binding.continueBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    if (result.data.loginResponse.regStatus.equals("yes", true)) {
                        val user = User(this)
                        user.storeUserDetails(result.data.loginResponse.id, result.data.loginResponse.authToken, result.data.loginResponse.mobile)
                        user.loginUser()
                        if (result.data.loginResponse.userAddressesStatus.equals("yes", true)) {
                            user.saveAddress()
                        }
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        val intent = Intent(this, RegisterActivity::class.java)
                        intent.putExtra("userId", result.data.loginResponse.id)
                        intent.putExtra("mobile", mobile)
                        startActivity(intent)
                        finish()
                    }
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                    binding.continueBtn.setEnabledState(true)
                }
            }
        }
    }

    private fun observeResendOtpResponse() {
        viewModel.resendResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    countDown()
                    binding.progress.hideProgress()
                    otp = result.data.resendOtpResponse.otp
                    //showToast(otp)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun countDown() {
        countDownTimer = object : CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.countDownTimeTxt.text = "00:" + millisUntilFinished / 1000
                countDownTime = (millisUntilFinished / 1000).toString() + "s"
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding.countDownTimeTxt.text = "00:00"
            }
        }.start()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }
}