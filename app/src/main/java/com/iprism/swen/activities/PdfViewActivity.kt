package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.iprism.swen.databinding.ActivityPdfViewBinding
import com.iprism.swen.utils.Constants

class PdfViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewBinding
    private var pdfUrl: String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pdfUrl = intent.getStringExtra("pdfUrl").toString()
        binding.nameTxt.text = intent.getStringExtra("name")
        binding.pdfView.settings.javaScriptEnabled = true
        binding.progress.visibility = View.VISIBLE
        val googleDocsUrl = "https://docs.google.com/viewer?url=${Constants.IMAGES_BASE_URL + pdfUrl}"
        binding.pdfView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progress.visibility = View.GONE
            }
        }
        binding.pdfView.loadUrl(googleDocsUrl)
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}