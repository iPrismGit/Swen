package com.iprism.swen.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityPharmacyPrescriptionBinding
import com.iprism.swen.databinding.ChooseOrderTypeBsBinding
import com.iprism.swen.utils.showToast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class PrescriptionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPharmacyPrescriptionBinding
    private var tag = ""
    private var labId = ""
    private var diagnosticId = ""
    private var pharmacyId = ""
    private var hospitalId = ""
    private var orderType = ""
    private var launchSomeActivity: ActivityResultLauncher<Intent>? = null
    private var takePictureIntent: ActivityResultLauncher<Intent>? = null
    private var imageUri: Uri? = null
    private var bitmap: Bitmap? = null
    private val REQUEST_CAMERA_PERMISSION = 100
    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Handle the selected PDF file here
            handlePdfFile(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacyPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
            if (tag.equals("labTest", true)) {
                labId = intent.getStringExtra("labId")!!
            } else if (tag.equals("diagnostic", true)) {
                diagnosticId = intent.getStringExtra("diagnosticId")!!
            } else if (tag.equals("pharmacy", true)) {
                pharmacyId = intent.getStringExtra("pharmacyId")!!
                orderType = intent.getStringExtra("orderType")!!
            } else if (tag.equals("hospitalPharmacy", true)) {
                hospitalId = intent.getStringExtra("hospitalId")!!
                orderType = intent.getStringExtra("orderType")!!
            } else if (tag.equals("hospitalDiagnostic", true)) {
                hospitalId = intent.getStringExtra("hospitalId")!!
            }
        }
        handleBack()
        handleContinueBtn()
        createLaunchSomeActivity()
        createCameraLaunchSomeActivity()
        handleGalleryLL()
        handleCameraLL()
        handleCrossImg()
        handlePdfLL()
        handlePdfCrossImg()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleCameraLL() {
        binding.cameraLl.setOnClickListener(View.OnClickListener {
                if (checkPermissions()) {
                    launchCameraIntent()
                } else {
                    requestPermissions()
                }
        })
    }

    @SuppressLint("IntentReset")
    private fun handleGalleryLL() {
        binding.galleryLl.setOnClickListener(View.OnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickPhoto.type = "image/*"
            launchSomeActivity!!.launch(pickPhoto)
        })
    }

    @SuppressLint("IntentReset")
    private fun handlePdfLL() {
        binding.pdfLl.setOnClickListener(View.OnClickListener {
                pdfPickerLauncher.launch("application/pdf")
        })
    }

    @SuppressLint("Range")
    private fun handlePdfFile(uri: Uri) {
        if (uri != null) {
            imageUri = uri
            binding.pdfViewLl.visibility = View.VISIBLE
            binding.imageLl.visibility = View.GONE
        }
        // Example: Get file name
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                Log.d("PDF_NAME", "Selected file: $displayName")
                binding.pdfNameTxt.text = displayName
            }
        }

        // You can also open the InputStream to read the content
        val inputStream = contentResolver.openInputStream(uri)
        // Now you can upload, read, or display the PDF
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (imageUri != null) {
                if (tag.equals("labTest", true)) {
                    val intent = Intent(this, AddLabTestPatientActivity::class.java)
                    intent.putExtra("tag", "labTest")
                    intent.putExtra("labId", labId)
                    intent.putExtra("imageUri", imageUri.toString())
                    startActivity(intent)
                } else if (tag.equals("diagnostic", true)) {
                    val intent = Intent(this, AddLabTestPatientActivity::class.java)
                    intent.putExtra("tag", "diagnostic")
                    intent.putExtra("diagnosticId", diagnosticId)
                    intent.putExtra("imageUri", imageUri.toString())
                    startActivity(intent)
                } else if (tag.equals("pharmacy", true)) {
                    if (orderType.equals("yes", true)) {
                        showOrderTypeBs(this)
                    } else {
                        navigateCart("pickup")
                    }
                } else if (tag.equals("hospitalDiagnostic", true)) {
                    val intent = Intent(this, AddLabTestPatientActivity::class.java)
                    intent.putExtra("tag", "hospitalDiagnostic")
                    intent.putExtra("hospitalId", hospitalId)
                    intent.putExtra("imageUri", imageUri.toString())
                    startActivity(intent)
                } else if (tag.equals("hospitalPharmacy", true)) {
                    if (orderType.equals("yes", true)) {
                        showOrderTypeBs(this)
                    } else {
                        navigateCart("pickup")
                    }
                }
            } else {
                showToast(getString(R.string.please_select_prescription))
            }
        })
    }

    private fun handleCrossImg() {
        binding.crossImg.setOnClickListener(View.OnClickListener {
            imageUri = null
            binding.imageLl.visibility = View.GONE
        })
    }

    private fun handlePdfCrossImg() {
        binding.pdfCrossImg.setOnClickListener(View.OnClickListener {
            imageUri = null
            binding.pdfViewLl.visibility = View.GONE
        })
    }


    private fun createLaunchSomeActivity() {
        launchSomeActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data
                imageUri = data!!.data
                if (imageUri != null) {
                    imageUri = data.data
                    binding.imageLl.visibility = View.VISIBLE
                    binding.pdfViewLl.visibility = View.GONE
                    binding.selectedImg.setImageURI(imageUri)
                    val imageStream: InputStream?
                    try {
                        imageStream = contentResolver.openInputStream(imageUri!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        //String encodedImage = encodeImage(selectedImage);
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun createCameraLaunchSomeActivity() {
        takePictureIntent = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val extras = result.data!!.extras
                val capturedImage = extras?.get("data") as? Bitmap
                if (capturedImage != null) {
                    bitmap = capturedImage
                    binding.imageLl.visibility = View.VISIBLE
                    binding.pdfViewLl.visibility = View.GONE
                    binding.selectedImg.setImageBitmap(bitmap)
                    imageUri = getImageUri(this, bitmap!!)
                }
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val file = File(inContext.cacheDir, "image.jpg")
        try {
            val out = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }

    private fun convertUriToBase64Image(imageUri: Uri?): String? {
        var base64Image: String? = ""
        if (imageUri == null) {
            return "".also { base64Image = it }
        }
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap!!.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                byteArrayOutputStream
            )
            val imageBytes = byteArrayOutputStream.toByteArray()
            base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            // 'base64Image' now contains the Base64-encoded image
        }
        return base64Image
    }

    private fun checkPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                storagePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CAMERA_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchCameraIntent()
                } else {
                    showToast(getString(R.string.permission_denied))
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun launchCameraIntent() {
        val pickPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent!!.launch(pickPhoto)
    }

    private fun showOrderTypeBs(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val chooseOrderTypeBsBinding = ChooseOrderTypeBsBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(chooseOrderTypeBsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        chooseOrderTypeBsBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        chooseOrderTypeBsBinding.pickUpOrderLl.setOnClickListener(View.OnClickListener {
            navigateCart("pickup")
            bottomSheetDialog.cancel()
        })
        chooseOrderTypeBsBinding.homeDeliveryLl.setOnClickListener(View.OnClickListener {
            navigateCart("home_delivery")
            bottomSheetDialog.cancel()
        })
        bottomSheetDialog.show()
    }

    private fun navigateCart(orderType : String) {
        if (tag.equals("hospitalPharmacy", true)) {
            val intent = Intent(this, HospitalPharmacyPrescriptionCartActivity::class.java)
            intent.putExtra("tag", "hospitalPharmacy")
            intent.putExtra("hospitalId", hospitalId)
            intent.putExtra("imageUri", imageUri.toString())
            intent.putExtra("orderType", orderType)
            startActivity(intent)
        } else {
            val intent = Intent(this, PharmacyPrescriptionCartActivity::class.java)
            intent.putExtra("tag", "pharmacy")
            intent.putExtra("pharmacyId", pharmacyId)
            intent.putExtra("imageUri", imageUri.toString())
            intent.putExtra("orderType", orderType)
            startActivity(intent)
        }
    }
}