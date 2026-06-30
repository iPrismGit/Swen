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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.R
import com.iprism.swen.adapters.ImagesAdapter
import com.iprism.swen.databinding.ActivitySurgeryQuoteBinding
import com.iprism.swen.interfaces.OnImageDeleteActionListener
import com.iprism.swen.models.LeadPaymentType
import com.iprism.swen.models.insertsurgicalquote.InsertSurgicalQuoteRequest
import com.iprism.swen.repository.SurgicalQuoteRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.setEnabledState
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.InsertSurgicalQuoteViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import java.io.IOException
import kotlin.text.toInt
import kotlin.toString

class SurgeryQuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySurgeryQuoteBinding
    private lateinit var viewModel : InsertSurgicalQuoteViewModel

    private var catId = ""
    private var imagesAdapter: ImagesAdapter? = null
    private var imageUri: Uri? = null
    private var PICK_IMAGE_MULTIPLE = 1
    private var imagesUris: ArrayList<Uri> = ArrayList()
    private var bitmap : Bitmap? = null
    private var paymentTypeId = ""
    private var insuranceType = ""
    private var paymentTypeName = ""
    private var isInsuranceRgListenerEnabled = true
    private var isOthersRgListenerEnabled = true
    val paymentTypes = listOf(
        LeadPaymentType("-1", "", "Select Treatment Status"),
        LeadPaymentType("1", "cash", "Cash"),
        LeadPaymentType("2", "health_insurance", "Health Insurance"),
        LeadPaymentType("3", "others", "Others")
    )
    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Handle the selected PDF file here
            handlePdfFile(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySurgeryQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("catId")) {
            catId = intent.getStringExtra("catId")!!
            Log.d("catId", catId.toString())
            binding.catNameTxt.text = intent.getStringExtra("catName")!!
        }
        handleBack()
        handleConfirmBtn()
        initViewModel()
        observeResponse()
        handlePdfLL()
        handleGalleryLL()
        setupImagesAdapter()
        setupImagesRv()
        setupPaymentTypesAdapter(paymentTypes)
        binding.insuranceCompaniesRg.setOnCheckedChangeListener { _, checkedId ->
            if (!isInsuranceRgListenerEnabled) return@setOnCheckedChangeListener
            when (checkedId) {
                R.id.personal_insurance_rb -> {
                    insuranceType = "personal_insurance"
                }

                R.id.company_insurance_rb -> {
                    insuranceType = "company_insurance"
                }
            }
        }
        binding.othersRg.setOnCheckedChangeListener { _, checkedId ->
            if (!isOthersRgListenerEnabled) return@setOnCheckedChangeListener
            when (checkedId) {
                R.id.state_govt_rb -> {
                    insuranceType = "state_govt"
                }

                R.id.central_govt_rb -> {
                    insuranceType = "central_govt"
                }

                R.id.arogyabhadratha_rb -> {
                    insuranceType = "arogyabhadratha"
                }

                R.id.arogyasree_rb -> {
                    insuranceType = "arogyasree"
                }

                R.id.ayushmanbhava_rb -> {
                    insuranceType = "ayushmanbhava"
                }

                R.id.others_rb -> {
                    insuranceType = "others"
                }
            }

        }
    }

    private fun handleConfirmBtn() {
        binding.confirmBtn.setOnClickListener { view ->
            if (getName().isEmpty()) {
                showToast("Please Enter Name")
            } else if (getName().length < 3) {
                showToast(getString(R.string.pls_enter_3_chars))
            } else if (getAge().isEmpty()) {
                showToast("Please Enter Age")
            } else if (getAge().toInt() < 1) {
                showToast("Patient Age Should not be 0")
            } else if (getHealthIssue().isEmpty()) {
                showToast("Please Enter health Issue")
            } else if (getSurgeryName().isEmpty()) {
                showToast("Please Enter Surgery Name")
            } else if (paymentTypeId.equals("-1", true)) {
                showToast("Please Select Treatment Status!")
            } else if (paymentTypeId.equals("2", true) && insuranceType.isEmpty()) {
                showToast("Please Select  Insurance Type!")
            } else if (paymentTypeId.equals("2", true) && getInsuranceCompanyName().isEmpty()) {
                showToast("Please Enter Insurance Company Name!")
            } else if (paymentTypeId.equals("2", true) && getTpaName().isEmpty()) {
                showToast("Please Enter TPA Name!")
            } else if (paymentTypeId.equals("2", true) && getNoOfPersonsCovered().isEmpty()) {
                showToast("Please Enter No.of Persons Covered!")
            } else if (paymentTypeId.equals("2", true) && getNoOfPersonsCovered().toInt() < 1) {
                showToast("No. of Persons Covered Should not be Zero")
            } else if (paymentTypeId.equals("3", true) && insuranceType.isEmpty()) {
                showToast("Please Select  Others Type!")
            } else if (imagesUris.isEmpty()) {
                showToast("Please Select Documents")
            } else {
                insertSurgicalQuote()
            }
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun getName() : String {
        return binding.nameEt.text.toString()
    }

    private fun getAge() : String {
        return binding.ageEt.text.toString()
    }

    private fun getHealthIssue() : String {
        return binding.healthIssueEt.text.toString()
    }

    private fun getSurgeryName() : String {
        return binding.surgeryNameEt.text.toString()
    }

    private fun initViewModel() {
        val repository = SurgicalQuoteRepository()
        val factory = ViewModelFactory { InsertSurgicalQuoteViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[InsertSurgicalQuoteViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.confirmBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    val intent = Intent(this@SurgeryQuoteActivity, SuccessActivity::class.java)
                    intent.putExtra("tag", "Surgical Quote Inserted")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.confirmBtn.setEnabledState(true)
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun insertSurgicalQuote() {
        binding.confirmBtn.setEnabledState(false)
        val userDetails = getUserDetails()
        val request = InsertSurgicalQuoteRequest(
            getSurgeryName(),
            convertUriToBase64Image(imagesUris).toString().replace("[", "").replace("]", "")
                .replace(" ", ""),
            getHealthIssue(),
            userDetails[User.ID]!!.toInt(),
            catId.toInt(),
            getName(),
            userDetails[User.AUTH_TOKEN].toString(),
            Constants.MAIN_DATA_ID,
            getAge(),
            paymentTypeName,
            insuranceType,
            getInsuranceCompanyName(),
            getTpaName(),
            getNoOfPersonsCovered()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.insertSurgicalQuote(req)
        }
        Log.d("requestLoading", request.toString())
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

    private fun setupPaymentTypesAdapter(paymentTypes: List<LeadPaymentType>) {
        val namesList = paymentTypes.map { it.formattedName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentsSp.adapter = adapter
        binding.paymentsSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    paymentTypeId = paymentTypes[position].id.toString()
                    paymentTypeName = paymentTypes[position].name.toString()

                    when {
                        paymentTypeName.equals("cash", true) -> {
                            binding.insuranceLo.visibility = View.GONE
                            binding.othersLo.visibility = View.GONE
                            insuranceType = "cash"

                            // Disable listeners
                            isInsuranceRgListenerEnabled = false
                            isOthersRgListenerEnabled = false

                            binding.insuranceCompaniesRg.clearCheck()
                            binding.othersRg.clearCheck()

                            // Re-enable listeners
                            isInsuranceRgListenerEnabled = true
                            isOthersRgListenerEnabled = true
                        }

                        paymentTypeName.equals("health_insurance", true) -> {
                            binding.insuranceLo.visibility = View.VISIBLE
                            binding.othersLo.visibility = View.GONE
                            insuranceType = ""

                            isOthersRgListenerEnabled = false
                            binding.othersRg.clearCheck()
                            isOthersRgListenerEnabled = true
                        }

                        paymentTypeName.equals("others", true) -> {
                            binding.insuranceLo.visibility = View.GONE
                            binding.othersLo.visibility = View.VISIBLE
                            insuranceType = ""

                            isInsuranceRgListenerEnabled = false
                            binding.insuranceCompaniesRg.clearCheck()
                            isInsuranceRgListenerEnabled = true
                        }
                    }

                    Log.d("PaymentType", "$paymentTypeId, $paymentTypeName, $insuranceType")
                }


                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun getInsuranceCompanyName(): String {
        return binding.insuranceCompanyNameTxt.text.toString().trim()
    }

    private fun getTpaName(): String {
        return binding.tpaNameTxt.text.toString().trim()
    }

    private fun getNoOfPersonsCovered(): String {
        return binding.noPersonsCoveredTxt.text.toString().trim()
    }
}