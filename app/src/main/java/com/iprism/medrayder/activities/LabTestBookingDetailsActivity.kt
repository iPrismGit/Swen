package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.PatientDetailsAdapter
import com.iprism.medrayder.adapters.TestsAdapter
import com.iprism.medrayder.databinding.ActivityLabTestBookingDetailsBinding
import com.iprism.medrayder.databinding.TestsBottomSheetBinding
import com.iprism.medrayder.models.diagnostictests.TestsItem
import com.iprism.medrayder.models.labtestbookingdetails.FamilyMembersItem
import com.iprism.medrayder.models.labtestbookingdetails.LabTestBookingDetailsRequest
import com.iprism.medrayder.models.labtestbookingdetails.ReportsItem
import com.iprism.medrayder.models.labtestbookingdetails.Response
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.utils.DRY
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.LabTestsBookingsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class LabTestBookingDetailsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLabTestBookingDetailsBinding
    private lateinit var viewModel: LabTestsBookingsViewModel
    private var bookingId = ""
    private var bookingType = ""
    private var image = ""
    private var tests : List<TestsItem>? = null
    private var reports : ArrayList<ReportsItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLabTestBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("id")) {
            bookingId = intent.getStringExtra("id")!!
            bookingType = intent.getStringExtra("bookingType")!!
        }
        handleBack()
        handleViewPrescriptionLL()
        initViewModel()
        observeResponse()
        fetchBookingDetails()
        handleNumberOfTestsTxt()
        handlePrescriptionLL()
        handleNeedHelp()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleNeedHelp() {
        binding.needHelpTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        })
    }

    private fun handleNumberOfTestsTxt() {
        binding.numberOfTestsTxt.setOnClickListener(View.OnClickListener {
            showTestsSheet(tests!!)
        })
    }

    private fun handleViewPrescriptionLL() {
        binding.viewPrescriptionLl.setOnClickListener(View.OnClickListener {
            if (!reports!![0].image.endsWith(".pdf")) {
                val intent = Intent(this, ViewDocumentsActivity::class.java)
                val imageList = ArrayList<String>()
                for (item in reports!!) {
                    imageList.add(item.image)
                }
                intent.putStringArrayListExtra("images", imageList)
                intent.putExtra("name", getString(R.string.reports))
                startActivity(intent)
            } else {
                val intent = Intent(this, PdfListActivity::class.java)
                intent.putExtra("reports", reports!!)
                startActivity(intent)
            }
        })
    }

    private fun handlePrescriptionLL() {
        binding.prescriptionLl.setOnClickListener(View.OnClickListener {
            if (!image.endsWith(".pdf")) {
                val intent = Intent(this, ViewDocumentsActivity::class.java)
                val imageList = ArrayList<String>()
                imageList.add(image)
                intent.putStringArrayListExtra("images", imageList)
                intent.putExtra("name", getString(R.string.prescription))
                startActivity(intent)
            } else {
                val intent = Intent(this, PdfViewActivity::class.java)
                intent.putExtra("pdfUrl", image)
                intent.putExtra("name", getString(R.string.prescription))
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = LabsRepository()
        val factory = ViewModelFactory { LabTestsBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[LabTestsBookingsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.bookingDetails.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    showDetails(result.data.response)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    showToast(result.message)
                }
            }
        }
    }

    private fun fetchBookingDetails() {
        val userDetails = getUserDetails()
        val request = LabTestBookingDetailsRequest(bookingId, userDetails[User.ID]!!.toInt(), bookingType, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchLabTestBookingDetails(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun setupPatientDetails(patients: List<FamilyMembersItem>) {
        val patientDetailsAdapter = PatientDetailsAdapter(patients)
        binding.patientDetailsRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.patientDetailsRv.adapter = patientDetailsAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun showDetails(details : Response) {
        binding.scroolView.visibility = View.VISIBLE
        binding.bookingIdTxt.text = details.bookingId
        binding.labNameTxt.text = details.labName
        binding.locationTxt.text = listOf(
            details.address.hno,
            details.address.buildingNo,
            details.address.landmark,
            details.address.address
        ).filter { !it.isNullOrBlank() }
            .joinToString(", ")
        if (details.bookingType.equals("product_booking", true)) {
            tests = details.packages[0].tests
            binding.slotDetailsTxt.visibility = View.VISIBLE
            binding.itemTotalTxt.text = "₹${details.fee}"
            //binding.totalDiscountTxt.text = "₹${details.couponDiscount}"
            binding.totalDiscountTxt.text = "${details.flatDiscount}%"
            binding.totalAmountTxt.text = "₹${details.consultationFee}"
            if (details.paymentType.equals("online", true)) {
                binding.paymentModeTxt.text = getString(R.string.online)
            } else {
                binding.paymentModeTxt.text = getString(R.string.wallet)
            }
            binding.slotDateTimeTxt.text = "${details.date}, ${details.time}"
            binding.labNameTxt1.text = details.name
            binding.labTestNameTxt.text = details.name
            binding.numberOfTestsTxt.text = "${getString(R.string.includes)} ${details.packages[0].tests.size} ${getString(R.string.tests)}"
            if (details.fasting.equals("yes", true)) {
                binding.fastingTxt.text = getString(R.string.fasting_required)
            } else {
                binding.fastingTxt.text = getString(R.string.fasting_not_required)
            }
        } else {
            binding.slotDetailsTxt.visibility = View.GONE
            binding.billSummaryLl.visibility = View.GONE
            binding.fastingTxt.visibility = View.GONE
            binding.labTestBookingLl.visibility = View.GONE
            binding.labTestNameTxt.text = getString(R.string.prescription_order)
            binding.slotDateTimeTxt.text = getString(R.string.prescription_order)
        }
        if (details.logo.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + details.logo)
                .into(binding.labImg)
        }
        if (details.testStatus.equals("report_sent", true)) {
            DRY.updateOrderStatus(
                this,
                 "3",
                binding.stepPlaced,
                binding.stepProcessed,
                binding.stepCompleted,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.labelCompleted,
                binding.progressLine,
                binding.progressLine2
            )
            binding.viewPrescriptionLl.visibility = View.VISIBLE
            reports = details.reports
        } else if (details.testStatus.equals("sample_collected", true)) {
            DRY.updateOrderStatus(
                this,
                "2",
                binding.stepPlaced,
                binding.stepProcessed,
                binding.stepCompleted,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.labelCompleted,
                binding.progressLine,
                binding.progressLine2
            )
        } else {
            DRY.updateOrderStatus(
                this,
                "1", // or dynamic from API
                binding.stepPlaced,
                binding.stepProcessed,
                binding.stepCompleted,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.labelCompleted,
                binding.progressLine,
                binding.progressLine2
            )
        }
        setupPatientDetails(details.familyMembers)
        if (details.image.isEmpty()) {
            binding.prescriptionLl.visibility = View.GONE
        } else {
            binding.prescriptionLl.visibility = View.VISIBLE
            binding.prescriptionImgTxt.text = details.image
            image = details.image
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showTestsSheet(tests : List<TestsItem>) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val testsBottomSheetBinding = TestsBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog.setContentView(testsBottomSheetBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        testsBottomSheetBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        testsBottomSheetBinding.numberOfTestsTxt.text = "${getString(R.string.contains)} ${tests.size} ${getString(R.string.tests)}"
        val testsAdapter = TestsAdapter(tests)
        Log.d("tests", tests.toString())
        testsBottomSheetBinding.testsRv.layoutManager = LinearLayoutManager(this)
        testsBottomSheetBinding.testsRv.adapter = testsAdapter
        bottomSheetDialog.show()
    }
}