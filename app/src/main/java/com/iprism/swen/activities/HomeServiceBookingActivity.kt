package com.iprism.swen.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.swen.databinding.ActivityHomeServicePatientDetailsBinding

class HomeServiceBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeServicePatientDetailsBinding
    private var registerFor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeServicePatientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        handleConfirmBookingBtn()
        setupData()
    }

    private fun setupData() {
        if (registerFor.equals("RegisterForYou")){
            binding.uploadTopLo.visibility = View.GONE
            binding.emailLo.visibility = View.GONE
            binding.dobTopLo.visibility = View.GONE
            binding.reasonForBookingLo.visibility = View.GONE
            binding.degreeLo.visibility = View.GONE
        }
    }

    private fun handleConfirmBookingBtn() {
        binding.confirmBtn.setOnClickListener { p0 ->
            val intent = Intent(this, SuccessActivity::class.java)
            intent.putExtra("tag", "Home Service Booking Completed ")
            startActivity(intent)
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { p0 ->
            finish()
        }
    }
}