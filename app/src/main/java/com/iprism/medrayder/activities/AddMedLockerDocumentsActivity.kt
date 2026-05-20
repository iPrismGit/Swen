package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.ImagesAdapter
import com.iprism.medrayder.databinding.ActivityAddMedLockerDocumentsBinding
import com.iprism.medrayder.interfaces.OnImageDeleteActionListener
import com.iprism.medrayder.models.medlocker.MedLockerRequest
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.setEnabledState
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.MedLockerViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddMedLockerDocumentsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddMedLockerDocumentsBinding
    private lateinit var viewModel: MedLockerViewModel
    private var imagesAdapter: ImagesAdapter? = null
    private var imageUri: Uri? = null
    private var PICK_IMAGE_MULTIPLE = 1
    private var imagesUris: ArrayList<Uri> = ArrayList()
    private var bitmap : Bitmap? = null
    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Handle the selected PDF file here
            handlePdfFile(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMedLockerDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleUpload()
        initViewModel()
        observeResponse()
        handlePdfLL()
        handleGalleryLL()
        setupImagesAdapter()
        setupImagesRv()
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

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun getName() : String {
        return binding.nameEt.text.toString().trim()
    }

    private fun handleUpload() {
        binding.uploadBtn.setOnClickListener(View.OnClickListener {
            if (getName().isEmpty()) {
                showToast(getString(R.string.please_enter_name))
            } else if (imagesUris.size == 0) {
                showToast(getString(R.string.please_select_images))
            } else {
                addMedLocker()
            }
        })
    }

    private fun initViewModel() {
        val repository = CommonRepository()
        val factory = ViewModelFactory { MedLockerViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[MedLockerViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.uploadBtn.setEnabledState(false)
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.uploadBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    finish()
                }

                is UiState.Error -> {
                    binding.uploadBtn.setEnabledState(true)
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun addMedLocker() {
        val userDetails = getUserDetails()
        val request = MedLockerRequest(convertUriToBase64Image(imagesUris), userDetails[User.ID]!!.toInt(), getName(), "insert", "", userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.medLocker(req)
        }
        Log.d("requestLoading", request.toString())
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
}