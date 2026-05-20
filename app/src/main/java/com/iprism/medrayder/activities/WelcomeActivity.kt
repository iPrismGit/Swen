package com.iprism.medrayder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.ImageSliderAdapter
import com.iprism.medrayder.databinding.ActivityWelcomeBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private var currentPage = 0
    private val scrollDelay: Long = 2500L

    private var autoScrollJob: Job? = null
    private var userInteracted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        handleLogin()
        autoScrollSlider()
    }

    private fun setupAdapter() {
        val images = listOf(R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3, R.drawable.welcome4, R.drawable.welcome5)
        binding.viewPager.adapter = ImageSliderAdapter(images)
    }

    private fun handleLogin() {
        binding.mobileLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        })
    }

    private fun autoScrollSlider() {
        val viewPager = binding.viewPager
        val itemCount = viewPager.adapter?.itemCount ?: return

        // Detect manual interaction
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
                userInteracted = true
            }
        })

        // Launch coroutine
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                autoScrollJob = launch {
                    while (true) {
                        delay(scrollDelay)
                        if (userInteracted) {
                            // Reset flag and wait again before auto-scroll
                            userInteracted = false
                            delay(scrollDelay)
                        }
                        currentPage = (currentPage + 1) % itemCount
                        viewPager.setCurrentItem(currentPage, true)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        autoScrollJob?.cancel()
    }
}