package com.iprism.swen.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.SliderImageItemBinding
import com.iprism.swen.models.hospitaldetails.ImagesItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.SliderImageViewHolder
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderImagesAdapter(var images : List<ImagesItem>) : SliderViewAdapter<SliderImageViewHolder>() {

    override fun onCreateViewHolder(parent : ViewGroup?): SliderImageViewHolder {
        val binding = SliderImageItemBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return SliderImageViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: SliderImageViewHolder?, position: Int) {
        var image = images[position]
        if (image.url.isNullOrEmpty()) {
            Glide.with(viewHolder!!.itemView.context)
                .load(R.drawable.hospital_dummy_img)
                .into(viewHolder.binding.bannerImg)
        } else {
            Glide.with(viewHolder!!.itemView.context)
                .load(Constants.IMAGES_BASE_URL + image.url)
                .into(viewHolder.binding.bannerImg)
        }
    }

    override fun getCount(): Int {
        return images.size
    }
}