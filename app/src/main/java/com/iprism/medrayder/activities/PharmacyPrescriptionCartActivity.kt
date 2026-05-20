package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivityPharmacyPrescriptionCartBinding
import com.iprism.medrayder.models.pharmacyprescriptionbooking.PharmacyPrescriptionBookingRequest
import com.iprism.medrayder.models.pharmacyprescriptionbooking.Response
import com.iprism.medrayder.repository.PharmaciesRepository
import com.iprism.medrayder.utils.DRY
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.PharmacyPrescriptionCartViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.IOException

class PharmacyPrescriptionCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPharmacyPrescriptionCartBinding
    private lateinit var viewModel: PharmacyPrescriptionCartViewModel
    private var imageUri = ""
    private var image = ""
    private var pharmacyId = ""
    private var orderType = ""
    private var bitmap: Bitmap? = null
    private var bookingResponse : Response? =  null
    private var lat = ""
    private var lon = ""
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacyPrescriptionCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            imageUri = intent.getStringExtra("imageUri")!!
            pharmacyId = intent.getStringExtra("pharmacyId")!!
            orderType = intent.getStringExtra("orderType")!!
            image = convertUriToBase64(this, stringToUri(imageUri))!!
        }
        handleBack()
        handleConfirmBtn()
        handlePickupLocation()
        handleChangeTxt()
        initViewModel()
        observeResponse()
        observeBookingResponse()
        bookPrescriptionDiagnosticTest("view")
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                bookPrescriptionDiagnosticTest("view")
            }
        }
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

    private fun handlePickupLocation() {
        binding.pickUpLocationLl.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
        })
    }

    private fun handleChangeTxt() {
        binding.changeTxt.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            launcher.launch(intent)
        })
    }

    private fun initViewModel() {
        val repository = PharmaciesRepository()
        val factory = ViewModelFactory { PharmacyPrescriptionCartViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[PharmacyPrescriptionCartViewModel::class.java]
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
                    binding.imageTxt.text = imageUri
                    if (orderType.equals("home_delivery", true)) {
                        binding.deliveryAddressLl.visibility = View.VISIBLE
                        binding.pickUpLocationLl.visibility = View.GONE
                        binding.addressTypeTxt.text = result.data.response.address.addressType
                        binding.addressTxt.text = listOf(
                            result.data.response.address.hno,
                            result.data.response.address.buildingNo,
                            result.data.response.address.landmark,
                            result.data.response.address.address
                        ).filter { !it.isNullOrBlank() }
                            .joinToString(", ")
                    } else {
                        binding.deliveryAddressLl.visibility = View.GONE
                        binding.pickUpLocationLl.visibility = View.VISIBLE
                        binding.pickUpLocationTxt.text = result.data.response.storeAddress.colony
                        lat = result.data.response.storeAddress.lat
                        lon = result.data.response.storeAddress.lon
                    }
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
        val request : PharmacyPrescriptionBookingRequest
        if (viewType.equals("view", true)) {
            request = PharmacyPrescriptionBookingRequest(image, userDetails[User.ID]!!.toInt(), pharmacyId.toInt(), 0, "", "", viewType, userDetails[User.AUTH_TOKEN].toString(), orderType, userDetails[User.LANG].toString())
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.viewPharmacyPrescriptionOrder(req)
            }
        } else {
            request = PharmacyPrescriptionBookingRequest(image, userDetails[User.ID]!!.toInt(),  pharmacyId.toInt(), bookingResponse!!.address.id, bookingResponse!!.receiptDetails.name, bookingResponse!!.receiptDetails.mobile, viewType, userDetails[User.AUTH_TOKEN].toString(), orderType, userDetails[User.LANG].toString())
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.bookPharmacyPrescriptionOrder(req)
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