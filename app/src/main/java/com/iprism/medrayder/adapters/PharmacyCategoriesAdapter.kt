package com.iprism.medrayder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.databinding.PharmacyCategoryItemBinding
import com.iprism.medrayder.interfaces.OnPharmacyCatItemClickListener
import com.iprism.medrayder.models.pharmacyDetails.PharmacyCategory
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.PharmacyCatViewHolder

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
