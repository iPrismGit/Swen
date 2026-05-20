package com.iprism.medrayder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import androidx.lifecycle.lifecycleScope
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivitySplashBinding
import com.iprism.medrayder.utils.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startAnimations()
        lifecycleScope.launch {
            delay(1500)
            val user = User(this@SplashActivity)
            Log.d("userDetails", user.getUserDetails().toString() + user.isUserLoggedIn() + user.isAddress())
            if (user.isUserLoggedIn() && user.isAddress()) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            } else if (user.isUserLoggedIn() && !user.isAddress()) {
                val intent = Intent(this@SplashActivity, AddressActivity::class.java)
                intent.putExtra("tag", "register")
                startActivity(intent)
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LanguagesActivity::class.java))
                finish()
            }
        }
    }

    private fun startAnimations() {
        val scaleAnim = ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnim.duration = 1000
        scaleAnim.fillAfter = true
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000
        fadeIn.fillAfter = true
        val animSet = AnimationSet(true)
        animSet.addAnimation(scaleAnim)
        animSet.addAnimation(fadeIn)
        binding.imageView.startAnimation(animSet)
    }
}