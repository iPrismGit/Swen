package com.iprism.medrayder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.databinding.CategoryItemBinding
import com.iprism.medrayder.interfaces.OnItemClickListener
import com.iprism.medrayder.models.homepage.CategoriesItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.CategoryViewHolder

class MedCategoriesAdapter(private var medCategories: List<CategoriesItem>) : RecyclerView.Adapter<CategoryViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnArtistActionListener(onArtistActionListener: OnItemClickListener?) {
        this.onItemClickListener = onArtistActionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val medCategory = medCategories[position]
        holder.binding.catName.text = medCategory.name
        if (medCategory.image.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Constants.IMAGES_BASE_URL + medCategory.image)
                .into(holder.binding.catImg)
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onItemClickListener!!.onItemClicked(medCategory)
        })
    }

    override fun getItemCount(): Int {
        return medCategories.size
    }
}
