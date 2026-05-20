package com.iprism.medrayder.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.databinding.TestItemBinding
import com.iprism.medrayder.models.diagnostictests.TestsItem
import com.iprism.medrayder.viewholders.TestViewHolder

class TestsAdapter(var tests : List<TestsItem>) : RecyclerView.Adapter<TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val binding = TestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val test = tests[position]
        Log.d("TestsAdapter", "Binding test: ${test.name}, subtests: ${test.subTests?.size}")
        holder.binding.testNameTxt.text = test.name
        holder.binding.subTestsRv.layoutManager = LinearLayoutManager(holder.binding.subTestsRv.context)
        val subTestsAdapter = SubTestsAdapter(test.subTests)
        holder.binding.subTestsRv.adapter = subTestsAdapter
    }

    override fun getItemCount(): Int {
        return tests.size
    }
}
