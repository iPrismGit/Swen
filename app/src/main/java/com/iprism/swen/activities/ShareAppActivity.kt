package com.iprism.swen.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityShareAppBinding
import java.io.File
import java.io.FileOutputStream

class ShareAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareAppBinding
    private val appLink = "https://play.google.com/store/apps/details?id=com.iprism.medrayder"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleCopy()
        handleShare()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleCopy() {
        binding.copyTxt.setOnClickListener(View.OnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("App Link", appLink)
            clipboard.setPrimaryClip(clip)
        })
    }

    private fun handleShare() {
        binding.shareBtn.setOnClickListener(View.OnClickListener {
            val shareText = "Check out MedRayder - 24x7 Health Assistance.\n\nDownload here:\n$appLink"
            try {
                // Convert drawable to file in cache
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.med_rayder_app_logo)
                val file = File(cacheDir, "share_logo.png")
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()

                // Get URI with FileProvider
                val uri: Uri = FileProvider.getUriForFile(
                    this,
                    "${applicationContext.packageName}.provider",
                    file
                )

                // Create share intent
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error sharing", Toast.LENGTH_SHORT).show()
            }
        })
    }
}