package com.iprism.swen.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.swen.databinding.ActivityAirAmbulanceBinding

class AirAmbulanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAirAmbulanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAirAmbulanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}