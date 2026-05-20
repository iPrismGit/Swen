package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.R
import com.iprism.swen.adapters.FamilyMembersAdapter
import com.iprism.swen.databinding.ActivityAddLabTestPatientBinding
import com.iprism.swen.interfaces.OnFamilyItemClickListener
import com.iprism.swen.models.familymembers.FamilyMembersRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.models.onlinedoctors.DoctorsItem
import com.iprism.swen.repository.CommonRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.AddPatientViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class AddLabTestPatientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLabTestPatientBinding
    private lateinit var viewModel: AddPatientViewModel
    private var tag = ""
    private var date = ""
    private var convertDate = ""
    private var specialityId = ""
    private var labId = ""
    private var hospitalId = ""
    private var diagnosticId = ""
    private var imageUri = ""
    private var time: TimesItem? = null
    private var familyMembers: ArrayList<FamilyMembersItem>? = null
    private var doctor: DoctorsItem? = null
    private var familyMembersItem: FamilyMembersItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLabTestPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
            if (tag.equals("doctor", true)) {
                familyMembers =
                    intent.getSerializableExtra("familyMembers") as ArrayList<FamilyMembersItem>?
                doctor = intent.getSerializableExtra("doctor") as DoctorsItem?
                date = intent.getStringExtra("date")!!
                convertDate = intent.getStringExtra("convertDate")!!
                time = intent.getSerializableExtra("time") as TimesItem?
                specialityId = intent.getStringExtra("specialityId")!!
                setUpFamilyMembers(familyMembers!!)
            } else if (tag.equals("labTest", true)) {
                imageUri = intent.getStringExtra("imageUri")!!
                labId = intent.getStringExtra("labId")!!
                initViewModel()
                observeResponse()
                fetchFamilyMembers()
            } else if (tag.equals("diagnostic", true)) {
                imageUri = intent.getStringExtra("imageUri")!!
                diagnosticId = intent.getStringExtra("diagnosticId")!!
                initViewModel()
                observeResponse()
                fetchFamilyMembers()
            } else if (tag.equals("hospitalDiagnostic", true)) {
                imageUri = intent.getStringExtra("imageUri")!!
                hospitalId = intent.getStringExtra("hospitalId")!!
                initViewModel()
                observeResponse()
                fetchFamilyMembers()
            }
        }
        handleBack()
        handleContinueBookingBtn()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBookingBtn() {
        binding.continueBookingBtn.setOnClickListener(View.OnClickListener {
            if (tag.equals("labTest", true)) {
                if (familyMembersItem == null) {
                    showToast(getString(R.string.please_select_family_member))
                } else {
                    val intent = Intent(this, LabTestPrescriptionCartActivity::class.java)
                    intent.putExtra("tag", "labTest")
                    intent.putExtra("familyMember", familyMembersItem)
                    intent.putExtra("imageUri", imageUri)
                    intent.putExtra("labId", labId)
                    startActivity(intent)
                }
            } else if (tag.equals("diagnostic", true)) {
                if (familyMembersItem == null) {
                    showToast(getString(R.string.please_select_family_member))
                } else {
                    val intent = Intent(this, DiagnosticTestPrescriptionCartActivity::class.java)
                    intent.putExtra("tag", "diagnostic")
                    intent.putExtra("familyMember", familyMembersItem)
                    intent.putExtra("imageUri", imageUri)
                    intent.putExtra("diagnosticId", diagnosticId)
                    startActivity(intent)
                }
            } else if (tag.equals("hospitalDiagnostic", true)) {
                if (familyMembersItem == null) {
                    showToast(getString(R.string.please_select_family_member))
                } else {
                    val intent =
                        Intent(this, HospitalDiagnosticTestPrescriptionCartActivity::class.java)
                    intent.putExtra("tag", "hospitalDiagnostic")
                    intent.putExtra("familyMember", familyMembersItem)
                    intent.putExtra("imageUri", imageUri)
                    intent.putExtra("hospitalId", hospitalId)
                    startActivity(intent)
                }
            } else if (tag.equals("doctor", true)) {
                if (familyMembersItem == null) {
                    showToast(getString(R.string.please_select_family_member))
                } else {
                    val intent = Intent(this, DoctorSummaryActivity::class.java)
                    intent.putExtra("doctor", doctor)
                    intent.putExtra("date", date)
                    intent.putExtra("convertDate", convertDate)
                    intent.putExtra("time", time)
                    intent.putExtra("familyMember", familyMembersItem)
                    intent.putExtra("specialityId", specialityId)
                    startActivity(intent)
                }
            } else if (tag.equals("hospital", true)) {
                val intent = Intent(this, SuccessActivity::class.java)
                intent.putExtra("tag", "Booking Confirmed")
                startActivity(intent)
            } else {
                startActivity(Intent(this, DiagnosticTestSummaryActivity::class.java))
            }
        })
    }

    private fun setUpFamilyMembers(familyMembersItem: ArrayList<FamilyMembersItem>) {
        val familyMembersAdapter = FamilyMembersAdapter(familyMembersItem)
        binding.familyMembersRv.layoutManager = GridLayoutManager(this, 3)
        binding.familyMembersRv.adapter = familyMembersAdapter
        familyMembersAdapter.setOnDoctorItemClickListener(object : OnFamilyItemClickListener {
            override fun onItemClicked(familyMembersItem: FamilyMembersItem) {
                this@AddLabTestPatientActivity.familyMembersItem = familyMembersItem
            }
        })
    }

    private fun initViewModel() {
        val repository = CommonRepository()
        val factory = ViewModelFactory { AddPatientViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AddPatientViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    setUpFamilyMembers(result.data.response.familyMembers)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun fetchFamilyMembers() {
        val userDetails = getUserDetails()
        val request = FamilyMembersRequest(
            userDetails[User.ID]!!.toInt(),
            userDetails[User.LANG].toString(),
            userDetails[User.AUTH_TOKEN].toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchFamilyMembers(req)
        }
        Log.d("requestLoading", request.toString())
    }
}