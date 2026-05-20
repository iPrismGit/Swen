package com.iprism.swen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.R
import com.iprism.swen.adapters.MultipleFamilyMembersAdapter
import com.iprism.swen.databinding.ActivityAddLabTestPatientBinding
import com.iprism.swen.interfaces.OnMultipleFamilyItemClickListener
import com.iprism.swen.models.diagnostictimings.Price
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.utils.showToast

class AddMultiplePatientsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddLabTestPatientBinding
    private var tag = ""
    private var date = ""
    private var convertDate = ""
    private var id = ""
    private var testId = ""
    private var addressId = ""
    private var imageUri = ""
    private var time : TimesItem? = null
    private var price : Price? = null
    private var familyMembers : ArrayList<FamilyMembersItem>? = null
    private var selectedFamilyMembers  = ArrayList<FamilyMembersItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLabTestPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
            familyMembers = intent.getSerializableExtra("familyMembers") as ArrayList<FamilyMembersItem>?
            date = intent.getStringExtra("date")!!
            convertDate = intent.getStringExtra("convertDate")!!
            testId = intent.getStringExtra("testId")!!
            time = intent.getSerializableExtra("time") as TimesItem?
            price = intent.getSerializableExtra("price") as Price?
            if (tag.equals("lab", true)) {
                id = intent.getStringExtra("labId")!!
                addressId = intent.getStringExtra("addressId")!!
                imageUri = intent.getStringExtra("imageUri")!!
            } else if (tag.equals("diagnostic", true)) {
                id = intent.getStringExtra("diagnosticId")!!
                imageUri = intent.getStringExtra("imageUri")!!
            } else if (tag.equals("hospitalDiagnostic", true)) {
                id = intent.getStringExtra("hospitalId")!!
                imageUri = intent.getStringExtra("imageUri")!!
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
            if (tag.equals("lab", true)) {
                if (price!!.patientCount == selectedFamilyMembers.size) {
                    val intent = Intent(this, LabTestSummaryActivity::class.java)
                    intent.putExtra("tag", "lab")
                    intent.putExtra("id", id)
                    intent.putExtra("familyMembers", familyMembers)
                    intent.putExtra("price", price)
                    intent.putExtra("testId", testId)
                    intent.putExtra("date", date)
                    intent.putExtra("convertDate", convertDate)
                    intent.putExtra("time", time)
                    intent.putExtra("selectedFamilyMembers", selectedFamilyMembers)
                    intent.putExtra("addressId", addressId)
                    intent.putExtra("imageUri", imageUri)
                    startActivity(intent)
                } else {
                    showToast(getString(R.string.please_select) + " ${price!!.patientCount} " + getString(R.string.members))
                }
            } else if (tag.equals("diagnostic", true)) {
                if (price!!.patientCount == selectedFamilyMembers.size) {
                    val intent = Intent(this, DiagnosticTestSummaryActivity::class.java)
                    intent.putExtra("tag", "diagnostic")
                    intent.putExtra("id", id)
                    intent.putExtra("familyMembers", familyMembers)
                    intent.putExtra("price", price)
                    intent.putExtra("testId", testId)
                    intent.putExtra("date", date)
                    intent.putExtra("convertDate", convertDate)
                    intent.putExtra("time", time)
                    intent.putExtra("imageUri", imageUri)
                    intent.putExtra("selectedFamilyMembers", selectedFamilyMembers)
                    startActivity(intent)
                } else {
                    showToast(getString(R.string.please_select) + " ${price!!.patientCount} " + getString(R.string.members))
                }
            }  else if (tag.equals("hospitalDiagnostic", true)) {
                if (price!!.patientCount == selectedFamilyMembers.size) {
                    val intent = Intent(this, HospitalDiagnosticTestSummaryActivity::class.java)
                    intent.putExtra("tag", "hospitalDiagnostic")
                    intent.putExtra("id", id)
                    intent.putExtra("familyMembers", familyMembers)
                    intent.putExtra("price", price)
                    intent.putExtra("testId", testId)
                    intent.putExtra("date", date)
                    intent.putExtra("convertDate", convertDate)
                    intent.putExtra("time", time)
                    intent.putExtra("imageUri", imageUri)
                    intent.putExtra("selectedFamilyMembers", selectedFamilyMembers)
                    startActivity(intent)
                } else {
                    showToast(getString(R.string.please_select) + " ${price!!.patientCount} " + getString(R.string.members))
                }
            }
        })
    }

    private fun setUpFamilyMembers() {
        val familyMembersAdapter = MultipleFamilyMembersAdapter(familyMembers!!, price!!.patientCount)
        binding.familyMembersRv.layoutManager = GridLayoutManager(this, 3)
        binding.familyMembersRv.adapter = familyMembersAdapter
        familyMembersAdapter.setOnMultipleFamilyItemClickListener(object : OnMultipleFamilyItemClickListener{
            override fun onItemClicked(familyMembers: ArrayList<FamilyMembersItem>) {
                selectedFamilyMembers = familyMembers
            }
        })
    }
}