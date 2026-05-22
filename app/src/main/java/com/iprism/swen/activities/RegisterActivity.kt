package com.iprism.swen.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.iprism.swen.R
import com.iprism.swen.adapters.SpinnerItemsAdapter
import com.iprism.swen.databinding.ActivityRegisterBinding
import com.iprism.swen.fragments.AddFamilyMembersBs
import com.iprism.swen.models.register.RegisterRequest
import com.iprism.swen.models.userdropdowns.CategoriesItem
import com.iprism.swen.models.userdropdowns.UserDropDownRequest
import com.iprism.swen.repository.AuthRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.setEnabledState
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.RegisterViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private var launchSomeActivity: ActivityResultLauncher<Intent>? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100
    private var profileUri: Uri? = null
    private var bitmap: Bitmap? = null
    private var gender: String? = ""
    private var genderId: String? = ""
    private var bloodGroupId: String? = ""
    private var coverageCategoriesId: String? = "0"
    private var userId: String = ""
    private var mobile: String = ""

    private val cropImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val croppedUri = result.data?.let { UCrop.getOutput(it) }
            croppedUri?.let { setProfileImage(it) }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = result.data?.let { UCrop.getError(it) }
            cropError?.printStackTrace()
        }
    }

    private fun setProfileImage(uri: Uri) {
        profileUri = uri
        binding.profileImg.visibility = View.VISIBLE
        Glide.with(this)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.NONE) // No disk caching
            .skipMemoryCache(true) // No memory caching
            .circleCrop() // Ensures circular cropping
            .into(binding.profileImg) // Load image into ImageView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId")!!
            mobile = intent.getStringExtra("mobile")!!
            //binding.addFamilyBtn.visibility = View.VISIBLE
            binding.mobileEt.setText(mobile)
        }
        handleBack()
        handleContinueBtn()
        handleSkipBtn()
        handleDateLL()
        handleUploadImg()
        createLaunchSomeActivity()
        initViewModel()
        observeResponse()
        observeRegisterResponse()
        getUserDropDowns()
    }

    private fun createLaunchSomeActivity() {
        launchSomeActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data
                    val imageUri = result.data?.data // Get the selected image URI
                    imageUri?.let { startCrop(it) }
                    /* profileUri = data!!.data
                     if (profileUri != null) {
                         profileUri = data.data
                         binding.profileImg.visibility = View.VISIBLE
                         binding.profileImg.setImageURI(profileUri)
                         val imageStream: InputStream?
                         try {
                             imageStream = contentResolver.openInputStream(profileUri!!)
                             val selectedImage = BitmapFactory.decodeStream(imageStream)
                             //String encodedImage = encodeImage(selectedImage);
                         } catch (e: FileNotFoundException) {
                             e.printStackTrace()
                         }
                     }*/
            }
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleUploadImg() {
        binding.updateProfileImg.setOnClickListener(View.OnClickListener {
            selectImage()
        })
    }

    private fun handleSkipBtn() {
        binding.addFamilyBtn.setOnClickListener(View.OnClickListener {
            //startActivity(Intent(this, HomeActivity::class.java))
        })
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (profileUri == null) {
                showToast(getString(R.string.please_choose_profile_image))
            } else if (getName().isEmpty()) {
                showToast(getString(R.string.please_enter_name))
            } else if (getName().length < 3) {
                showToast(getString(R.string.pls_enter_3_chars))
            } else if (getMobile().length != 10) {
                showToast(getString(R.string.please_enter_10_digit_mobile))
            } else if (Pattern.matches("[0-5].*", getMobile())) {
                showToast(getString(R.string.please_enter_valid_mobile))
            } else if (getEmail().isEmpty()) {
                showToast(getString(R.string.please_enter_email))
            } else if (!isValidGmail(getEmail())) {
                showToast(getString(R.string.please_enter_valid_email))
            } else if (gender!!.isEmpty()) {
                showToast(getString(R.string.please_choose_gender))
            } else if (getDob().isEmpty()) {
                showToast(getString(R.string.please_enter_dob))
            } else if (bloodGroupId!!.isEmpty()) {
                showToast(getString(R.string.please_choose_blood_group))
            } else {
               registerUser()
            }
        })
    }

    private fun isValidGmail(email: String): Boolean {
        val gmailRegex = "^[a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._%+-]*[a-zA-Z0-9]@gmail\\.com$"
        return Regex(gmailRegex).matches(email)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleDateLL() {
        binding.dateLl.setOnClickListener(View.OnClickListener {
            getDate(binding.dobTxt)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun getDate(dateTxt: TextView) {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(dateTxt.context, { view, year, monthOfYear, dayOfMonth ->
            dateTxt.text = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = c.timeInMillis
        datePickerDialog.show()
    }

    @SuppressLint("IntentReset")
    private fun selectImage() {
        var options = arrayOf<CharSequence>()
        options = arrayOf<CharSequence>(getString(R.string.choose_from_gallery), getString(R.string.camera), getString(R.string.cancel))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.add_photo))
        builder.setItems(options) { dialog, item ->
            if (options[item] == getString(R.string.choose_from_gallery)) {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickPhoto.type = "image/*"
                launchSomeActivity!!.launch(pickPhoto)
            } else  if (options[item] == getString(R.string.camera)) {
                if (checkPermissions()) {
                    launchCameraIntent()
                } else {
                    requestPermissions()
                }
            } else {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
        val uCrop = UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f) // Optional: Set aspect ratio
            .withMaxResultSize(512, 512)  // Set max resolution
            .withOptions(getUCropOptions()) // Apply UI changes
        cropImageLauncher.launch(uCrop.getIntent(this))
    }

    private fun getUCropOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCircleDimmedLayer(true)           // Circular crop mask
        options.setShowCropGrid(false)               // Hide grid lines
        options.setShowCropFrame(false)              // Hide crop frame
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(90)            // Good balance
        options.setHideBottomControls(true)          // Clean UI
        options.setFreeStyleCropEnabled(false)       // Lock aspect ratio
        return options
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
            // Now you have the image in 'bitmap'
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap!!.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                byteArrayOutputStream
            ) // You can choose a different format if needed
            val imageBytes = byteArrayOutputStream.toByteArray()
            base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            // 'base64Image' now contains the Base64-encoded image
        }
        return base64Image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            val bitmap = data.extras!!["data"] as Bitmap?
            val baos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            //  Base64.Encoder encoder = Base64.getEncoder();
            val uri: Uri = getImageUri(this@RegisterActivity, bitmap)!!
            uri?.let { startCrop(it) }
            /*  profileUri = uri
              binding.profileImg.visibility = View.VISIBLE
              binding.profileImg.setImageURI(profileUri)*/
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
                    showToast("Permission Denied")
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun launchCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun getName() : String {
        return binding.nameTxt.text.toString().trim()
    }

    private fun getMobile() : String {
        return binding.mobileEt.text.toString().trim()
    }

    private fun getEmail() : String {
        return binding.emailTxt.text.toString().trim()
    }

    private fun getDob() : String {
        return binding.dobTxt.text.toString().trim()
    }

    private fun setupGenderSpinner(items: ArrayList<CategoriesItem>) {
        items.add(0, CategoriesItem(getString(R.string.choose_gender), 0))
        val adapter = SpinnerItemsAdapter(this, R.layout.spinner_item, items)
        binding.genderSpinner.adapter = adapter
        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    val item = adapter.getItem(position)
                    genderId = item!!.id.toString()
                    if (genderId.equals("1")) {
                        gender = "male"
                    } else if (genderId.equals("2")) {
                        gender = "female"
                    }
                } else {
                    gender = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setupCoverageCategoriesSpinner(items: ArrayList<CategoriesItem>) {
        items.add(0, CategoriesItem(getString(R.string.choose_coverage_category), 0))
        val adapter = SpinnerItemsAdapter(this, R.layout.spinner_item, items)
        binding.coverageCategoriesSpinner.adapter = adapter
        binding.coverageCategoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    val item = adapter.getItem(position)
                    coverageCategoriesId = item!!.id.toString()
                } else {
                    coverageCategoriesId = "0"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setupBloodGroupsSpinner(items: ArrayList<CategoriesItem>) {
        items.add(0, CategoriesItem(getString(R.string.choose_bg), 0))
        val adapter = SpinnerItemsAdapter(this, R.layout.spinner_item, items)
        binding.bloodGroupSpinner.adapter = adapter
        binding.bloodGroupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    val item = adapter.getItem(position)
                    bloodGroupId = item!!.id.toString()
                } else {
                    bloodGroupId = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun initViewModel() {
        val repository = AuthRepository()
        val factory = ViewModelFactory { RegisterViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    setupGenderSpinner(result.data.response.gender)
                    setupBloodGroupsSpinner(result.data.response.bloodGroups)
                    setupCoverageCategoriesSpinner(result.data.response.categories)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    showToast(result.message)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRegisterResponse() {
        viewModel.registerResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.continueBtn.setEnabledState(false)
                }

                is UiState.Success -> {
                    binding.continueBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    val user = User(this)
                    user.storeUserDetails(result.data.response.id.toString(), result.data.response.authToken, result.data.response.mobile)
                    user.loginUser()
                    val menuDialogFragment = AddFamilyMembersBs()
                    menuDialogFragment.show(supportFragmentManager, "")
                }

                is UiState.Error -> {
                    binding.continueBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    showToast(result.message)
                }
            }
        }
    }

    private fun getUserDropDowns() {
        val userDetails = getUserDetails()
        val request = UserDropDownRequest(userDetails[User.LANG].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchUserDropDowns(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun registerUser() {
        val request = RegisterRequest(convertUriToBase64Image(profileUri)!!, genderId!!, coverageCategoriesId!!.toInt(), userId.toInt(), getDob(), getName(), getMobile(), bloodGroupId!!.toInt(), getEmail(), "")
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.registerUser(req)
        }
        Log.d("requestLoading", request.toString())
    }
}