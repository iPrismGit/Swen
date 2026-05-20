package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.activities.PdfViewActivity
import com.iprism.swen.activities.ViewDocumentsActivity
import com.iprism.swen.databinding.HospitalMedicineBookingItemBinding
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.interfaces.OnBookingItemClickListener
import com.iprism.swen.models.hospitalmedicneongoing.BookingsItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.HospitalMedicineBookingViewHolder
import com.iprism.swen.viewholders.ItemLoadingViewHolder

class HospitalMedicineBookingsAdapter(var bookings: ArrayList<BookingsItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        val binding = HospitalMedicineBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            HospitalMedicineBookingViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HospitalMedicineBookingViewHolder) {
            val booking = bookings[position]
            if (booking!!.bookingType.equals("product_booking", true)) {
                holder.binding.productBookingLl.visibility = View.VISIBLE
                holder.binding.prescriptionBookingLl.visibility = View.GONE
                holder.binding.hospitalNameTxt.text = booking.hospitalName
                if (booking.products.isNotEmpty()) {
                    holder.binding.nameTxt.text = booking.products[0].productName ?: ""
                    if (booking.products[0].image.isNotEmpty()) {
                        Glide.with(holder.itemView.context)
                            .load(Constants.IMAGES_BASE_URL + booking.products[0].image)
                            .into(holder.binding.productImg)
                    }
                }
            } else {
                holder.binding.productBookingLl.visibility = View.GONE
                holder.binding.prescriptionBookingLl.visibility = View.VISIBLE
                holder.binding.prescriptionImgTxt.text = "Prescription.img"
            }
            holder.binding.bookingId.text = booking.bookingId
            if (booking.orderType.equals("home_delivery", true)) {
                holder.binding.arrivingDateTxt.text = holder.binding.root.context.getString(R.string.home_delivery)
            } else {
                holder.binding.arrivingDateTxt.text = holder.binding.root.context.getString(R.string.in_store_pickup)
            }
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onBookingItemClickListener!!.onItemClicked(booking.bookingId, booking.bookingType)
            })
            holder.binding.viewTxt.setOnClickListener(View.OnClickListener {
                if (!booking.image.endsWith(".pdf")) {
                    val intent = Intent(holder.binding.root.context, ViewDocumentsActivity::class.java)
                    val imageList = ArrayList<String>()
                    imageList.add(booking.image)
                    intent.putStringArrayListExtra("images", imageList)
                    intent.putExtra("name", holder.binding.root.context.getString(R.string.prescription))
                    holder.binding.root.context.startActivity(intent)
                } else {
                    val intent = Intent(holder.binding.root.context, PdfViewActivity::class.java)
                    intent.putExtra("pdfUrl", booking.image)
                    intent.putExtra("name", holder.binding.root.context.getString(R.string.prescription))
                    holder.binding.root.context.startActivity(intent)
                }
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