package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.swen.databinding.PreviewMedcineTestItemBinding
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.MedicinesItem

class PreviewMedicinesAdapter(private var medicines: List<MedicinesItem>):Adapter<PreviewMedicinesAdapter.PreviewMedicineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewMedicinesAdapter.PreviewMedicineViewHolder {
        val binding = PreviewMedcineTestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreviewMedicineViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PreviewMedicinesAdapter.PreviewMedicineViewHolder, position: Int) {
        val medicine = medicines[position]
        val count = position + 1
        holder.binding.nameTxt.text = count.toString() + ". " + medicine.medicine.replaceFirstChar { it.uppercase() }
        holder.binding.timeTxt.text = medicine.medicineTime.replaceFirstChar { it.uppercase() }
    }

    override fun getItemCount(): Int {
       return medicines.size
    }

    class PreviewMedicineViewHolder(var binding: PreviewMedcineTestItemBinding) : ViewHolder(binding.root)
}