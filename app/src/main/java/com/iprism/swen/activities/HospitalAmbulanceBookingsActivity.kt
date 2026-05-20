package com.iprism.swen.activities

import HospitalAmbulanceBookingViewPagerAdapter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityHospitalAmbulanceBookingsBinding

class HospitalAmbulanceBookingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHospitalAmbulanceBookingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalAmbulanceBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = HospitalAmbulanceBookingViewPagerAdapter(this)
        binding.viewPager1.isUserInputEnabled = false
        binding.viewPager1.adapter = adapter
        binding.viewPager1.setCurrentItem(0, false)
        handleBack()
        handleUpcomingLabTestsTxt()
        handleCompletedTxt()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleUpcomingLabTestsTxt() {
        binding.bookingsTxt.setOnClickListener(View.OnClickListener {
            binding.viewPager1.setCurrentItem(0, false)
            binding.bookingsTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.bookingsTxt.background = ContextCompat.getDrawable(this, R.drawable.checked_txt_bg)
            binding.completedTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.completedTxt.background = ContextCompat.getDrawable(this, R.drawable.unchecked_txt_bg)
        })
    }

    private fun handleCompletedTxt() {
        binding.completedTxt.setOnClickListener(View.OnClickListener {
            binding.viewPager1.setCurrentItem(1, false)
            binding.completedTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.completedTxt.background = ContextCompat.getDrawable(this, R.drawable.checked_txt_bg)
            binding.bookingsTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.bookingsTxt.background = ContextCompat.getDrawable(this, R.drawable.unchecked_txt_bg)
        })
    }
}