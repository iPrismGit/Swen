package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityDiagnosticTestPrescriptionCartBinding
import com.iprism.swen.models.hospitaldiagnosticprescriptionbooking.HospitalDiagnosticPrescriptionBookingRequest
import com.iprism.swen.models.hospitaldiagnosticprescriptionbooking.Response
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.repository.HospitalDiagnosticsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HospitalDiagnosticTestPrescriptionCartViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import java.io.IOException

class HospitalDiagnosticTestPrescriptionCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiagnosticTestPrescriptionCartBinding
    private lateinit var viewModel: HospitalDiagnosticTestPrescriptionCartViewModel
    private var imageUri = ""
    private var image = ""
    private var hospitalId = ""
    private var familyMembersItem : FamilyMembersItem? =  null
    private var bitmap: Bitmap? = null
    private var bookingResponse : Response? =  null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnosticTestPrescriptionCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.hospitalAddressTxt.text = getString(R.string.hospital_address)
        if (intent.hasExtra("tag")) {
            imageUri = intent.getStringExtra("imageUri")!!
            familyMembersItem = intent.getSerializableExtra("familyMember") as FamilyMembersItem?
            hospitalId = intent.getStringExtra("hospitalId")!!
            image = convertUriToBase64(this, stringToUri(imageUri))!!
            Log.d("imagePdf", imageUri)
            Log.d("imagePdf", image)
        }
        handleBack()
        handleConfirmBtn()
        handleChangeTxt()
        initViewModel()
        observeResponse()
        observeBookingResponse()
        bookPrescriptionDiagnosticTest("view")
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleConfirmBtn() {
        binding.confirmBtn.setOnClickListener(View.OnClickListener {
            binding.confirmBtn.isActivated = false
            binding.confirmBtn.isEnabled = false
            binding.confirmBtn.isClickable = false
            bookPrescriptionDiagnosticTest("order")
        })
    }

    private fun handleChangeTxt() {
        binding.changeTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        })
    }

    private fun initViewModel() {
        val repository = HospitalDiagnosticsRepository()
        val factory = ViewModelFactory { HospitalDiagnosticTestPrescriptionCartViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalDiagnosticTestPrescriptionCartViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    bookingResponse = result.data.response
                    binding.scrollView.visibility = View.VISIBLE
                    binding.divider3.visibility = View.VISIBLE
                    binding.confirmBtn.visibility = View.VISIBLE
                    binding.patientNameTxt.setText(result.data.response.receiptDetails.name)
                    binding.patientMobileTxt.setText(result.data.response.receiptDetails.mobile)
                    binding.locationTxt.text = result.data.response.address.location ?: ""
                    binding.imageTxt.text = imageUri
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeBookingResponse() {
        viewModel.bookingResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    val intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", getString(R.string.order_placed_successfully))
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.confirmBtn.isActivated = true
                    binding.confirmBtn.isEnabled = true
                    binding.confirmBtn.isClickable = true
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

    private fun bookPrescriptionDiagnosticTest(viewType : String) {
        val userDetails = getUserDetails()
        val request : HospitalDiagnosticPrescriptionBookingRequest
        if (viewType.equals("view", true)) {
            request = HospitalDiagnosticPrescriptionBookingRequest(image, userDetails[User.ID]!!.toInt(), "", "", viewType, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), hospitalId.toInt(), familyMembersItem!!.id)
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.viewHospitalDiagnosticTestPrescription(req)
            }
        } else {
            request = HospitalDiagnosticPrescriptionBookingRequest(image, userDetails[User.ID]!!.toInt(), bookingResponse!!.receiptDetails.name, bookingResponse!!.receiptDetails.mobile, viewType, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), hospitalId.toInt(), familyMembersItem!!.id)
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.bookHospitalDiagnosticTestPrescription(req)
            }
        }
        Log.d("requestLoading", request.toString())
    }

    private fun convertUriToBase64(context: Context, fileUri: Uri?): String? {
        if (fileUri == null) return ""
        try {
            val mimeType = context.contentResolver.getType(fileUri)
            val inputStream = context.contentResolver.openInputStream(fileUri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            return if (bytes != null) {
                Base64.encodeToString(bytes, Base64.NO_WRAP)
            } else {
                ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun stringToUri(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }
}