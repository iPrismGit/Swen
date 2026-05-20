package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.ActivitySubscriptionDetailsBinding
import com.iprism.medrayder.models.subscription.SubscriptionRequest
import com.iprism.medrayder.models.subscriptiondetails.Response
import com.iprism.medrayder.models.subscriptiondetails.SubscriptionDetailsRequest
import com.iprism.medrayder.repository.SubscriptionRepository
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.SubscriptionDetailsViewModel
import com.iprism.medrayder.viewmodels.SubscriptionViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class SubscriptionDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySubscriptionDetailsBinding
    private lateinit var viewModel: SubscriptionDetailsViewModel
    private var invoice = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleRenewalBtn()
        handleViewInvoice()
        handleDownloadInvoice()
        initViewModel()
        observeResponse()
    }

    override fun onResume() {
        super.onResume()
        fetchSubscriptionDetails()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleViewInvoice() {
        binding.viewInvoiceBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, PdfViewActivity::class.java)
            intent.putExtra("pdfUrl", invoice)
            intent.putExtra("name", getString(R.string.invoice))
            startActivity(intent)
        })
    }

    private fun handleDownloadInvoice() {
        binding.downloadInvoiceBtn.setOnClickListener(View.OnClickListener {
            downloadAndOpenPdf(this, Constants.IMAGES_BASE_URL + invoice, "MedRayderInvoice.pdf")
        })
    }

    private fun handleRenewalBtn() {
        binding.renewalPlanBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SubscriptionActivity::class.java)
            intent.putExtra("tag", "booking")
            startActivity(intent)
        })
    }

    private fun initViewModel() {
        val repository = SubscriptionRepository()
        val factory = ViewModelFactory { SubscriptionDetailsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SubscriptionDetailsViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.noDataLl.visibility = View.GONE
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.noDataLl.visibility = View.GONE
                    binding.progress.hideProgress()
                    showSubscriptionDetails(result.data.response)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                        binding.noDataTxt.text = getString(R.string.no_data_found)
                    } else {
                        showToast(result.message)
                    }
                    binding.renewalPlanBtn.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun fetchSubscriptionDetails() {
        val userDetails = getUserDetails()
        val request = SubscriptionDetailsRequest( userDetails[User.ID]!!.toInt(), userDetails[User.LANG]!!, userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchSubscriptionDetails(req)
        }
        Log.d("requestLoading", request.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun showSubscriptionDetails(details : Response) {
        binding.scrollView.visibility = View.VISIBLE
        binding.paidTxt.text = "${getString(R.string.paid)} ₹" + details.totalAmount
        binding.timeTxt.text = "${details.count} ${if (details.count == 1) "${getString(R.string.person_for)} " else "${getString(R.string.persons_for)} "}${details.duration}"
        binding.planNameTxt.text = details.name
        binding.stratedTxt.text = "${getString(R.string.started)}:\n" + details.fromDate
        binding.expiryDateTxt.text = "${getString(R.string.expires_on)} : " + details.toDate
        if (details.invoice.isNotEmpty()) {
            invoice = details.invoice
            binding.invoiceLl.visibility = View.VISIBLE
        }
    }

    fun downloadAndOpenPdf(context: Context, url: String, fileName: String = "invoice.pdf") {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Invoice Download")
            .setDescription("Downloading your invoice...")
            .setMimeType("application/pdf")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        // Listen for download completion
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val cursor: Cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            val uriString = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
                            val fileUri = Uri.parse(uriString)

                            // Open PDF with any viewer
                            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(fileUri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY)
                            }
                            try {
                                context.startActivity(openIntent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No PDF viewer found!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    cursor.close()
                    // Unregister receiver after handling
                    context.unregisterReceiver(this)
                }
            }
        }

        ContextCompat.registerReceiver(
            context,
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }
}