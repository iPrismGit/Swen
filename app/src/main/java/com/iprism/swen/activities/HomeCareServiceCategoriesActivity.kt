package com.iprism.swen.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.adapters.HomeCareServiceCategoriesAdapter
import com.iprism.swen.databinding.ActivityHomeCareServiceCategoriesBinding
import com.iprism.swen.interfaces.OnServiceItemClickListener

class HomeCareServiceCategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeCareServiceCategoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeCareServiceCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        setupAdapter()
    }

    private fun setupAdapter() {
        val adapter =
            HomeCareServiceCategoriesAdapter()
        val layoutManager = GridLayoutManager(this, 4)
        binding.categoriesRv.adapter = adapter
        binding.categoriesRv.layoutManager = layoutManager
        adapter.setupListener(object : OnServiceItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@HomeCareServiceCategoriesActivity, HomeCareServiceSubCategoriesActivity::class.java)
                startActivity(intent)
            }

        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { p0 ->
            finish()
        }
    }

}