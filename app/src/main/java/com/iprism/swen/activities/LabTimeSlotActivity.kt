package com.iprism.swen.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.swen.R
import com.iprism.swen.adapters.PatientPricesAdapter
import com.iprism.swen.adapters.SelectDatesAdapter
import com.iprism.swen.adapters.SelectTimesAdapter
import com.iprism.swen.databinding.ActivityDiagnosticTimeSlotBinding
import com.iprism.swen.databinding.GetImageBottomSheetBinding
import com.iprism.swen.databinding.LabTestSelectMembersBottomSheetBinding
import com.iprism.swen.interfaces.OnDateItemClickListener
import com.iprism.swen.interfaces.OnPatientPriceItemClickListener
import com.iprism.swen.interfaces.OnTimeItemClickListener
import com.iprism.swen.models.diagnostictimings.Price
import com.iprism.swen.models.labtestslots.LabTestSlotsApiResponse
import com.iprism.swen.models.labtestslots.LabTestSlotsRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.repository.LabsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.LabTimeSlotViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class LabTimeSlotActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDiagnosticTimeSlotBinding
    private lateinit var viewModel: LabTimeSlotViewModel
    private var date : String = ""
    private var convertDate : String = ""
    private var time : TimesItem? = null
    private var labId = ""
    private var testId = ""
    private var addressId = ""
    private var familyMembers : ArrayList<FamilyMembersItem>? = null
    private var prices : ArrayList<Price>? = null
    private var slotsApiResponse: LabTestSlotsApiResponse? = null
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var launchSomeActivity: ActivityResultLauncher<Intent>? = null
    private var takePictureIntent: ActivityResultLauncher<Intent>? = null
    private var imageUri: Uri? = null
    private var bitmap: Bitmap? = null
    private val REQUEST_CAMERA_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnosticTimeSlotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("labId")) {
            labId = intent.getStringExtra("labId")!!
            testId = intent.getStringExtra("testId")!!
        }
        handleContinueBookingBtn()
        handleBack()
        handleChangeTxt()
        handlePrescriptionLl()
        createLaunchSomeActivity()
        createCameraLaunchSomeActivity()
        initViewModel()
        observeResponse()
        val userDetails = getUserDetails()
        val request = LabTestSlotsRequest("", "", userDetails[User.ID]!!.toInt(), "0", 0, "dates", userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), labId.toInt(), testId.toInt(), "", "0", "0")
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getDates(req)
        }
        Log.d("request", request.toString())
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                    viewModel.getDates(req)
                }
                Log.d("request", request.toString())
            }
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBookingBtn() {
        binding.continueBookingBtn.setOnClickListener(View.OnClickListener {
            if (date.isEmpty()) {
                showToast(getString(R.string.please_select_date))
            } else if (time == null) {
                showToast(getString(R.string.please_select_time))
            } else {
                showPricesSheet(this, slotsApiResponse!!.response.prices)
            }
        })
    }

    private fun setUpSelectDates(dates : List<DatesItem>) {
        val selectDatesAdapter = SelectDatesAdapter(dates)
        binding.selectDatesRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.selectDatesRv.adapter = selectDatesAdapter
        selectDatesAdapter.setOnDoctorItemClickListener(object : OnDateItemClickListener {
            override fun onItemClicked(date : DatesItem) {
                this@LabTimeSlotActivity.date = date.date
                this@LabTimeSlotActivity.convertDate = date.convertDate
                this@LabTimeSlotActivity.time = null
                observeResponse1()
                val userDetails = getUserDetails()
                val request = LabTestSlotsRequest(date.date, "", userDetails[User.ID]!!.toInt(), "0", 0, "slots", userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), labId.toInt(), testId.toInt(), "", "0", "0")
                NetworkRetryHelper.checkAndCallWithRetry(this@LabTimeSlotActivity, request) { req ->
                    viewModel.getSlots(req)
                }
                Log.d("request", request.toString())
            }
        })
    }

    private fun setUpSelectMorningTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.morningTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.morningTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener {
            override fun onItemClicked(time: TimesItem) {
                this@LabTimeSlotActivity.time = time
                setUpSelectEveningTimes(slotsApiResponse!!.response.slots.evening)
                setUpSelectAfternoonTimes(slotsApiResponse!!.response.slots.afternoon)
            }
        })
    }

    private fun setUpSelectAfternoonTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.afternoonTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.afternoonTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener {
            override fun onItemClicked(time: TimesItem) {
                this@LabTimeSlotActivity.time = time
                setUpSelectMorningTimes(slotsApiResponse!!.response.slots.morning)
                setUpSelectEveningTimes(slotsApiResponse!!.response.slots.evening)
            }
        })
    }

    private fun setUpSelectEveningTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.eveningTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.eveningTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener {
            override fun onItemClicked(time: TimesItem) {
                this@LabTimeSlotActivity.time = time
                setUpSelectMorningTimes(slotsApiResponse!!.response.slots.morning)
                setUpSelectAfternoonTimes(slotsApiResponse!!.response.slots.afternoon)
            }
        })
    }

    private fun handleChangeTxt() {
        binding.changeTxt.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            launcher.launch(intent)
        })
    }

    private fun initViewModel() {
        val repository = LabsRepository()
        val factory = ViewModelFactory { LabTimeSlotViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[LabTimeSlotViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.addressLl.visibility = View.VISIBLE
                    addressId = result.data.response.address.id.toString()
                    binding.addressTypeTxt.text = result.data.response.address.addressType
                    binding.locationTxt.text = listOf(
                        result.data.response.address.hno,
                        result.data.response.address.buildingNo,
                        result.data.response.address.landmark,
                        result.data.response.address.address
                    ).filter { !it.isNullOrBlank() }
                        .joinToString(", ")
                    if (result.data.response.dates.isNotEmpty()) {
                        setUpSelectDates(result.data.response.dates)
                        binding.datesNoTxt.visibility = View.GONE
                    } else {
                        binding.datesNoTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeResponse1() {
        viewModel.response1.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.slotsLl.visibility = View.VISIBLE
                    slotsApiResponse = result.data
                    familyMembers = result.data.response.familyMembers
                    prices = result.data.response.prices
                    binding.morningTimesRv.adapter = null
                    binding.afternoonTimesRv.adapter = null
                    binding.eveningTimesRv.adapter = null
                    if (result.data.response.slots.morning.isNotEmpty()){
                        binding.morningNoSlotsTxt.visibility = View.GONE
                        setUpSelectMorningTimes(result.data.response.slots.morning)
                    } else {
                        binding.morningNoSlotsTxt.visibility = View.VISIBLE
                    }
                    if (result.data.response.slots.afternoon.isNotEmpty()){
                        binding.afternoonNoSlotsTxt.visibility = View.GONE
                        setUpSelectAfternoonTimes(result.data.response.slots.afternoon)
                    } else {
                        binding.afternoonNoSlotsTxt.visibility = View.VISIBLE
                    }
                    if (result.data.response.slots.evening.isNotEmpty()){
                        binding.eveningNoSlotsTxt.visibility = View.GONE
                        setUpSelectEveningTimes(result.data.response.slots.evening)
                    } else {
                        binding.eveningNoSlotsTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun showPricesSheet(context: Context, prices : ArrayList<Price>) {
        var priceDetails : Price? = null
        val bottomSheetDialog = BottomSheetDialog(context)
        val bindingBs = LabTestSelectMembersBottomSheetBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(bindingBs.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        val patientPricesAdapter = PatientPricesAdapter(prices)
        bindingBs.patientPricesRv.layoutManager = LinearLayoutManager(this)
        bindingBs.patientPricesRv.adapter = patientPricesAdapter
        patientPricesAdapter.setOnPatientPriceItemClickListener(object : OnPatientPriceItemClickListener{
            override fun onItemClicked(price: Price) {
                priceDetails = price
            }
        })
        bindingBs.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        bindingBs.btnContinueBooking.setOnClickListener(View.OnClickListener {
            if (priceDetails == null) {
                showToast(getString(R.string.please_select_number_of_patients))
            } else {
                val intent = Intent(this, AddMultiplePatientsActivity::class.java)
                intent.putExtra("tag", "lab")
                intent.putExtra("labId", labId)
                intent.putExtra("familyMembers", familyMembers)
                intent.putExtra("price", priceDetails)
                intent.putExtra("testId", testId)
                intent.putExtra("date", date)
                intent.putExtra("convertDate", convertDate)
                intent.putExtra("time", time)
                intent.putExtra("addressId", addressId)
                intent.putExtra("imageUri", imageUri.toString())
                startActivity(intent)
            }
        })
        bottomSheetDialog.show()
    }

    @SuppressLint("IntentReset")
    private fun showPrescriptionBs(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val sheetBinding = GetImageBottomSheetBinding.inflate(
            LayoutInflater.from(context))
        bottomSheetDialog.setContentView(sheetBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        sheetBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        sheetBinding.cameraLl.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
            if (checkPermissions()) {
                launchCameraIntent()
            } else {
                requestPermissions()
            }
        })
        sheetBinding.galleryLl.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickPhoto.type = "image/*"
            launchSomeActivity!!.launch(pickPhoto)
        })
        bottomSheetDialog.show()
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
                    binding.imageTxt.text = imageUri.toString()
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
                    imageUri = getImageUri(this, bitmap!!)
                    binding.imageTxt.text = imageUri.toString()
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

    private fun handlePrescriptionLl() {
        binding.prescriptionLl.setOnClickListener(View.OnClickListener {
            showPrescriptionBs(this)
        })
    }
}