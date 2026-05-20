package com.iprism.swen.activities

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
import com.iprism.swen.R
import com.iprism.swen.adapters.DiagnosticTestsAdapter
import com.iprism.swen.adapters.TestsAdapter
import com.iprism.swen.databinding.ActivityDiagnosticCenterDetailsBinding
import com.iprism.swen.databinding.TestsBottomSheetBinding
import com.iprism.swen.interfaces.OnDiagnosticTestItemClickListener
import com.iprism.swen.models.diagnostictests.DiagnosticTestsRequest
import com.iprism.swen.models.diagnostictests.MainDataItem
import com.iprism.swen.models.diagnostictests.TestsItem
import com.iprism.swen.repository.DiagnosticsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.viewmodels.DiagnosticDetailsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class DiagnosticCenterDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDiagnosticCenterDetailsBinding
    private lateinit var viewModel: DiagnosticDetailsViewModel
    private var diagnosticId = ""
    private var diagnosticName = ""
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var diagnosticTests = mutableListOf<MainDataItem>()
    private lateinit var adapter: DiagnosticTestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnosticCenterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("diagnosticId")) {
            diagnosticId = intent.getStringExtra("diagnosticId")!!
            diagnosticName = intent.getStringExtra("diagnosticName")!!
            binding.nameTxt.text = diagnosticName
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
            intent.putExtra("tag", "diagnostic")
            intent.putExtra("diagnosticId", diagnosticId)
            startActivity(intent)
        })
    }

    private fun setUpHealthPackages() {
        adapter = DiagnosticTestsAdapter(diagnosticTests as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.healthPackagesRv.apply {
            layoutManager = linearLayoutManager
            adapter = this@DiagnosticCenterDetailsActivity.adapter
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
                val intent = Intent(this@DiagnosticCenterDetailsActivity, DiagnosticTimeSlotActivity::class.java)
                intent.putExtra("diagnosticId", diagnosticId)
                intent.putExtra("testId", testId)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = DiagnosticsRepository()
        val factory = ViewModelFactory { DiagnosticDetailsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DiagnosticDetailsViewModel::class.java]
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
                        diagnosticTests.addAll(newBookings)
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
        val request = DiagnosticTestsRequest(userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), diagnosticId.toInt())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchDiagnosticTests(req)
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
        testsBottomSheetBinding.numberOfTestsTxt.text = getString(R.string.contains) + " ${tests.size} " + getString(R.string.tests)
        val testsAdapter = TestsAdapter(tests)
        Log.d("tests", tests.toString())
        testsBottomSheetBinding.testsRv.layoutManager = LinearLayoutManager(this)
        testsBottomSheetBinding.testsRv.adapter = testsAdapter
        bottomSheetDialog.show()
    }
}