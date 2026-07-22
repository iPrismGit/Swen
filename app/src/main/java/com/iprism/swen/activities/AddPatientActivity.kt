package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.adapters.FamilyMembersAdapter1
import com.iprism.swen.databinding.ActivityChooseFamilyMembersBinding
import com.iprism.swen.interfaces.OnFamilyItemClickListener
import com.iprism.swen.models.familymembers.FamilyMembersRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.models.surgeonsymptomdoctors.DoctorsItem
import com.iprism.swen.repository.CommonRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.FamilyMembersViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class AddPatientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseFamilyMembersBinding
    private lateinit var viewModel: FamilyMembersViewModel
    private var tag = ""
    private var date = ""
    private var convertDate = ""
    private var consultType = ""
    private var fee = ""
    private var specialityId : Int = 0
    private var symptomId = ""
    private var hospitalId = ""
    private var time: TimesItem? = null
    private var familyMembers: ArrayList<FamilyMembersItem>? = null
    private var doctor: DoctorsItem? = null
    private var symptomDoctor: DoctorsItem? = null
    private var surgeonDoctor: DoctorsItem? = null
    private var familyMembersItem: FamilyMembersItem? = null
    private var imageUri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChooseFamilyMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
            if (tag.equals("doctor", true)) {
                familyMembers = intent.getSerializableExtra("familyMembers") as ArrayList<FamilyMembersItem>?
                date = intent.getStringExtra("date")!!
                convertDate = intent.getStringExtra("convertDate")!!
                time = intent.getSerializableExtra("time") as TimesItem?
                specialityId = intent.getIntExtra("specialityId", 0)
                doctor = intent.getSerializableExtra("doctor") as DoctorsItem?
                consultType = intent.getStringExtra("consultType")!!
                fee = intent.getStringExtra("fee")!!
                setUpFamilyMembers(familyMembers!!)
            } else if (tag.equals("symptomDoctor", true)) {
                familyMembers = intent.getSerializableExtra("familyMembers") as ArrayList<FamilyMembersItem>?
                date = intent.getStringExtra("date")!!
                convertDate = intent.getStringExtra("convertDate")!!
                time = intent.getSerializableExtra("time") as TimesItem?
                specialityId = intent.getIntExtra("specialityId", 0)
                symptomDoctor = intent.getSerializableExtra("doctor") as DoctorsItem?
                setUpFamilyMembers(familyMembers!!)
            } else if (tag.equals("surgeonDoctor", true)) {
                familyMembers = intent.getSerializableExtra("familyMembers") as ArrayList<FamilyMembersItem>?
                date = intent.getStringExtra("date")!!
                convertDate = intent.getStringExtra("convertDate")!!
                time = intent.getSerializableExtra("time") as TimesItem?
                symptomId = intent.getStringExtra("symptomId")!!
                hospitalId = intent.getStringExtra("hospitalId")!!
                surgeonDoctor = intent.getSerializableExtra("doctor") as DoctorsItem?
                setUpFamilyMembers(familyMembers!!)
            } else if (tag.equals("diagnostic", true)) {
                imageUri = intent.getStringExtra("imageUri")!!
                initViewModel()
                observeResponse()
                fetchFamilyMembers()
            }  else if (tag.equals("healthCheckup", true)) {
                imageUri = intent.getStringExtra("imageUri")!!
                initViewModel()
                observeResponse()
                fetchFamilyMembers()
            }
        }
        handleBack()
        handleContinueBtn()
    }

    private fun handleContinueBtn() {
        binding.confirmBtn.setOnClickListener { view ->
            if (tag.equals("doctor", true)) {
                if (familyMembersItem == null) {
                    showToast("Please Select Family Member")
                } else {
                   /* val intent = Intent(this, AppointmentDoctorSummaryActivity::class.java)
                    intent.putExtra("doctor", doctor)
                    intent.putExtra("date", date)
                    intent.putExtra("convertDate", convertDate)
                    intent.putExtra("time", time)
                    intent.putExtra("familyMember", familyMembersItem)
                    intent.putExtra("specialityId", specialityId)
                    intent.putExtra("consultType", consultType)
                    intent.putExtra("fee", fee)
                    startActivity(intent)*/
                }
            } else if (tag.equals("symptomDoctor", true)) {
                if (familyMembersItem == null) {
                    showToast("Please Select Family Member")
                } else {
                   /* val intent = Intent(this, SymptomDoctorSummaryActivity::class.java)
                    intent.putExtra("doctor", symptomDoctor)
                    intent.putExtra("date", date)
                    intent.putExtra("convertDate", convertDate)
                    intent.putExtra("time", time)
                    intent.putExtra("familyMember", familyMembersItem)
                    intent.putExtra("specialityId", specialityId)
                    startActivity(intent)*/
                }
            } else if (tag.equals("surgeonDoctor", true)) {
                if (familyMembersItem == null) {
                    showToast("Please Select Family Member")
                } else {
                    val intent = Intent(this, SurgeonDoctorSummaryActivity::class.java)
                    intent.putExtra("doctor", surgeonDoctor)
                    intent.putExtra("date", date)
                    intent.putExtra("convertDate", convertDate)
                    intent.putExtra("time", time)
                    intent.putExtra("familyMember", familyMembersItem)
                    intent.putExtra("specialityId", symptomId)
                    intent.putExtra("hospitalId", hospitalId)
                    startActivity(intent)
                }
            } else if (tag.equals("diagnostic", true)) {
                if (familyMembersItem == null) {
                    showToast("Please Select Family Member")
                } else {
                   /* val intent = Intent(this, DiagnosticPrescriptionCartActivity::class.java)
                    intent.putExtra("tag", specialityId)
                    intent.putExtra("imageUri", imageUri)
                    intent.putExtra("familyMember", familyMembersItem)
                    startActivity(intent)*/
                }
            } else if (tag.equals("healthCheckup", true)) {
                if (familyMembersItem == null) {
                    showToast("Please Select Family Member")
                } else {
                    /*val intent = Intent(this, HealthCheckupPrescriptionCartActivity::class.java)
                    intent.putExtra("tag", specialityId)
                    intent.putExtra("imageUri", imageUri)
                    intent.putExtra("familyMember", familyMembersItem)
                    startActivity(intent)*/
                }
            }
        }
    }

    private fun setUpFamilyMembers(familyMembersItem: ArrayList<FamilyMembersItem>) {
        val familyMembersAdapter = FamilyMembersAdapter1(familyMembersItem)
        binding.familyMembersRv.layoutManager = GridLayoutManager(this, 3)
        binding.familyMembersRv.adapter = familyMembersAdapter
        familyMembersAdapter.setOnDoctorItemClickListener(object : OnFamilyItemClickListener {
            override fun onItemClicked(familyMembersItem: FamilyMembersItem) {
                this@AddPatientActivity.familyMembersItem = familyMembersItem
            }
        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = CommonRepository()
        val factory = ViewModelFactory { FamilyMembersViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[FamilyMembersViewModel::class.java]
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