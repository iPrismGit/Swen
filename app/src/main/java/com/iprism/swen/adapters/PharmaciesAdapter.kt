package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.databinding.MedicinePharmacyItemBinding
import com.iprism.swen.interfaces.OnPharmacyItemClickListener
import com.iprism.swen.models.pharmacies.MainDataItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.ItemLoadingViewHolder
import com.iprism.swen.viewholders.PharmacyViewHolder

class PharmaciesAdapter(var pharmacies: ArrayList<MainDataItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onPharmacyItemClickListener: OnPharmacyItemClickListener? = null

    fun setOnDoctorItemClickListener(onPharmacyItemClickListener: OnPharmacyItemClickListener?) {
        this.onPharmacyItemClickListener = onPharmacyItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (pharmacies[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MedicinePharmacyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            PharmacyViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PharmacyViewHolder) {
            val context = holder.binding.root.context
            val pharmacy = pharmacies[position]
            holder.binding.nameTxt.text = pharmacy!!.name
            holder.binding.timingsTxt.text = "${context.getString(R.string.opens)} ${pharmacy.openTime} / ${context.getString(R.string.closed)} ${pharmacy.closeTime}"
            if (pharmacy.logo.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(Constants.IMAGES_BASE_URL + pharmacy.logo)
                    .into(holder.binding.profileImg)
            }
            holder.binding.locationTxt.text = pharmacy.location
            if (pharmacy.homeDelivery.equals("yes", true)) {
                holder.binding.deliveryTypeTxt.text = context.getString(R.string.home_delivery)
                holder.binding.deliveryTypeTxt.background = ContextCompat.getDrawable(holder.binding.root.context, R.drawable.home_delivery_bg)
            } else {
                holder.binding.deliveryTypeTxt.text = context.getText(R.string.pickup_order)
                holder.binding.deliveryTypeTxt.background = ContextCompat.getDrawable(holder.binding.root.context, R.drawable.order_picup_bg)
            }
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onPharmacyItemClickListener!!.onItemClicked(pharmacy.id.toString(), pharmacy.homeDelivery)
            })
        }
    }

    fun showLoadingFooter() {
        pharmacies.add(null)
        notifyItemInserted(pharmacies.size - 1)
    }

    fun removeLoadingFooter() {
        val index = pharmacies.indexOf(null)
        if (index != -1) {
            pharmacies.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun getItemCount(): Int {
        return pharmacies.size
    }
}