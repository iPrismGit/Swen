package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityDoctorProfileBinding
import com.iprism.swen.models.surgeonsymptomdoctors.DoctorsItem
import com.iprism.swen.utils.Constants

class SurgeonSymptomDoctorProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorProfileBinding
    private var symptomId = ""
    private var hospitalId = ""
    private var doctor : DoctorsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra("symptomId")) {
            symptomId = intent.getStringExtra("symptomId")!!
            hospitalId = intent.getStringExtra("hospitalId")!!
            doctor = intent.getSerializableExtra("doctor") as DoctorsItem?
            showDoctorDetails()
        }
        handleBack()
        handleBookNowBtn()
    }

    private fun handleBookNowBtn() {
        binding.bookBtn.setOnClickListener { view ->
            val intent = Intent(this, SurgeonDoctorTimeSlotActivity::class.java)
            intent.putExtra("doctor", doctor)
            intent.putExtra("symptomId", symptomId)
            intent.putExtra("hospitalId", hospitalId)
            startActivity(intent)
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDoctorDetails() {
        binding.nameTxt.text  = doctor!!.name
        binding.specialityTxt.text  = doctor!!.specialization
        if (doctor!!.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + doctor!!.image)
                .placeholder(R.drawable.profile_icon)
                .into(binding.doctorImg)
        }
        binding.studyTxt.text  = doctor!!.qualification
        binding.educationTxt.text  = doctor!!.qualification
        binding.aboutTxt.text  = doctor!!.description
        binding.registrationIdTxt.text  = doctor!!.uniqueId
        binding.consultFeeTxt.text  = "₹" + doctor!!.fee
        binding.ratingBar.rating = doctor!!.rating.toFloat()
        binding.consultationsCountTxt.text  = "${doctor!!.consultations} ${if (doctor!!.consultations.toInt() > 1) getString(R.string.consultations) else getString(R.string.consultation)}"
        binding.expTxt.text = "${doctor!!.exp} ${if (doctor!!.exp.toInt() > 1) getString(R.string.years) else getString(R.string.year)}"
    }
}