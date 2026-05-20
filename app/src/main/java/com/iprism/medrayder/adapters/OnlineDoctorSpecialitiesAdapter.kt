package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.OnlineDoctorSpecialityItemBinding
import com.iprism.medrayder.interfaces.OnSpecialityItemClickListener
import com.iprism.medrayder.models.onlinedoctorspeacilities.SpecialitiesItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.OnlineDoctorSpecialityViewHolder

class OnlineDoctorSpecialitiesAdapter(private val specialitiesItems: List<SpecialitiesItem>) : RecyclerView.Adapter<OnlineDoctorSpecialityViewHolder>() {

    private var selectedItem = -1
    private var onSpecialityItemClickListener: OnSpecialityItemClickListener? = null

    fun setOnArtistActionListener(onSpecialityItemClickListener: OnSpecialityItemClickListener?) {
        this.onSpecialityItemClickListener = onSpecialityItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineDoctorSpecialityViewHolder {
        val binding = OnlineDoctorSpecialityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnlineDoctorSpecialityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnlineDoctorSpecialityViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val specialitiesItem = specialitiesItems[position]
        holder.binding.catName.text = specialitiesItem.name
        if (specialitiesItem.image.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Constants.IMAGES_BASE_URL + specialitiesItem.image)
                .into(holder.binding.catImg)
        }
        if (selectedItem == position) {
            holder.binding.cardView.strokeColor = ContextCompat.getColor(holder.binding.root.context, R.color.blue)
            holder.binding.cardView.strokeWidth = 4
        } else {
            holder.binding.cardView.strokeColor = ContextCompat.getColor(holder.binding.root.context, R.color.light_gray1)
            holder.binding.cardView.strokeWidth = 2

        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            val previousItem = selectedItem
            selectedItem = position
            notifyItemChanged(previousItem)
            notifyItemChanged(selectedItem)
            onSpecialityItemClickListener!!.onItemClicked(specialitiesItem.id, specialitiesItem.name)
        })
    }

    override fun getItemCount(): Int {
        return specialitiesItems.size
    }
}
