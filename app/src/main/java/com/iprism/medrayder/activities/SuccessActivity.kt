package com.iprism.medrayder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivitySuccessBinding

class SuccessActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView20.scaleX = 0f
        binding.imageView20.scaleY = 0f
        binding.imageView20.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
        binding.purposeTxt.alpha = 0f
        binding.purposeTxt.translationY = 50f
        binding.purposeTxt.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(300)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
        if (intent.hasExtra("tag")) {
            binding.purposeTxt.text = intent.getStringExtra("tag")!!
        }
        Handler().postDelayed({
            finishAffinity()
            startActivity(Intent(this, HomeActivity::class.java))
        }, 2000)
    }
}