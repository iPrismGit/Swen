package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.HospitalAdmissionBookingItemBinding
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.interfaces.OnHospitalAdmitBookingItemClickListener
import com.iprism.swen.models.hospitaladmitongoingbookings.HistoryItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.HospitalAdmissionBookingViewHolder
import com.iprism.swen.viewholders.ItemLoadingViewHolder

class HospitalAdmissionBookingsAdapter(var bookings: ArrayList<HistoryItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listener: OnHospitalAdmitBookingItemClickListener? = null

    fun setOnBookingItemClickListener(listener: OnHospitalAdmitBookingItemClickListener?) {
        this.listener = listener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (bookings[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = HospitalAdmissionBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            HospitalAdmissionBookingViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HospitalAdmissionBookingViewHolder) {
            val context = holder.binding.root.context
            val booking = bookings[position]
            holder.binding.hospitalNameTxt.text = booking!!.name
            holder.binding.bookingIdTxt.text = booking.bookingId
            holder.binding.specialityTxt.text = booking.tagline
            holder.binding.locationTxt.text = booking.location
            holder.binding.slotDateTimeTxt.text = "${booking.date}, ${booking.time}"
            if (booking.prName.isNotEmpty()) {
                holder.binding.prNameTxt.text = booking.prName
                holder.binding.callImg.visibility = View.VISIBLE
            } else {
                holder.binding.prNameTxt.text = context.getString(R.string.you_will_receive_pr_details)
                holder.binding.callImg.visibility = View.GONE
            }
            if (booking.bookingStatus.equals("completed", true)) {
                holder.binding.prLl.visibility = View.GONE
            } else {
                holder.binding.prLl.visibility = View.VISIBLE
            }
            if (booking.logo.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(Constants.IMAGES_BASE_URL + booking.logo)
                    .into(holder.binding.hospitalImg)
            } else {

            }
            holder.binding.root.setOnClickListener(View.OnClickListener {
                listener!!.onItemClicked(booking.bookingId, "")
            })
            if (!booking.prMobile.equals("0", true)) {
                holder.binding.prLl.setOnClickListener(View.OnClickListener {
                    listener!!.onCallClicked(booking.prMobile)
                })
            }
        }
        /*holder.binding.root.setOnClickListener(View.OnClickListener {
            holder.binding.root.context.startActivity(Intent(holder.binding.root.context, HospitalAdmissionBookingDetailsActivity::class.java))
        })*/
    }

    override fun getItemCount(): Int {
        return bookings.size
    }

    fun showLoadingFooter() {
        bookings.add(null)
        notifyItemInserted(bookings.size - 1)
    }

    fun removeLoadingFooter() {
        val index = bookings.indexOf(null)
        if (index != -1) {
            bookings.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
