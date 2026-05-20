package com.iprism.medrayder.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.medrayder.R
import com.iprism.medrayder.activities.HospitalDetailsActivity
import com.iprism.medrayder.activities.HospitalDiagnosticTimeSlotActivity
import com.iprism.medrayder.activities.PrescriptionActivity
import com.iprism.medrayder.adapters.DiagnosticTestsAdapter
import com.iprism.medrayder.adapters.TestsAdapter
import com.iprism.medrayder.databinding.FragmentHospitalDiagnosticBinding
import com.iprism.medrayder.databinding.TestsBottomSheetBinding
import com.iprism.medrayder.interfaces.OnDiagnosticTestItemClickListener
import com.iprism.medrayder.models.diagnostictests.MainDataItem
import com.iprism.medrayder.models.diagnostictests.TestsItem
import com.iprism.medrayder.models.hospitaldiagnostictests.HospitalDiagnosticTestsRequest
import com.iprism.medrayder.repository.HospitalDiagnosticsRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.HospitalDiagnosticTestsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class HospitalDiagnosticFragment(var hospitalId : String) : Fragment() {

    private lateinit var binding : FragmentHospitalDiagnosticBinding
    private lateinit var viewModel: HospitalDiagnosticTestsViewModel
    private var diagnosticTests = mutableListOf<MainDataItem>()
    private lateinit var diagnosticTestsAdapter: DiagnosticTestsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHospitalDiagnosticBinding.inflate(layoutInflater)
        handleDiagnostic()
        handlePrescriptionLL()
        setUpHealthPackages()
        initViewModel()
        observeResponse()
        fetchDiagnosticTests()
        return binding.root
    }

    private fun handlePrescriptionLL() {
        binding.prescriptionLl.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(), PrescriptionActivity::class.java)
            intent.putExtra("tag", "hospitalDiagnostic")
            intent.putExtra("hospitalId", hospitalId)
            startActivity(intent)
        })
    }

    private fun handleDiagnostic() {
        (activity as? HospitalDetailsActivity)?.setView("diagnostic")
    }

    private fun setUpHealthPackages() {
        diagnosticTestsAdapter = DiagnosticTestsAdapter(diagnosticTests as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.healthPackagesRv.apply {
            layoutManager = linearLayoutManager
            adapter = diagnosticTestsAdapter
        }
        diagnosticTestsAdapter.setOnDoctorItemClickListener(object : OnDiagnosticTestItemClickListener {
            override fun onTestsClicked(tests: List<TestsItem>) {
                showTestsSheet(tests)
            }

            override fun onBookClicked(testId: String) {
                val intent = Intent(requireContext(), HospitalDiagnosticTimeSlotActivity::class.java)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("testId", testId)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalDiagnosticsRepository()
        val factory = ViewModelFactory { HospitalDiagnosticTestsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalDiagnosticTestsViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(viewLifecycleOwner) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    diagnosticTestsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.mainData
                    if (newBookings.isNotEmpty()) {
                        diagnosticTests.addAll(newBookings)
                        diagnosticTestsAdapter.notifyDataSetChanged()
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    } else {
                        requireContext().showToast(result.message)
                    }
                }
            }
        }
    }

    private fun fetchDiagnosticTests() {
        val userDetails = requireContext().getUserDetails()
        val request = HospitalDiagnosticTestsRequest(userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), hospitalId.toInt())
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchDiagnosticTests(req)
        }
        Log.d("requestLoading", request.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun showTestsSheet(tests : List<TestsItem>) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val testsBottomSheetBinding = TestsBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
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
        testsBottomSheetBinding.testsRv.layoutManager = LinearLayoutManager(requireContext())
        testsBottomSheetBinding.testsRv.adapter = testsAdapter
        bottomSheetDialog.show()
    }
}