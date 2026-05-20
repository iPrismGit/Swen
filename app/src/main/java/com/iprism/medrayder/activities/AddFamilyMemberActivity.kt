package com.iprism.medrayder.activities

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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.SpinnerItemsAdapter
import com.iprism.medrayder.databinding.ActivityAddFamilyMemberBinding
import com.iprism.medrayder.models.addfamilymember.AddFamilyMemberRequest
import com.iprism.medrayder.models.profile.ProfileItem
import com.iprism.medrayder.models.profile.ProfileRequest
import com.iprism.medrayder.models.userdropdowns.CategoriesItem
import com.iprism.medrayder.models.userdropdowns.Response
import com.iprism.medrayder.models.userdropdowns.UserDropDownRequest
import com.iprism.medrayder.repository.AuthRepository
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.setEnabledState
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.AddFamilyMemberViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.regex.Pattern

class AddFamilyMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFamilyMemberBinding
    private lateinit var viewModel: AddFamilyMemberViewModel
    private var launchSomeActivity: ActivityResultLauncher<Intent>? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100
    private var profileUri: Uri? = null
    private var bitmap: Bitmap? = null
    private var gender: String? = ""
    private var genderId: String? = ""
    private var bloodGroupId: String? = ""
    private var coverageCategoriesId: String? = "0"
    private var dropDowns: Response? = null
    private var profileResponse: List<ProfileItem>? = null

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
        binding = ActivityAddFamilyMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleFamilyBtn()
        handleContinueBtn()
        handleDateLL()
        handleUploadImg()
        createLaunchSomeActivity()
        initViewModel()
        observeResponse()
        observeRegisterResponse()
        observeFamilyMembersResponse()
        lifecycleScope.launch {
            getUserDropDowns()
            profileDetails()
        }
        handleStep1Txt()
        handleStep2Txt()
        handleStep3Txt()
        handleStep4Txt()
        handleStep5Txt()
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

    private fun handleStep1Txt() {
        binding.step1Txt.setOnClickListener(View.OnClickListener {
            binding.relationshipLl.visibility = View.GONE
            if (profileResponse!!.isNotEmpty()) {
                showDetails(profileResponse!![0])
            }
        })
    }

    private fun handleStep2Txt() {
        binding.step2Txt.setOnClickListener(View.OnClickListener {
            binding.relationshipLl.visibility = View.VISIBLE
            if (profileResponse!!.size >= 2) {
                showDetails(profileResponse!![1])
            } else {
                showDetails(null)
            }
        })
    }

    private fun handleStep3Txt() {
        binding.step3Txt.setOnClickListener(View.OnClickListener {
            binding.relationshipLl.visibility = View.VISIBLE
            if (profileResponse!!.size >= 3) {
                showDetails(profileResponse!![2])
            } else {
                showDetails(null)
            }
        })
    }

    private fun handleStep4Txt() {
        binding.step4Txt.setOnClickListener(View.OnClickListener {
            binding.relationshipLl.visibility = View.VISIBLE
            if (profileResponse!!.size >= 4) {
                showDetails(profileResponse!![3])
            } else {
                showDetails(null)
            }
        })
    }

    private fun handleStep5Txt() {
        binding.step5Txt.setOnClickListener(View.OnClickListener {
            binding.relationshipLl.visibility = View.VISIBLE
            if (profileResponse!!.size >= 5) {
                showDetails(profileResponse!![4])
            } else {
                showDetails(null)
            }
        })
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            /*startActivity(Intent(this, SubscriptionActivity::class.java))*/
            val intent = Intent(this, AddressActivity::class.java)
            intent.putExtra("tag", "register")
            startActivity(intent)
        })
    }

    private fun handleFamilyBtn() {
        binding.addFamilyBtn.setOnClickListener(View.OnClickListener {
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
            } else if (getRelationship().isEmpty()) {
                showToast(getString(R.string.enter_relationship))
            } else {
               addFamilyMember()
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
            val uri: Uri = getImageUri(this@AddFamilyMemberActivity, bitmap)!!
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

    private fun getRelationship() : String {
        return binding.relationshipEt.text.toString().trim()
    }

    private fun setupGenderSpinner(items: ArrayList<CategoriesItem>) {
        if (items.isEmpty() || items[0].id != 0) {
            items.add(0, CategoriesItem(getString(R.string.choose_gender), 0))
        }
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
        if (genderId!!.isNotEmpty()) {
            var position : Int = 0
            for (i in 0 until items.size) {
                if (genderId!!.toInt() == items[i].id) {
                    position = i
                }
            }
            binding.genderSpinner.setSelection(position)
        }
    }

    private fun setupCoverageCategoriesSpinner(items: ArrayList<CategoriesItem>) {
        if (items.isEmpty() || items[0].id != 0) {
            items.add(0, CategoriesItem(getString(R.string.choose_coverage_category), 0))
        }
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
        if (coverageCategoriesId!!.isNotEmpty()) {
            var position : Int = 0
            for (i in 0 until items.size) {
                if (coverageCategoriesId!!.toInt() == items[i].id) {
                    position = i
                }
            }
            binding.coverageCategoriesSpinner.setSelection(position)
        }
    }

    private fun setupBloodGroupsSpinner(items: ArrayList<CategoriesItem>) {
        if (items.isEmpty() || items[0].id != 0) {
            items.add(0, CategoriesItem(getString(R.string.choose_bg), 0))
        }
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
        if (bloodGroupId!!.isNotEmpty()) {
            var position : Int = 0
            for (i in 0 until items.size) {
                if (bloodGroupId!!.toInt() == items[i].id) {
                    position = i
                }
            }
            binding.bloodGroupSpinner.setSelection(position)
        }
    }

    private fun initViewModel() {
        val repository = AuthRepository()
        val factory = ViewModelFactory { AddFamilyMemberViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AddFamilyMemberViewModel::class.java]
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
                    dropDowns = result.data.response
                    setupGenderSpinner(dropDowns!!.gender)
                    setupBloodGroupsSpinner(dropDowns!!.bloodGroups)
                    setupCoverageCategoriesSpinner(dropDowns!!.categories)
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
        viewModel.addFamilyResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.addFamilyBtn.setEnabledState(false)
                }

                is UiState.Success -> {
                    binding.addFamilyBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    showToast(getString(R.string.family_mem_added_suc))
                    profileDetails()
                    showDetails(null)
                }

                is UiState.Error -> {
                    binding.addFamilyBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    showToast(result.message)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeFamilyMembersResponse() {
        viewModel.profileResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    profileResponse = result.data.response.profile
                    highlightStep(result.data.response.count)
                    if (profileResponse!!.size >= 5) {
                        startActivity(Intent(this, SubscriptionActivity::class.java))
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    showToast(result.message)
                }
            }
        }
    }

    private fun showDetails(details: ProfileItem?) {
        if (details == null) {
            binding.nameTxt.setText("")
            binding.mobileEt.setText("")
            binding.emailTxt.setText("")
            binding.relationshipEt.setText("")
            binding.addFamilyBtn.visibility = View.VISIBLE
            binding.dobTxt.text = ""
            profileUri = null
            binding.genderSpinner.setSelection(0)
            binding.bloodGroupSpinner.setSelection(0)
            binding.coverageCategoriesSpinner.setSelection(0)
            binding.profileImg.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.profile_img
                )
            )
        } else {
            binding.nameTxt.setText(details.name)
            binding.relationshipEt.setText(details.relationship)
            binding.mobileEt.setText(details.mobile.toString())
            binding.emailTxt.setText(details.email)
            binding.addFamilyBtn.visibility = View.GONE
            binding.dobTxt.text = details.dob
            genderId = details.gender
            coverageCategoriesId = details.coverageCategory
            bloodGroupId = details.bloodGroup.toString()
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + details.image)
                .into(binding.profileImg)
            setupGenderSpinner(dropDowns!!.gender)
            setupBloodGroupsSpinner(dropDowns!!.bloodGroups)
            setupCoverageCategoriesSpinner(dropDowns!!.categories)
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

    private fun addFamilyMember() {
        val userDetails = getUserDetails()
        val request = AddFamilyMemberRequest(convertUriToBase64Image(profileUri)!!, genderId!!, coverageCategoriesId!!.toInt(), userDetails[User.ID]!!.toInt(), getDob(), getName(), getMobile(), bloodGroupId!!.toInt(), getEmail(), getRelationship())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.registerUser(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun profileDetails() {
        val userDetails = getUserDetails()
        val request = ProfileRequest(userDetails[User.ID]!!.toInt(), userDetails[User.LANG]!!, userDetails[User.AUTH_TOKEN]!!)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.profileDetails(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun highlightStep(activeStep: Int) {
        val steps = listOf(
            binding.step1Txt,
            binding.step2Txt,
            binding.step3Txt,
            binding.step4Txt,
            binding.step5Txt
        )

        val lines = listOf(
            binding.step1Line,
            binding.step2Line,
            binding.step3Line,
            binding.step4Line
        )

        for ((index, step) in steps.withIndex()) {
            if (index < activeStep) {
                step.background = ContextCompat.getDrawable(this, R.drawable.filled_bg)
                step.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            } else {
                step.background = ContextCompat.getDrawable(this, R.drawable.progress_bg)
                step.setTextColor(ContextCompat.getColor(this, R.color.black1))
            }
        }

        for ((index, line) in lines.withIndex()) {
            if (index < activeStep - 1) {
                line.setBackgroundColor(ContextCompat.getColor(this, R.color.blue)) // active blue
            } else {
                line.setBackgroundColor(ContextCompat.getColor(this, R.color.black1)) // default gray
            }
        }
    }
}