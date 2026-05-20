package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.databinding.LabTestBookingItemBinding
import com.iprism.swen.interfaces.OnBookingItemClickListener
import com.iprism.swen.models.labtestbookings.HistoryItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.ItemLoadingViewHolder
import com.iprism.swen.viewholders.LabTestBookingViewHolder

class LabTestBookingsAdapter(var bookings: ArrayList<HistoryItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onBookingItemClickListener: OnBookingItemClickListener? = null

    fun setOnBookingItemClickListener(onBookingItemClickListener: OnBookingItemClickListener?) {
        this.onBookingItemClickListener = onBookingItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (bookings[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LabTestBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            LabTestBookingViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LabTestBookingViewHolder) {
            val context = holder.binding.root.context
            val booking = bookings[position]
            if (booking!!.bookingType.equals("product_booking", true)) {
                holder.binding.slotDetailsTxt.visibility = View.VISIBLE
                holder.binding.slotDateTimeTxt.text = "${booking.date}, ${booking.time}"
                holder.binding.fastingTxt.visibility = View.VISIBLE
                holder.binding.testNameTxt.text = booking.packages.packageName
                if (booking.packages.fasting.equals("yes", true)) {
                    holder.binding.fastingTxt.text = context.getString(R.string.fasting_required)
                } else {
                    holder.binding.fastingTxt.text = context.getString(R.string.fasting_not_required)
                }
            } else {
                holder.binding.slotDetailsTxt.visibility = View.GONE
                holder.binding.slotDateTimeTxt.text = context.getString(R.string.prescription_order)
                holder.binding.fastingTxt.visibility = View.GONE
                holder.binding.testNameTxt.text = context.getString(R.string.prescription_order)
            }
            holder.binding.nameTxt.text = booking.name
            holder.binding.bookingIdTxt.text = booking.bookingId
            if (booking.logo.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(Constants.IMAGES_BASE_URL + booking.logo)
                    .into(holder.binding.labTestImg)
            }
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onBookingItemClickListener!!.onItemClicked(booking.bookingId, booking.bookingType)
            })
        }
        /*holder.binding.root.setOnClickListener(View.OnClickListener {
            holder.binding.root.context.startActivity(Intent(holder.binding.root.context, LabTestBookingDetailsActivity::class.java))
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