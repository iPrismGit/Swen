package com.iprism.swen.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.adapters.DocumentsListAdapter
import com.iprism.swen.databinding.ActivityViewDocumentsBinding
import com.iprism.swen.models.medlocker.ImagesItem

class ViewDocumentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewDocumentsBinding
    private var images : ArrayList<ImagesItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        if (intent.hasExtra("images")) {
            val images = intent.getStringArrayListExtra("images")
            binding.documentsNameTxt.text = intent.getStringExtra("name")
            setupImages(images!!)
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setupImages(items : ArrayList<String>) {
        val documentsListAdapter = DocumentsListAdapter(items)
        binding.documentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.documentsRv.adapter = documentsListAdapter
    }
}