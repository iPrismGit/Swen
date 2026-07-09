package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.R
import com.iprism.swen.adapters.ImagesAdapter
import com.iprism.swen.databinding.ActivityHomeServicePatientDetailsBinding
import com.iprism.swen.interfaces.OnImageDeleteActionListener
import com.iprism.swen.models.homeservicesbooking.HomeServicesBookingRequest
import com.iprism.swen.repository.HomeVisitServicesRepository
import com.iprism.swen.utils.DateTimeUtils
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HomeServicesBookingViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import java.io.IOException
import java.util.regex.Pattern
import kotlin.text.toInt

class HomeServiceBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeServicePatientDetailsBinding
    private lateinit var viewModel : HomeServicesBookingViewModel
    private var imagesAdapter: ImagesAdapter? = null
    private var imageUri: Uri? = null
    private var PICK_IMAGE_MULTIPLE = 1
    private var imagesUris: ArrayList<Uri> = ArrayList()
    private var bitmap : Bitmap? = null
    private var categoryId : String = ""
    private var subCatId : String = ""
    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Handle the selected PDF file here
            handlePdfFile(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeServicePatientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra("catId")) {
            categoryId = intent.getStringExtra("catId")!!
            subCatId = intent.getStringExtra("subCatId")!!
        }
        handleBack()
        handleConfirmBookingBtn()
        handlePdfLL()
        handleGalleryLL()
        setupImagesAdapter()
        setupImagesRv()
        initViewModel()
        observeResponse()
        handleDateLo()
        handleDobLo()
        handleTimeLl()
        handleConfirmBooking()
    }

    private fun handleConfirmBooking() {
        binding.confirmBtn.setOnClickListener {
            if (getName().isEmpty()) {
                showToast("Please Enter Patient Name")
            } else if (getMobile().length != 10) {
                showToast(getString(R.string.please_enter_10_digit_mobile))
            } else if (Pattern.matches("[0-5].*", getMobile())) {
                showToast(getString(R.string.please_enter_valid_mobile))
            } else if (getEmail().isEmpty()) {
                showToast(getString(R.string.please_enter_email))
            } else if (!isValidGmail(getEmail())) {
                showToast(getString(R.string.please_enter_valid_email))
            } else if (getReason().isEmpty()) {
                showToast("Please Enter Reason for Booking")
            } else if (getDob().isEmpty()) {
                showToast(getString(R.string.please_enter_dob))
            } else if (getDate().isEmpty()) {
                showToast("Please Select Date")
            } else if (getTime().isEmpty()) {
                showToast("Please Select Time")
            } else {
                bookHomeServices()
            }
        }
    }

    private fun isValidGmail(email: String): Boolean {
        val gmailRegex = "^[a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._%+-]*[a-zA-Z0-9]@gmail\\.com$"
        return Regex(gmailRegex).matches(email)
    }

    private fun handleDateLo() {
        binding.dateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateTxt, true)
        })
    }

    private fun handleDobLo() {
        binding.dobLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dobTxt, true)
        })
    }

    private fun handleTimeLl() {
        binding.timeLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.timeTxt)
        })
    }

    private fun handleConfirmBookingBtn() {
        binding.confirmBtn.setOnClickListener { p0 ->
            val intent = Intent(this, SuccessActivity::class.java)
            intent.putExtra("tag", "Home Service Booking Completed ")
            startActivity(intent)
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { p0 ->
            finish()
        }
    }

    private fun setupImagesRv() {
        binding.imagesRv.layoutManager = GridLayoutManager(this, 3)
        binding.imagesRv.adapter = imagesAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupImagesAdapter() {
        imagesAdapter = ImagesAdapter()
        imagesAdapter!!.setCheckInImages(imagesUris)
        imagesAdapter!!.setOnDeleteActionListener(object : OnImageDeleteActionListener {
            override fun onDelete(position: Int) {
                imagesUris.removeAt(position)
                imagesAdapter!!.notifyDataSetChanged()
            }
        })
    }

    @SuppressLint("Range")
    private fun handlePdfFile(uri: Uri) {
        if (uri != null) {
            imagesUris.add(uri)
            imagesAdapter!!.notifyDataSetChanged()
        }
        // Example: Get file name
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                Log.d("PDF_NAME", "Selected file: $displayName")
                //binding.pdfNameTxt.visibility = View.VISIBLE
                //binding.pdfNameTxt.text = displayName
            }
        }

        // You can also open the InputStream to read the content
        val inputStream = contentResolver.openInputStream(uri)
        // Now you can upload, read, or display the PDF
    }

    @SuppressLint("IntentReset")
    private fun handlePdfLL() {
        binding.pdfLl.setOnClickListener(View.OnClickListener {
            if (imagesUris.size == 0) {
                pdfPickerLauncher.launch("application/pdf")
            } else {
                showToast(getString(R.string.select_only_one_option_pdf_or_gallery))
            }
        })
    }

    @SuppressLint("IntentReset")
    private fun handleGalleryLL() {
        binding.galleryLl.setOnClickListener(View.OnClickListener {
            if (imagesUris.size == 0) {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(
                    intent, "Select Picture"),
                    PICK_IMAGE_MULTIPLE)
            } else {
                showToast(getString(R.string.select_only_one_option_pdf_or_gallery))
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            if (data.clipData != null) {
                val count = data.clipData!!.itemCount
                if (imagesUris.size > 5) {
                    showToast(getString(R.string.you_can_also_choose))
                } else {
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        imagesUris.add(imageUri)
                        imagesAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        } else {
            //Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }

    private fun convertUriToBase64Image(filesUris: ArrayList<Uri>): ArrayList<String> {
        val base64FileList = ArrayList<String>()
        for (uri in filesUris) {
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()

                if (bytes != null) {
                    val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
                    base64FileList.add(base64String)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return base64FileList
    }

    private fun initViewModel() {
        val repository = HomeVisitServicesRepository()
        val factory = ViewModelFactory { HomeServicesBookingViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HomeServicesBookingViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    showToast(result.data.message)
                    finishAffinity()
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun bookHomeServices() {
        val userDetails = getUserDetails()
        val request = HomeServicesBookingRequest(
            getDate(),
            getReason(),
            convertUriToBase64Image(imagesUris),
            "",
            subCatId,
            getMobile(),
            "booking",
            userDetails[User.ID]!!.toInt(),
            getDob(),
            categoryId,
            getName(),
            getTime(),
            userDetails[User.AUTH_TOKEN]!!,
            getEmail(),
            "0"
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.bookHomeServices(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun getName() : String {
        return binding.nameEt.text.toString().trim()
    }

    private fun getMobile() : String {
        return binding.mobileEt.text.toString().trim()
    }

    private fun getEmail() : String {
        return binding.emailEt.text.toString().trim()
    }

    private fun getReason() : String {
        return binding.reasonEt.text.toString().trim()
    }

    private fun getDob() : String {
        return binding.dobTxt.text.toString().trim()
    }

    private fun getDegree() : String {
        return binding.degreeTxt.text.toString().trim()
    }

    private fun getDate() : String {
        return binding.dateTxt.text.toString().trim()
    }

    private fun getTime() : String {
        return binding.timeTxt.text.toString().trim()
    }
}