package com.iprism.medrayder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.databinding.WelcomeItemBinding
import com.iprism.medrayder.viewholders.WelcomeItemViewHolder

class ImageSliderAdapter(private val images: List<Int>) : RecyclerView.Adapter<WelcomeItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeItemViewHolder {
        val binding = WelcomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WelcomeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WelcomeItemViewHolder, position: Int) {
        holder.binding.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int = images.size
}
