package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.LabTestsAdapter
import com.iprism.medrayder.adapters.TestsAdapter
import com.iprism.medrayder.databinding.ActivityDiagnosticCenterDetailsBinding
import com.iprism.medrayder.databinding.TestsBottomSheetBinding
import com.iprism.medrayder.interfaces.OnDiagnosticTestItemClickListener
import com.iprism.medrayder.models.diagnostictests.TestsItem
import com.iprism.medrayder.models.labtests.LabTestsRequest
import com.iprism.medrayder.models.labtests.MainDataItem
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.LabDetailsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class LabDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDiagnosticCenterDetailsBinding
    private lateinit var viewModel: LabDetailsViewModel
    private var labId = ""
    private var labName = ""
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var labTests = mutableListOf<MainDataItem>()
    private lateinit var adapter: LabTestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnosticCenterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("labId")) {
            labId = intent.getStringExtra("labId")!!
            labName = intent.getStringExtra("labName")!!
            binding.nameTxt.text = labName
        }
        setUpHealthPackages()
        initViewModel()
        observeResponse()
        handleBack()
        handleContinueBtn()
        handlePrescriptionLL()
        fetchDiagnosticTests()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DiagnosticTimeSlotActivity::class.java))
        })
    }

    private fun handlePrescriptionLL() {
        binding.prescriptionLl.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, PrescriptionActivity::class.java)
            intent.putExtra("tag", "labTest")
            intent.putExtra("labId", labId)
            startActivity(intent)
        })
    }

    private fun setUpHealthPackages() {
        adapter = LabTestsAdapter(labTests as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.healthPackagesRv.apply {
            layoutManager = linearLayoutManager
            adapter = this@LabDetailsActivity.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreTests()
                        }
                    }
                }
            })
        }
        adapter.setOnDoctorItemClickListener(object : OnDiagnosticTestItemClickListener{
            override fun onTestsClicked(tests: List<TestsItem>) {
                showTestsSheet(tests)
            }

            override fun onBookClicked(testId: String) {
                val intent = Intent(this@LabDetailsActivity, LabTimeSlotActivity::class.java)
                intent.putExtra("labId", labId)
                intent.putExtra("testId", testId)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = LabsRepository()
        val factory = ViewModelFactory { LabDetailsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[LabDetailsViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    adapter.removeLoadingFooter()
                    val newBookings = result.data.response.mainData
                    if (newBookings.isNotEmpty()) {
                        labTests.addAll(newBookings)
                        adapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    adapter.removeLoadingFooter()
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchDiagnosticTests() {
        val userDetails = getUserDetails()
        val request = LabTestsRequest(userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), labId.toInt())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchLabTests(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreTests() {
        isLoading = true
        currentPage += 1
        adapter.showLoadingFooter()
        fetchDiagnosticTests()
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