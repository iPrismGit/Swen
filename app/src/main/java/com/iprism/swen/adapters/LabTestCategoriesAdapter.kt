package com.iprism.swen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.databinding.LabTestItemBinding
import com.iprism.swen.interfaces.OnLabCatItemClickListener
import com.iprism.swen.models.homepage.CategoriesItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.LabTestCategoryViewHolder

class LabTestCategoriesAdapter(private var labCats : List<CategoriesItem>) : RecyclerView.Adapter<LabTestCategoryViewHolder>() {

    private var onItemClickListener: OnLabCatItemClickListener? = null

    fun setOnArtistActionListener(onArtistActionListener: OnLabCatItemClickListener?) {
        this.onItemClickListener = onArtistActionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabTestCategoryViewHolder {
        val binding = LabTestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LabTestCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabTestCategoryViewHolder, position: Int) {
        val lab = labCats[position]
        holder.binding.catName.text = lab.name
        if (lab.image.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Constants.IMAGES_BASE_URL + lab.image)
                .into(holder.binding.catImg)
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onItemClickListener!!.onItemClicked(lab)
        })
        val colors = listOf("#FFCDD2", "#C8E6C9", "#BBDEFB", "#D1C4E9")
        val bgColor = android.graphics.Color.parseColor(colors[position % colors.size])
        holder.binding.ll.setBackgroundColor(bgColor)
    }

    override fun getItemCount(): Int {
        return labCats.size
    }
}
