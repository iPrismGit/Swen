package com.iprism.swen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.databinding.SubTestItemBinding
import com.iprism.swen.models.diagnostictests.SubTestsItem
import com.iprism.swen.viewholders.SubTestViewHolder

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
