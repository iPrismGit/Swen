package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.DiagnosticTestBookingItemBinding
import com.iprism.medrayder.databinding.ItemLoadingBinding
import com.iprism.medrayder.interfaces.OnBookingItemClickListener
import com.iprism.medrayder.models.diagnostictestbookings.HistoryItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.DiagnosticTestBookingViewHolder
import com.iprism.medrayder.viewholders.ItemLoadingViewHolder

class DiagnosticTestBookingsAdapter(var bookings: ArrayList<HistoryItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        val binding = DiagnosticTestBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            DiagnosticTestBookingViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DiagnosticTestBookingViewHolder) {
            val context = holder.binding.root.context
            val booking = bookings[position]
            if (booking!!.bookingType.equals("product_booking", true)) {
                holder.binding.slotDetailsTxt.visibility = View.VISIBLE
                holder.binding.slotDateTimeTxt.text = "${booking.date}, ${booking.time}"
                holder.binding.testNameTxt.text = booking.packages.packageName
                holder.binding.testNameTxt.visibility = View.VISIBLE
            } else {
                holder.binding.slotDetailsTxt.visibility = View.GONE
                holder.binding.slotDateTimeTxt.text = context.getString(R.string.prescription_order)
                holder.binding.testNameTxt.visibility = View.GONE
            }
            holder.binding.nameTxt.text = booking.name
            holder.binding.bookingIdTxt.text = booking.bookingId
            holder.binding.locationTxt.text = booking.location
            if (booking.logo.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(Constants.IMAGES_BASE_URL + booking.logo)
                    .into(holder.binding.diagnosticTestImg)
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
