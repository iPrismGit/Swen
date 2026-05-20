package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.R
import com.iprism.medrayder.activities.OnlineDoctorBookingDetailsActivity
import com.iprism.medrayder.databinding.DoctorItemBinding
import com.iprism.medrayder.databinding.ItemLoadingBinding
import com.iprism.medrayder.databinding.OnlineDoctorBookingItemBinding
import com.iprism.medrayder.interfaces.OnDoctorItemClickListener
import com.iprism.medrayder.interfaces.OnOnlineDoctorBookingItemClickListener
import com.iprism.medrayder.models.onlinedoctorbookinghistory.HistoryItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.DoctorItemViewHolder
import com.iprism.medrayder.viewholders.ItemLoadingViewHolder
import com.iprism.medrayder.viewholders.OnlineDoctorBookingViewHolder

class OnlineDoctorBookingsAdapter(private var bookings : ArrayList<HistoryItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onOnlineDoctorBookingItemClickListener: OnOnlineDoctorBookingItemClickListener? = null

    fun setOnDoctorItemClickListener(onOnlineDoctorBookingItemClickListener: OnOnlineDoctorBookingItemClickListener?) {
        this.onOnlineDoctorBookingItemClickListener = onOnlineDoctorBookingItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (bookings[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = OnlineDoctorBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            OnlineDoctorBookingViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OnlineDoctorBookingViewHolder) {
            val context = holder.binding.root.context
            val booking = bookings[position]
            holder.binding.nameTxt.text = booking!!.name
            holder.binding.categoryTxt.text = booking.specialization
            holder.binding.bookingId.text = booking.bookingId
            holder.binding.slotDateTimeTxt.text = booking.date + ", " + booking.time
            if (booking.image.isNotEmpty()) {
                Glide.with(context)
                    .load(Constants.IMAGES_BASE_URL + booking.image)
                    .into(holder.binding.doctorImg)
            }
            holder.binding.studyTxt.text = booking.qualification
            holder.binding.priceTxt.text = "₹" + booking.consultationFee
            holder.binding.consultationsCountTxt.text = "${booking.consultations} ${if (booking.consultations.toInt() > 1) context.getString(R.string.consultations) else context.getString(R.string.consultation)}"
            holder.binding.experienceTxt.text =
                "${booking.exp} ${if (booking.exp.toInt() > 1) context.getString(R.string.years) else context.getString(R.string.year)}"
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onOnlineDoctorBookingItemClickListener!!.onItemClicked(booking.bookingId)
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateBookings(newBookings: List<HistoryItem>) {
        bookings = newBookings as ArrayList<HistoryItem?>
        notifyDataSetChanged()
    }
}
