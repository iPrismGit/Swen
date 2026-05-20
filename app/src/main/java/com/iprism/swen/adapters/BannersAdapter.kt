package com.iprism.swen.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iprism.swen.databinding.SliderImageItemBinding
import com.iprism.swen.models.homepage.BannersItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.SliderImageViewHolder
import com.smarteist.autoimageslider.SliderViewAdapter

class BannersAdapter(var images : List<BannersItem>) : SliderViewAdapter<SliderImageViewHolder>() {

    override fun onCreateViewHolder(parent : ViewGroup?): SliderImageViewHolder {
        val binding = SliderImageItemBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return SliderImageViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: SliderImageViewHolder?, position: Int) {
        val image = images[position]
        if (image.image.isNotEmpty()) {
            Glide.with(viewHolder!!.itemView.context)
                .load(Constants.IMAGES_BASE_URL + image.image)
                .into(viewHolder.binding.bannerImg)
        }
    }

    override fun getCount(): Int {
        return images.size
    }
}