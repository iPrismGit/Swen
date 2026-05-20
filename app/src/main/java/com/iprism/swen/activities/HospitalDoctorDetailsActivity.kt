package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityDoctorDetailsBinding
import com.iprism.swen.models.hospitaldoctors.DoctorsItem
import com.iprism.swen.utils.Constants

class HospitalDoctorDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDoctorDetailsBinding
    private var tag : String = ""
    private var specialityId : String = ""
    private var hospitalId : String = ""
    private var catId = ""
    private var consultType = ""
    private var fee = ""
    private var doctor : DoctorsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            specialityId = intent.getStringExtra("specialityId")!!
            tag = intent.getStringExtra("tag")!!
            doctor = intent.getSerializableExtra("doctor") as DoctorsItem?
            hospitalId = intent.getStringExtra("hospitalId")!!
            catId = intent.getStringExtra("catId")!!
            showDoctorDetails()
            if (tag.equals("hospitalDoctors", true)) {
                binding.hospitalVisitBtn.visibility = View.VISIBLE
                binding.hospitalVisitFeeLl.visibility = View.VISIBLE
            }
        }
        handleBack()
        handleContinueBtn()
        handleHospitalVisitBtn()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBtn() {
        binding.onlineConsultBtn.setOnClickListener(View.OnClickListener {
            consultType = "online"
            fee = doctor!!.onlineFee.toString()
            val intent = Intent(this, HospitalDoctorTimeSlotActivity::class.java)
            intent.putExtra("doctor", doctor)
            intent.putExtra("specialityId", specialityId)
            intent.putExtra("hospitalId", hospitalId)
            intent.putExtra("catId", catId)
            intent.putExtra("consultType", consultType)
            intent.putExtra("fee", fee)
            startActivity(intent)
        })
    }

    private fun handleHospitalVisitBtn() {
        binding.hospitalVisitBtn.setOnClickListener(View.OnClickListener {
            consultType = "offline"
            fee = doctor!!.offlineFee.toString()
            val intent = Intent(this, HospitalDoctorTimeSlotActivity::class.java)
            intent.putExtra("doctor", doctor)
            intent.putExtra("specialityId", specialityId)
            intent.putExtra("hospitalId", hospitalId)
            intent.putExtra("catId", catId)
            intent.putExtra("consultType", consultType)
            intent.putExtra("fee", fee)
            startActivity(intent)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showDoctorDetails() {
        binding.nameTxt.text  = doctor!!.name
        binding.specialityTxt.text  = doctor!!.specialization
        binding.hospitalNameTxt.visibility = View.VISIBLE
        binding.hospitalNameTxt.text  = doctor!!.hospitalName
        if (doctor!!.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + doctor!!.image)
                .into(binding.doctorImg)
        }
        binding.studyTxt.text  = doctor!!.qualification
        binding.educationTxt.text  = doctor!!.qualification
        binding.aboutTxt.text  = doctor!!.description
        binding.registrationIdTxt.text  = doctor!!.uniqueId
        if (doctor!!.consultType.equals("offline", true)) {
            binding.onlineConsultFeeLl.visibility = View.GONE
            binding.onlineConsultBtn.visibility = View.GONE
            binding.hospitalVisitFeeLl.visibility = View.VISIBLE
            binding.hospitalVisitBtn.visibility = View.VISIBLE
        } else {
            binding.onlineConsultFeeLl.visibility = View.VISIBLE
            binding.onlineConsultBtn.visibility = View.VISIBLE
            binding.hospitalVisitFeeLl.visibility = View.VISIBLE
            binding.hospitalVisitBtn.visibility = View.VISIBLE
        }
        binding.consultFeeTxt.text  = "₹" + doctor!!.onlineFee
        binding.offlineFeeTxt.text  = "₹" + doctor!!.offlineFee
        binding.ratingBar.rating = doctor!!.rating.toFloat()
        binding.consultationsCountTxt.text  = "${doctor!!.consultations} ${if (doctor!!.consultations.toInt() > 1) getString(R.string.consultations) else getString(R.string.consultation)}"
        binding.expTxt.text = "${doctor!!.exp} ${if (doctor!!.exp.toInt() > 1) getString(R.string.years) else getString(R.string.year)}"
    }
}