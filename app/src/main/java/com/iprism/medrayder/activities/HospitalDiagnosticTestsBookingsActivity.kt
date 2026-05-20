package com.iprism.medrayder.activities

import HospitalDiagnosticBookingsViewPagerAdapter
import LabTestsBookingsViewPagerAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.LabTestBookingsAdapter
import com.iprism.medrayder.databinding.ActivityLabTestsBookingsBinding

class HospitalDiagnosticTestsBookingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLabTestsBookingsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLabTestsBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.headingTxt.text = getString(R.string.hospital_diagnostic_bookings)
        binding.upcomingLabTestsTxt.text = getString(R.string.bookings)
        val adapter = HospitalDiagnosticBookingsViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
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
        binding.upcomingLabTestsTxt.setOnClickListener(View.OnClickListener {
            binding.viewPager.setCurrentItem(0, false)
            binding.upcomingLabTestsTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.upcomingLabTestsTxt.background = ContextCompat.getDrawable(this, R.drawable.checked_txt_bg)
            binding.completedTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.completedTxt.background = ContextCompat.getDrawable(this, R.drawable.unchecked_txt_bg)
        })
    }

    private fun handleCompletedTxt() {
        binding.completedTxt.setOnClickListener(View.OnClickListener {
            binding.viewPager.setCurrentItem(1, false)
            binding.completedTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.completedTxt.background = ContextCompat.getDrawable(this, R.drawable.checked_txt_bg)
            binding.upcomingLabTestsTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.upcomingLabTestsTxt.background = ContextCompat.getDrawable(this, R.drawable.unchecked_txt_bg)
        })
    }
}