package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.DoctorBookingItemBinding
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.interfaces.OnDoctorBookingItemClickListener
import com.iprism.swen.models.surgeondoctorbookings.HistoryItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.DoctorBookingViewHolder
import com.iprism.swen.viewholders.ItemLoadingViewHolder

class SurgeonDoctorBookingsAdapter(private var bookings : ArrayList<HistoryItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onDoctorBookingItemClickListener: OnDoctorBookingItemClickListener? = null

    fun setOnDoctorItemClickListener(onDoctorBookingItemClickListener: OnDoctorBookingItemClickListener?) {
        this.onDoctorBookingItemClickListener = onDoctorBookingItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (bookings[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DoctorBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            DoctorBookingViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DoctorBookingViewHolder) {
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
                onDoctorBookingItemClickListener!!.onItemClicked(booking.bookingId)
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
