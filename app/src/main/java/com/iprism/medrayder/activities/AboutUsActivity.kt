package com.iprism.medrayder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding
    private var tag : String = ""
    private var name : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleTerms()
        handlePrivacy()
        handleAboutUs()
        handleBack()
        handleRefund()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleAboutUs() {
        binding.aboutUsTxt.setOnClickListener(View.OnClickListener {
            tag = "about_us"
            name = getString(R.string.about_us)
            val intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        })
    }

    private fun handlePrivacy() {
        binding.privacyTxt.setOnClickListener(View.OnClickListener {
            tag = "privacy"
            name = getString(R.string.privacy_policy)
            val intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        })
    }

    private fun handleRefund() {
        binding.refundTxt.setOnClickListener(View.OnClickListener {
            tag = "refund"
            name = getString(R.string.refund_policy)
            val intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        })
    }

    private fun handleTerms() {
        binding.termsTxt.setOnClickListener(View.OnClickListener {
            tag = "terms"
            name = getString(R.string.terms_and_conditions)
            val intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        })
    }

}