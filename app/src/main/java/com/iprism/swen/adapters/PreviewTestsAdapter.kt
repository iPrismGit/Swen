package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.swen.databinding.PreviewMedcineTestItemBinding
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.TestsItem

class PreviewTestsAdapter(private var testsItems: List<TestsItem>):Adapter<PreviewTestsAdapter.PreviewMedicineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewTestsAdapter.PreviewMedicineViewHolder {
        val binding = PreviewMedcineTestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreviewMedicineViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PreviewTestsAdapter.PreviewMedicineViewHolder, position: Int) {
        val testsItem = testsItems[position]
        val count = position + 1
        holder.binding.nameTxt.text = count.toString() + ". " + testsItem.test.replaceFirstChar { it.uppercase() }
        holder.binding.timeTxt.text = testsItem.testInstruction.replaceFirstChar { it.uppercase() }
    }

    override fun getItemCount(): Int {
       return testsItems.size
    }

    class PreviewMedicineViewHolder(var binding: PreviewMedcineTestItemBinding) : ViewHolder(binding.root)
}