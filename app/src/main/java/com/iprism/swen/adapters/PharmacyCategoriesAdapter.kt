package com.iprism.swen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.databinding.PharmacyCategoryItemBinding
import com.iprism.swen.interfaces.OnPharmacyCatItemClickListener
import com.iprism.swen.models.pharmacyDetails.PharmacyCategory
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.PharmacyCatViewHolder

class PharmacyCategoriesAdapter(private var pharmacyCategories: List<PharmacyCategory>) : RecyclerView.Adapter<PharmacyCatViewHolder>() {

    private var onPharmacyCatItemClickListener: OnPharmacyCatItemClickListener? = null

    fun setOnDoctorItemClickListener(onPharmacyCatItemClickListener: OnPharmacyCatItemClickListener?) {
        this.onPharmacyCatItemClickListener = onPharmacyCatItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyCatViewHolder {
        val binding = PharmacyCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PharmacyCatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PharmacyCatViewHolder, position: Int) {
        val pharmacyCategory = pharmacyCategories[position]
        holder.binding.catName.text = pharmacyCategory.name
        if (pharmacyCategory.image.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Constants.IMAGES_BASE_URL + pharmacyCategory.image)
                .into(holder.binding.catImg)
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onPharmacyCatItemClickListener!!.onItemClicked(pharmacyCategory.id.toString()
                , pharmacyCategory.name)
        })
    }

    override fun getItemCount(): Int {
        return pharmacyCategories.size
    }
}
