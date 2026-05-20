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
import com.iprism.swen.models.admit.AdmitBookingRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HospitalAdmitViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class AddAdmitPatientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLabTestPatientBinding
    private lateinit var viewModel: HospitalAdmitViewModel
    private var tag = ""
    private var date = ""
    private var convertDate = ""
    private var id = ""
    private var catId = ""
    private var time: TimesItem? = null
    private var familyMembers: ArrayList<FamilyMembersItem>? = null
    private var familyMembersItem: FamilyMembersItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLabTestPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
            if (tag.equals("hospitalAdmit", true)) {
                familyMembers = intent.getSerializableExtra("familyMembers") as ArrayList<FamilyMembersItem>?
                date = intent.getStringExtra("date")!!
                convertDate = intent.getStringExtra("convertDate")!!
                id = intent.getStringExtra("hospitalId")!!
                catId = intent.getStringExtra("catId")!!
                time = intent.getSerializableExtra("time") as TimesItem?
                setUpFamilyMembers(familyMembers!!)
            }
        }
        handleBack()
        handleContinueBookingBtn()
        initViewModel()
        observeResponse()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBookingBtn() {
        binding.continueBookingBtn.setOnClickListener(View.OnClickListener {
            if (familyMembersItem != null) {
                binding.continueBookingBtn.isClickable = false
                binding.continueBookingBtn.isActivated = false
                binding.continueBookingBtn.isEnabled = false
                bookHospitalAdmit()
            } else {
                showToast(getString(R.string.please_select_family_member))
            }
        })
    }

    private fun setUpFamilyMembers(familyMembersItem: ArrayList<FamilyMembersItem>) {
        val familyMembersAdapter = FamilyMembersAdapter(familyMembersItem)
        binding.familyMembersRv.layoutManager = GridLayoutManager(this, 3)
        binding.familyMembersRv.adapter = familyMembersAdapter
        familyMembersAdapter.setOnDoctorItemClickListener(object : OnFamilyItemClickListener {
            override fun onItemClicked(familyMembersItem: FamilyMembersItem) {
                this@AddAdmitPatientActivity.familyMembersItem = familyMembersItem
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalAdmitViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalAdmitViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.bookingResponse.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.continueBookingBtn.isClickable = true
                    binding.continueBookingBtn.isActivated = true
                    binding.continueBookingBtn.isEnabled = true
                    val intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", getString(R.string.hospital_admit_booked_successfully))
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (result.message.equals("Your subscription has expired. Please renew.", true)) {
                        val intent = Intent(this, SubscriptionActivity::class.java)
                        intent.putExtra("tag", "booking")
                        startActivity(intent)
                    } else {
                        showToast(result.message)
                    }
                    binding.continueBookingBtn.isClickable = true
                    binding.continueBookingBtn.isActivated = true
                    binding.continueBookingBtn.isEnabled = true
                }
            }
        }
    }

    private fun bookHospitalAdmit() {
        val userDetails = getUserDetails()
        val request = AdmitBookingRequest(
            date,
            userDetails[User.ID]!!.toInt(),
            time!!.id,
            catId.toInt(),
            familyMembersItem!!.mobile,
            familyMembersItem!!.name,
            "book",
            userDetails[User.LANG].toString(),
            time!!.time,
            userDetails[User.AUTH_TOKEN].toString(),
            id.toInt(),
            familyMembersItem!!.id
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.bookHospitalAdmit(req)
        }
        Log.d("requestLoading", request.toString())
    }
}