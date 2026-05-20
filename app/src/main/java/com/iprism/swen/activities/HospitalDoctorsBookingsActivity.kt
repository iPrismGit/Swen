package com.iprism.swen.activities

import HospitalDoctorBookingsViewPagerAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityOnlineDoctorsBookingsBinding

class HospitalDoctorsBookingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOnlineDoctorsBookingsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnlineDoctorsBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.headingTxt.text = getString(R.string.hospital_doctor_bookings)
        binding.onlineConsultTxt.text = getString(R.string.consultation)
        val adapter = HospitalDoctorBookingsViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
        handleBack()
        handleOnlineConsultationTxt()
        handleCompletedTxt()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleOnlineConsultationTxt() {
        binding.onlineConsultTxt.setOnClickListener(View.OnClickListener {
            binding.viewPager.setCurrentItem(0, false)
            binding.onlineConsultTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.onlineConsultTxt.background = ContextCompat.getDrawable(this, R.drawable.checked_txt_bg)
            binding.completedTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.completedTxt.background = ContextCompat.getDrawable(this, R.drawable.unchecked_txt_bg)
        })
    }

    private fun handleCompletedTxt() {
        binding.completedTxt.setOnClickListener(View.OnClickListener {
            binding.viewPager.setCurrentItem(1, false)
            binding.completedTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.completedTxt.background = ContextCompat.getDrawable(this, R.drawable.checked_txt_bg)
            binding.onlineConsultTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.onlineConsultTxt.background = ContextCompat.getDrawable(this, R.drawable.unchecked_txt_bg)
        })
    }
}