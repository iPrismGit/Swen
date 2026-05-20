package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.databinding.PatientDetailsItemBinding
import com.iprism.medrayder.models.labtestbookingdetails.FamilyMembersItem
import com.iprism.medrayder.viewholders.PatientDetailsViewHolder

class PatientDetailsAdapter(private val items : List<FamilyMembersItem>) : RecyclerView.Adapter<PatientDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientDetailsViewHolder {
        val binding = PatientDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientDetailsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PatientDetailsViewHolder, position: Int) {
        val item = items[position]
        holder.binding.nameTxt.text = item.name
        holder.binding.mobileTxt.text = item.mobile
        holder.binding.dobTxt.text = item.dob
        holder.binding.genderTxt.text = item.gender.replaceFirstChar { it.uppercaseChar() }
        holder.binding.gmailTxt.text = item.email
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
