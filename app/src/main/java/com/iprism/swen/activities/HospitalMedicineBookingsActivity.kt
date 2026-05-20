package com.iprism.swen.activities

import HospitalMedBookingViewPagerAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityHospitalMedicineBookingsBinding

class HospitalMedicineBookingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalMedicineBookingsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalMedicineBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = HospitalMedBookingViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
        if (intent.hasExtra("tag")) {
            val tag = intent.getStringExtra("tag")
            if (tag.equals("wellness", true)) {
                binding.headingTxt.text = getString(R.string.wellness_medicine_bookings)
            }
        }
        handleBack()
        handleOnGoingOrdersTxt()
        handleCompletedOrdersTxt()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleOnGoingOrdersTxt() {
        binding.ongoingOrdersTxt.setOnClickListener(View.OnClickListener {
            binding.viewPager.setCurrentItem(0, false)
            binding.ongoingOrdersTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.ongoingOrdersTxt.background =
                ContextCompat.getDrawable(this, R.drawable.checked_txt_bg)
            binding.completedOrdersTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.completedOrdersTxt.background =
                ContextCompat.getDrawable(this, R.drawable.unchecked_txt_bg)
        })
    }

    private fun handleCompletedOrdersTxt() {
        binding.completedOrdersTxt.setOnClickListener(View.OnClickListener {
            binding.viewPager.setCurrentItem(1, false)
            binding.completedOrdersTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.completedOrdersTxt.background =
                ContextCompat.getDrawable(this, R.drawable.checked_txt_bg)
            binding.ongoingOrdersTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.ongoingOrdersTxt.background =
                ContextCompat.getDrawable(this, R.drawable.unchecked_txt_bg)
        })
    }
}