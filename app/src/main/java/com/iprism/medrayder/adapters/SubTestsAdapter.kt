package com.iprism.medrayder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.databinding.SubTestItemBinding
import com.iprism.medrayder.databinding.TestItemBinding
import com.iprism.medrayder.models.diagnostictests.SubTestsItem
import com.iprism.medrayder.models.diagnostictests.TestsItem
import com.iprism.medrayder.viewholders.SubTestViewHolder
import com.iprism.medrayder.viewholders.TestViewHolder

class SubTestsAdapter(var tests : List<SubTestsItem>) : RecyclerView.Adapter<SubTestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTestViewHolder {
        val binding = SubTestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubTestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubTestViewHolder, position: Int) {
        val test = tests[position]
        holder.binding.subTestNameTxt.text = test.name
    }

    override fun getItemCount(): Int {
        return tests.size
    }
}
