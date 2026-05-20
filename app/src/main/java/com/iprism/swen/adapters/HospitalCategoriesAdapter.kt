package com.iprism.swen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.databinding.HospitalCatItemBinding
import com.iprism.swen.interfaces.OnHospitalCatItemClickListener
import com.iprism.swen.models.homepage.SubCategoriesItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.HospitalCategoryViewHolder

class HospitalCategoriesAdapter(private var hospitalCategories: List<SubCategoriesItem>) : RecyclerView.Adapter<HospitalCategoryViewHolder>() {

    private var onItemClickListener: OnHospitalCatItemClickListener? = null

    fun setOnArtistActionListener(actionListener: OnHospitalCatItemClickListener?) {
        this.onItemClickListener = actionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalCategoryViewHolder {
        val binding = HospitalCatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HospitalCategoryViewHolder, position: Int) {
        val hospitalCategory = hospitalCategories[position]
        holder.binding.catName.text = hospitalCategory.name
        if (hospitalCategory.image.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Constants.IMAGES_BASE_URL + hospitalCategory.image)
                .into(holder.binding.catImg)
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onItemClickListener!!.onItemClicked(hospitalCategory)
        })
    }

    override fun getItemCount(): Int {
        return hospitalCategories.size
    }
}
