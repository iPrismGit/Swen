package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivityLanguagesBinding
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.showToast

class LanguagesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLanguagesBinding
    private var languageCode : String? = ""
    private var tag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
        }
        if (getUserDetails()[User.LANG].equals("en")) {
            languageCode = "en"
            binding.englishLl.background = ContextCompat.getDrawable(this, R.drawable.language_bg)
            binding.hindiLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.teluguLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.engTickImg.visibility = View.VISIBLE
            binding.hinTickImg.visibility = View.GONE
            binding.telTickImg.visibility = View.GONE
        } else {
            languageCode = "te"
            binding.teluguLl.background = ContextCompat.getDrawable(this, R.drawable.language_bg)
            binding.hindiLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.englishLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.telTickImg.visibility = View.VISIBLE
            binding.hinTickImg.visibility = View.GONE
            binding.engTickImg.visibility = View.GONE
        }
        handleEnglishLL()
        handleTeluguLL()
        handleHindiLL()
        handleConfirmBtn()
    }

    @SuppressLint("ResourceAsColor")
    private fun handleEnglishLL() {
        binding.englishLl.setOnClickListener(View.OnClickListener {
            languageCode = "en"
            binding.englishLl.background = ContextCompat.getDrawable(this, R.drawable.language_bg)
            binding.hindiLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.teluguLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.engTickImg.visibility = View.VISIBLE
            binding.hinTickImg.visibility = View.GONE
            binding.telTickImg.visibility = View.GONE
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun handleHindiLL() {
        binding.hindiLl.setOnClickListener(View.OnClickListener {
            languageCode = "hi"
            binding.hindiLl.background = ContextCompat.getDrawable(this, R.drawable.language_bg)
            binding.englishLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.teluguLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.hinTickImg.visibility = View.VISIBLE
            binding.engTickImg.visibility = View.GONE
            binding.telTickImg.visibility = View.GONE
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun handleTeluguLL() {
        binding.teluguLl.setOnClickListener(View.OnClickListener {
            languageCode = "te"
            binding.teluguLl.background = ContextCompat.getDrawable(this, R.drawable.language_bg)
            binding.hindiLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.englishLl.background = ContextCompat.getDrawable(this, R.drawable.white_bg)
            binding.telTickImg.visibility = View.VISIBLE
            binding.hinTickImg.visibility = View.GONE
            binding.engTickImg.visibility = View.GONE
        })
    }

    private fun handleConfirmBtn() {
        binding.confirmBtn.setOnClickListener(View.OnClickListener {
            if (languageCode!!.isEmpty()) {
                showToast(getString(R.string.please_select_language))
            } else {
                val user = User(this)
                user.storeLang(languageCode!!)
                if (tag.equals("fromProfile", true)) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                }
            }
        })
    }
}