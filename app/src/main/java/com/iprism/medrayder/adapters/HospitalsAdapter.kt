package com.iprism.medrayder.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.R
import com.iprism.medrayder.activities.HospitalDetailsActivity
import com.iprism.medrayder.databinding.HospitalItemBinding
import com.iprism.medrayder.databinding.ItemLoadingBinding
import com.iprism.medrayder.interfaces.OnHospitalItemClickListener
import com.iprism.medrayder.models.hospitals.MainDataItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.DoctorItemViewHolder
import com.iprism.medrayder.viewholders.HospitalItemViewHolder
import com.iprism.medrayder.viewholders.ItemLoadingViewHolder

class HospitalsAdapter(var hospitals: ArrayList<MainDataItem?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onHospitalItemClickListener: OnHospitalItemClickListener? = null

    fun setOnDoctorItemClickListener(onHospitalItemClickListener: OnHospitalItemClickListener?) {
        this.onHospitalItemClickListener = onHospitalItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hospitals[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = HospitalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            HospitalItemViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HospitalItemViewHolder) {
            val hospital = hospitals[position]
            holder.binding.nameTxt.text = hospital!!.name
            holder.binding.specialityTxt.text = hospital.tagline
            if (hospital.logo.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(Constants.IMAGES_BASE_URL + hospital.logo)
                    .into(holder.binding.hospitalImg)
            }
            holder.binding.locationTxt.text = hospital.location
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onHospitalItemClickListener!!.onItemClicked(hospital.id)
            })
        }
    }

    override fun getItemCount(): Int {
        return hospitals.size
    }

    fun showLoadingFooter() {
        hospitals.add(null)
        notifyItemInserted(hospitals.size - 1)
    }

    fun removeLoadingFooter() {
        val index = hospitals.indexOf(null)
        if (index != -1) {
            hospitals.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
