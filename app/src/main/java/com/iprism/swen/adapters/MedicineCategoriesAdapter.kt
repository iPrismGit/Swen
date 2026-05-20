package com.iprism.swen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.databinding.MedicineCategoryItemBinding
import com.iprism.swen.interfaces.OnWellnessCatItemClickListener
import com.iprism.swen.models.homepage.PharmacyCategoriesItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.MedicineCatViewHolder

class MedicineCategoriesAdapter(private var pharmacyCategories : List<PharmacyCategoriesItem>) : RecyclerView.Adapter<MedicineCatViewHolder>() {

    private var onItemClickListener: OnWellnessCatItemClickListener? = null

    fun setOnArtistActionListener(actionListener: OnWellnessCatItemClickListener?) {
        this.onItemClickListener = actionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineCatViewHolder {
        val binding = MedicineCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicineCatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicineCatViewHolder, position: Int) {
        val pharmacyCategories = pharmacyCategories[position]
        holder.binding.catName.text = pharmacyCategories.name
        if (pharmacyCategories.image.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Constants.IMAGES_BASE_URL + pharmacyCategories.image)
                .into(holder.binding.catImg)
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onItemClickListener!!.onItemClicked(pharmacyCategories)
        })
    }

    override fun getItemCount(): Int {
        return pharmacyCategories.size
    }
}
