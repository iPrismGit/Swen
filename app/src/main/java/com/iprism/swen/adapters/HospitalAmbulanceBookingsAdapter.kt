package com.iprism.swen.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.databinding.HospitalAmbulanceBookingItemBinding
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.interfaces.OnAmbulanceBookingItemClickListener
import com.iprism.swen.models.hospitalambulancebookings.HistoryItem
import com.iprism.swen.viewholders.HospitalAmbulanceBookingViewHolder
import com.iprism.swen.viewholders.ItemLoadingViewHolder

class HospitalAmbulanceBookingsAdapter(var bookings: ArrayList<HistoryItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onAmbulanceBookingItemClickListener: OnAmbulanceBookingItemClickListener? = null

    fun setOnBookingItemClickListener(onBookingItemClickListener: OnAmbulanceBookingItemClickListener?) {
        this.onAmbulanceBookingItemClickListener = onBookingItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (bookings[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = HospitalAmbulanceBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            HospitalAmbulanceBookingViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HospitalAmbulanceBookingViewHolder) {
            val booking = bookings[position]
            holder.binding.hospitalNameTxt.text = booking!!.hospitalName
            holder.binding.bookingIdTxt.text = booking.bookingId
            holder.binding.locationTxt.text = booking.location
            if (booking.ambulanceDriverName.isEmpty()) {
                holder.binding.ambulanceAssignTxt.visibility = View.VISIBLE
                holder.binding.callLl.visibility = View.GONE
                holder.binding.trackAmbulanceTxt.visibility = View.GONE
            } else {
                holder.binding.ambulanceAssignTxt.visibility = View.GONE
                holder.binding.callLl.visibility = View.VISIBLE
                holder.binding.trackAmbulanceTxt.visibility = View.VISIBLE
            }
            if (booking.bookingStatus.equals("completed", true)) {
                holder.binding.trackAmbulanceTxt.setBackgroundColor(Color.parseColor("#DADADA"))
            }
            holder.binding.trackAmbulanceTxt.setOnClickListener(View.OnClickListener {
                onAmbulanceBookingItemClickListener!!.onTrackClicked(booking)
            })
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onAmbulanceBookingItemClickListener!!.onItemClicked(booking)
            })
            holder.binding.callLl.setOnClickListener(View.OnClickListener {
                onAmbulanceBookingItemClickListener!!.onCallClicked(booking.ambulanceDriverMobile)
            })
        }
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
