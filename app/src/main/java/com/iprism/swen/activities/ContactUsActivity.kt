package com.iprism.swen.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityContactUsBinding
import com.iprism.swen.models.contactus.ContactUsRequest
import com.iprism.swen.repository.CommonRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.ContactUsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import java.util.regex.Pattern

class ContactUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding
    private lateinit var viewModel: ContactUsViewModel
    private var mobile = ""
    private val CALL_PHONE_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleContinueClick()
        handleCallNow()
        handleBack()
        initViewModel()
        observeResponse()
        observeInsertResponse()
        val userDetails = getUserDetails()
        val request = ContactUsRequest(userDetails[User.ID]!!.toInt(),  "", "", "view", userDetails[User.AUTH_TOKEN].toString(), "", "")
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.viewContactUs(req)
        }
        Log.d("request", request.toString())
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleCallNow() {
        binding.callNowLl.setOnClickListener(View.OnClickListener {
            makePhoneCall(mobile)
        })
    }

    private fun handleContinueClick() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (getName().isEmpty()) {
                showToast(getString(R.string.please_enter_name))
            } else if (getName().length < 3) {
                showToast(getString(R.string.pls_enter_3_chars))
            } else if (getEmail().isEmpty()) {
                showToast(getString(R.string.please_enter_email))
            } else if (!isValidGmail(getEmail())) {
                showToast(getString(R.string.please_enter_valid_email))
            } else if (getMobile().length != 10) {
                showToast(getString(R.string.please_enter_10_digit_mobile))
            } else if (Pattern.matches("[0-5].*", getMobile())) {
                showToast(getString(R.string.please_enter_valid_mobile))
            } else if (getMessage().isEmpty()) {
                showToast(getString(R.string.please_enter_message))
            } else {
                val userDetails = getUserDetails()
                val request = ContactUsRequest(userDetails[User.ID]!!.toInt(),  getName(), getMobile(), "insert", userDetails[User.AUTH_TOKEN].toString(), getMessage(), getEmail())
                NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                    viewModel.contactUs(req)
                }
                Log.d("request", request.toString())
            }
        })
    }

    private fun isValidGmail(email: String): Boolean {
        val gmailRegex = "^[a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._%+-]*[a-zA-Z0-9]@gmail\\.com$"
        return Regex(gmailRegex).matches(email)
    }

    private fun getName() : String {
        return binding.nameTxt.text.toString().trim()
    }

    private fun getEmail() : String {
        return binding.emailIdTxt.text.toString().trim()
    }

    private fun getMobile() : String {
        return binding.phoneNumberTxt.text.toString().trim()
    }

    private fun getMessage() : String {
        return binding.messageTxt.text.toString().trim()
    }

    private fun initViewModel() {
        val repository = CommonRepository()
        val factory = ViewModelFactory { ContactUsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[ContactUsViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    mobile = result.data.response.mobile
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeInsertResponse() {
        viewModel.insertResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    showToast("Report Sent Successfully")
                    finish()
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_PHONE_PERMISSION_CODE
            )
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_PHONE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(mobile)
            } else {
                showToast(getString(R.string.permissission_denied_make_calls))
            }
        }
    }
}