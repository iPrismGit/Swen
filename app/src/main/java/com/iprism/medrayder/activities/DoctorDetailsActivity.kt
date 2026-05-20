package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.DoctorsAdapter
import com.iprism.medrayder.databinding.ActivityDoctorDetailsBinding
import com.iprism.medrayder.models.onlinedoctors.DoctorsItem
import com.iprism.medrayder.utils.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DoctorDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDoctorDetailsBinding
    private var tag : String = ""
    private var specialityId : String = ""
    private var doctor : DoctorsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            specialityId = intent.getStringExtra("specialityId")!!
            tag = intent.getStringExtra("tag")!!
            doctor = intent.getSerializableExtra("doctor") as DoctorsItem?
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
            val intent = Intent(this, DoctorTimeSlotActivity::class.java)
            intent.putExtra("doctor", doctor)
            intent.putExtra("specialityId", specialityId)
            startActivity(intent)
            /*val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.activity_doctor_details, null)
            createPdfFromView(view, "Medical_Report")*/
        })
    }

    private fun handleHospitalVisitBtn() {
        binding.hospitalVisitBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DoctorTimeSlotActivity::class.java)
            intent.putExtra("doctor", doctor)
            intent.putExtra("specialityId", specialityId)
            startActivity(intent)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showDoctorDetails() {
        binding.nameTxt.text  = doctor!!.name
        binding.specialityTxt.text  = doctor!!.specialization
        if (doctor!!.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + doctor!!.image)
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