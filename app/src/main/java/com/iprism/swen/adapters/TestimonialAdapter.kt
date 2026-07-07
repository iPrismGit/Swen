package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.VideoItemBinding
import com.iprism.swen.interfaces.TestimonialClickListener
import com.iprism.swen.models.Video
import com.iprism.swen.utils.Constants


class TestimonialAdapter(var context: Context, var videos : List<Video>) : RecyclerView.Adapter<TestimonialAdapter.TestimonialViewHolder>() {

    private lateinit var listener: TestimonialClickListener

    fun setupListener(listener: TestimonialClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TestimonialAdapter.TestimonialViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestimonialViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: TestimonialAdapter.TestimonialViewHolder,
        position: Int
    ) {
        val video = videos[position]
        holder.binding.videoLinkTxt.text = "Video Link : " + video.link
        if (video.image.isNotEmpty()) {
            Glide.with(context).load(Constants.IMAGES_BASE_URL + video.image)
                .placeholder(R.drawable.swen_logo)
                .into(holder.binding.thumbnailIv)
        } else {
            holder.binding.thumbnailIv.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.swen_logo
                )
            )
        }
        holder.binding.root.setOnClickListener { view ->
            listener.onVideoClick(video.link)
        }
    }

    override fun getItemCount(): Int {
      return videos.size
    }

    class TestimonialViewHolder(var binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root)

}