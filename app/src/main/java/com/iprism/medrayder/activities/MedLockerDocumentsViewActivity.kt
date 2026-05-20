package com.iprism.medrayder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivityMedLockerDocumentsViewBinding

class MedLockerDocumentsViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMedLockerDocumentsViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedLockerDocumentsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}