package com.iprism.medrayder.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iprism.medrayder.databinding.SliderImageItemBinding
import com.iprism.medrayder.models.homepage.BannersItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.SliderImageViewHolder
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