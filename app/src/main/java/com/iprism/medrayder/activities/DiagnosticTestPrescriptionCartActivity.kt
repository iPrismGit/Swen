package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivityDiagnosticTestPrescriptionCartBinding
import com.iprism.medrayder.models.diagnosticprescriptionbooking.DiagnosticPrescriptionBookingRequest
import com.iprism.medrayder.models.labtestprescriptionbooking.Response
import com.iprism.medrayder.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.DRY
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.DiagnosticTestPrescriptionCartViewModel
import com.iprism.medrayder.viewmodels.LabTestPrescriptionCartViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.IOException

class DiagnosticTestPrescriptionCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiagnosticTestPrescriptionCartBinding
    private lateinit var viewModel: DiagnosticTestPrescriptionCartViewModel
    private var imageUri = ""
    private var image = ""
    private var diagnosticId = ""
    private var lat = ""
    private var lon = ""
    private var familyMembersItem : FamilyMembersItem? =  null
    private var bitmap: Bitmap? = null
    private var bookingResponse : Response? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnosticTestPrescriptionCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            imageUri = intent.getStringExtra("imageUri")!!
            familyMembersItem = intent.getSerializableExtra("familyMember") as FamilyMembersItem?
            diagnosticId = intent.getStringExtra("diagnosticId")!!
            image = convertUriToBase64(this, stringToUri(imageUri))!!
            Log.d("imagePdf", imageUri)
            Log.d("imagePdf", image)
        }
        handleBack()
        handleConfirmBtn()
        handleChangeTxt()
        handleLocationLl()
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

    private fun handleLocationLl() {
        binding.locationLl.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
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
        val repository = DiagnosticsRepository()
        val factory = ViewModelFactory { DiagnosticTestPrescriptionCartViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DiagnosticTestPrescriptionCartViewModel::class.java]
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
                    lat = result.data.response.address.lat ?: ""
                    lon = result.data.response.address.lon ?: ""
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
                        val intent = Intent(this@DiagnosticTestPrescriptionCartActivity, SubscriptionActivity::class.java)
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
        val request : DiagnosticPrescriptionBookingRequest
        if (viewType.equals("view", true)) {
            request = DiagnosticPrescriptionBookingRequest(image, userDetails[User.ID]!!.toInt(), "", "", viewType, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), diagnosticId.toInt(), familyMembersItem!!.id)
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.viewDiagnosticTestPrescription(req)
            }
        } else {
            request = DiagnosticPrescriptionBookingRequest(image, userDetails[User.ID]!!.toInt(), bookingResponse!!.receiptDetails.name, bookingResponse!!.receiptDetails.mobile, viewType, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), diagnosticId.toInt(), familyMembersItem!!.id)
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.bookDiagnosticTestPrescription(req)
            }
        }
        Log.d("requestLoading", request.toString())
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
}