package com.iprism.swen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.R
import com.iprism.swen.adapters.PdfsAdapter
import com.iprism.swen.databinding.ActivityPdfListBinding
import com.iprism.swen.interfaces.PdfItemClickListener
import com.iprism.swen.models.labtestbookingdetails.ReportsItem

class PdfListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPdfListBinding
    private var reports : List<ReportsItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        if (intent.hasExtra("reports")) {
            reports = intent.getSerializableExtra("reports") as List<ReportsItem>
            setupReports(reports!!)
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setupReports(reports : List<ReportsItem>) {
        val pdfsAdapter = PdfsAdapter(reports)
        binding.pdfsRv.layoutManager = GridLayoutManager(this, 2)
        binding.pdfsRv.adapter = pdfsAdapter
        pdfsAdapter.setOnPdfItemClickListener(object : PdfItemClickListener {
            override fun onItemClicked(pdfUrl: String) {
                val intent = Intent(this@PdfListActivity, PdfViewActivity::class.java)
                intent.putExtra("pdfUrl", pdfUrl)
                intent.putExtra("name", getString(R.string.reports))
                startActivity(intent)
            }
        })
    }
}