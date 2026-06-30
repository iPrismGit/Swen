package com.iprism.swen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.databinding.DoctorTypeItemBinding
import com.iprism.swen.interfaces.OnDoctorWithSymptomsSpecialityClickListener
import com.iprism.swen.models.allsurgeryquotes.ResponseItem
import com.iprism.swen.utils.Constants

class AllSurgicalQuoteCategoriesAdapter(var symptoms : ArrayList<ResponseItem>) : RecyclerView.Adapter<AllSurgicalQuoteCategoriesAdapter.ViewAllDoctorViewHolder>() {

    private lateinit var listener: OnDoctorWithSymptomsSpecialityClickListener

    fun setupListener(listener: OnDoctorWithSymptomsSpecialityClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllSurgicalQuoteCategoriesAdapter.ViewAllDoctorViewHolder {
        val binding = DoctorTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewAllDoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllSurgicalQuoteCategoriesAdapter.ViewAllDoctorViewHolder, position: Int) {
        val symptom = symptoms[position]
        holder.binding.categoryName.text = symptom.name
        Glide.with(holder.itemView.context)
            .load(Constants.IMAGES_BASE_URL + symptom.image)
            .into(holder.binding.categoryImg)
        holder.binding.root.setOnClickListener { view ->
            listener.onItemClicked(symptom.id.toString(), symptom.name)
        }
    }

    override fun getItemCount(): Int {
        return symptoms.size
    }

    class ViewAllDoctorViewHolder(var binding: DoctorTypeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}