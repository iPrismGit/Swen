package com.iprism.swen.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.activities.OnlineDoctorBookingDetailsActivity
import com.iprism.swen.databinding.OnlineDoctorBookingItemBinding
import com.iprism.swen.viewholders.OnlineDoctorBookingViewHolder

class OnlineDoctorBookingsAdapterDummy(var images: Int) : RecyclerView.Adapter<OnlineDoctorBookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineDoctorBookingViewHolder {
        val binding = OnlineDoctorBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnlineDoctorBookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnlineDoctorBookingViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            holder.binding.root.context.startActivity(Intent(holder.binding.root.context, OnlineDoctorBookingDetailsActivity::class.java))
        })
    }

    override fun getItemCount(): Int = 8
}
