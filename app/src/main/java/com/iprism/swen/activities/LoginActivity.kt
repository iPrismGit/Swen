package com.iprism.swen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityLoginBinding
import com.iprism.swen.models.login.LoginRequest
import com.iprism.swen.repository.AuthRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.setEnabledState
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.LoginViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import com.onesignal.OneSignal
import org.json.JSONObject
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var playerId : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleContinueBtn()
        handleTermsConditionTxt()
        initViewModel()
        observeLoginResponse()
        OneSignal.initWithContext(this)

        // Replace with your OneSignal App ID

        // Replace with your OneSignal App ID
        OneSignal.setAppId("cebaa375-de95-4fb8-9403-71089f304ffe")
        Log.d("OneSignal", "Device is subscribed: " + OneSignal.getDeviceState()!!.isSubscribed)
        val deviceState = OneSignal.getDeviceState()
        if (deviceState != null) {
            playerId = deviceState.userId ?: ""
            Log.d("OneSignal", "Player ID1: $playerId")
        }
        OneSignal.sendTags(JSONObject().put("user_type", "user"))
    }

    private fun handleTermsConditionTxt() {
        binding.termsConditionsTxt.setOnClickListener(View.OnClickListener {
            val tag = "terms"
            val name = getString(R.string.terms_and_conditions)
            val intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        })
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (getMobile().length != 10) {
                showToast(getString(R.string.please_enter_10_digit_mobile))
            } else if (Pattern.matches("[0-5].*", getMobile())) {
                showToast(getString(R.string.please_enter_valid_mobile))
            } else {
                val request = LoginRequest(getMobile(), "not_verified", playerId!!, binding.referralCodeEt.text.toString().trim())
                NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                    viewModel.login(req)
                }
                Log.d("loginRequest", request.toString())
            }
        })
    }

    private fun getMobile() : String {
        return binding.mobileEt.text.toString().trim()
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
                    binding.continueBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    if (getMobile().equals("8585858585", true)) {
                        showToast(result.data.loginResponse.otp)
                    }
                    showToast(result.data.loginResponse.otp)
                    Log.d("otpMobile", result.data.loginResponse.otp)
                    val intent = Intent(this@LoginActivity, OtpVerificationActivity::class.java)
                    intent.putExtra("otp", result.data.loginResponse.otp)
                    intent.putExtra("mobile", getMobile())
                    intent.putExtra("referralCode", binding.referralCodeEt.text.toString().trim())
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.continueBtn.setEnabledState(true)
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }
}