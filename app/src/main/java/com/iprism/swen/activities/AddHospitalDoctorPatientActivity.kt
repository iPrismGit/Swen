package com.iprism.swen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.R
import com.iprism.swen.adapters.FamilyMembersAdapter
import com.iprism.swen.databinding.ActivityAddLabTestPatientBinding
import com.iprism.swen.interfaces.OnFamilyItemClickListener
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.models.hospitaldoctors.DoctorsItem
import com.iprism.swen.utils.showToast

class AddHospitalDoctorPatientActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddLabTestPatientBinding
    private var tag = ""
    private var date = ""
    private var convertDate = ""
    private var specialityId = ""
    private var consultType = ""
    private var fee = ""
    private var hospitalId : String = ""
    private var time : TimesItem? = null
    private var catId = ""
    private var familyMembers : ArrayList<FamilyMembersItem>? = null
    private var doctor : DoctorsItem? =  null
    private var familyMembersItem : FamilyMembersItem? =  null

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
                hospitalId = intent.getStringExtra("hospitalId")!!
                catId = intent.getStringExtra("catId")!!
                consultType = intent.getStringExtra("consultType")!!
                fee = intent.getStringExtra("fee")!!
            }
        }
        handleBack()
        setUpFamilyMembers()
        handleContinueBookingBtn()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBookingBtn() {
        binding.continueBookingBtn.setOnClickListener(View.OnClickListener {
            if (tag.equals("doctor", true)) {
                if (familyMembersItem == null) {
                    showToast(getString(R.string.please_select_family_member))
                } else {
                    val intent = Intent(this, HospitalDoctorSummaryActivity::class.java)
                    intent.putExtra("doctor", doctor)
                    intent.putExtra("date", date)
                    intent.putExtra("convertDate", convertDate)
                    intent.putExtra("time", time)
                    intent.putExtra("familyMember", familyMembersItem)
                    intent.putExtra("specialityId", specialityId)
                    intent.putExtra("hospitalId", hospitalId)
                    intent.putExtra("catId", catId)
                    intent.putExtra("consultType", consultType)
                    intent.putExtra("fee", fee)
                    startActivity(intent)
                }
            }
        })
    }

    private fun setUpFamilyMembers() {
        val familyMembersAdapter = FamilyMembersAdapter(familyMembers!!)
        binding.familyMembersRv.layoutManager = GridLayoutManager(this, 3)
        binding.familyMembersRv.adapter = familyMembersAdapter
        familyMembersAdapter.setOnDoctorItemClickListener(object : OnFamilyItemClickListener{
            override fun onItemClicked(familyMembersItem: FamilyMembersItem) {
                this@AddHospitalDoctorPatientActivity.familyMembersItem = familyMembersItem
            }
        })
    }
}