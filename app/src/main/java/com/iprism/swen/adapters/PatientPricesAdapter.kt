package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.R
import com.iprism.swen.databinding.PatientPriceItemBinding
import com.iprism.swen.interfaces.OnPatientPriceItemClickListener
import com.iprism.swen.models.diagnostictimings.Price
import com.iprism.swen.viewholders.PatientPriceViewHolder

class PatientPricesAdapter(private var prices : ArrayList<Price>) : RecyclerView.Adapter<PatientPriceViewHolder>() {

    private var selectedItem = -1

    private var onPatientPriceItemClickListener: OnPatientPriceItemClickListener? = null

    fun setOnPatientPriceItemClickListener(onPatientPriceItemClickListener: OnPatientPriceItemClickListener?) {
        this.onPatientPriceItemClickListener = onPatientPriceItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientPriceViewHolder {
        val binding = PatientPriceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientPriceViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PatientPriceViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val price = prices[position]
        val context = holder.itemView.context
        holder.binding.previousPriceTxt.text = "₹${price.price}"
        holder.binding.patientCountTxt.text = "${price.patientCount} ${if (price.patientCount == 1) context.getString(R.string.patient) else context.getString(R.string.patients)}"
        holder.binding.previousPriceTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.priceTxt.text = "₹${price.discontPrice}"
        if (selectedItem == position) {
            holder.binding.rb.setImageResource(R.drawable.radio_button_clicked_img)
        } else {
            holder.binding.rb.setImageResource(R.drawable.radio_button_img)
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onPatientPriceItemClickListener!!.onItemClicked(price)
            val previousItem = selectedItem
            selectedItem = position
            notifyItemChanged(previousItem)
            notifyItemChanged(selectedItem)
        })
    }

    override fun getItemCount(): Int {
        return prices.size
    }
}
