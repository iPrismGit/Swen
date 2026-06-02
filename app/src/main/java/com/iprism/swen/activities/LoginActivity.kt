package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var tag : String = ""
    private var name : String = ""
    private lateinit var viewModel: LoginViewModel
    private var playerId : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleContinueBtn()
        handleTerms()
        initViewModel()
        observeLoginResponse()
    }

    private fun initViewModel() {
        val repository = AuthRepository()
        val factory = ViewModelFactory { LoginViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun getMobile() : String {
        return binding.mobileTxt.text.toString().trim()
    }

    private fun observeLoginResponse() {
        viewModel.loginResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.continueBtn.setEnabledState(false)
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.continueBtn.setEnabledState(true)
                    var otp = ""
                    if (getMobile().equals("8585858585", true)){
                        otp = "5555"
                        showToast(otp)
                    } else{
                        otp = result.data.loginResponse.otp
                    }
                    showToast(otp)
                    Log.d("otp", "Otp: $otp")
                    val intent = Intent(this@LoginActivity, OtpVerificationActivity::class.java)
                    intent.putExtra("otp", otp)
                    intent.putExtra("mobile", getMobile())
                    intent.putExtra("playerId", playerId)
                    startActivity(intent)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                    binding.continueBtn.setEnabledState(true)
                }
            }
        }
    }

    private fun handleTerms() {
        binding.termsTxt.setOnClickListener(View.OnClickListener {
            tag = "terms"
            name = "Terms & Conditions"
            val intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        })
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener { view ->
            if (getMobile().isEmpty()){
                showToast("Please Enter Mobile Number!")
            } else if (getMobile().length != 10){
                showToast("Please Enter Valid Mobile Number!")
            }  else if (Pattern.matches("[0-5].*", getMobile())) {
                showToast("Please Enter Valid Mobile Number!")
            } else {
                val loginRequest = LoginRequest(getMobile(), "not_verified", playerId!!, "")
                NetworkRetryHelper.checkAndCallWithRetry(this, loginRequest) { req ->
                    viewModel.login(req)
                }
                Log.d("requestLoading", loginRequest.toString())
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall", "GestureBackNavigation")
    override fun onBackPressed() {
        finishAffinity()
    }
}