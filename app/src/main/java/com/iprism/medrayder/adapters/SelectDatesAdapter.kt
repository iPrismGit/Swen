package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.SelectDateItemBinding
import com.iprism.medrayder.interfaces.OnDateItemClickListener
import com.iprism.medrayder.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.medrayder.viewholders.SelectDateViewHolder

class SelectDatesAdapter(private val dates : List<DatesItem>) : RecyclerView.Adapter<SelectDateViewHolder>() {

    private var selectedItem = -1

    private var onDateItemClickListener: OnDateItemClickListener? = null

    fun setOnDoctorItemClickListener(onDateItemClickListener: OnDateItemClickListener?) {
        this.onDateItemClickListener = onDateItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectDateViewHolder {
        val binding = SelectDateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectDateViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: SelectDateViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val date = dates[position]
        val parts = date.formatDate.split(" ")
        holder.binding.dayNameTxt.text = parts[0].toString()
        holder.binding.dateTxt.text = parts[1].toString()
        holder.binding.monthNameTxt.text = parts[2].toString()
        if (selectedItem == position) {
            holder.binding.cardView.strokeColor = ContextCompat.getColor(holder.binding.root.context, R.color.green)
            holder.binding.cardView.strokeWidth = 2
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#DAFFD0"))
        } else {
            holder.binding.cardView.strokeColor = ContextCompat.getColor(holder.binding.root.context, R.color.light_gray1)
            holder.binding.cardView.strokeWidth = 2
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onDateItemClickListener!!.onItemClicked(date)
            val previousItem = selectedItem
            selectedItem = position
            notifyItemChanged(previousItem)
            notifyItemChanged(selectedItem)
        })
    }

    override fun getItemCount(): Int {
        return dates.size
    }
}