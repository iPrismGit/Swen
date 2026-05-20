package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.R
import com.iprism.swen.databinding.TimeItemBinding
import com.iprism.swen.interfaces.OnTimeItemClickListener
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.viewholders.SelectTimeViewHolder

class SelectTimesAdapter(private val times : List<TimesItem>) : RecyclerView.Adapter<SelectTimeViewHolder>() {

    private var selectedItem = -1

    private var onTimeItemClickListener: OnTimeItemClickListener? = null

    fun setOnDoctorItemClickListener(onTimeItemClickListener: OnTimeItemClickListener?) {
        this.onTimeItemClickListener = onTimeItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectTimeViewHolder {
        val binding = TimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectTimeViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: SelectTimeViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val time = times[position]
        holder.binding.textView49.text = time.time
        if (time.bookingStatus.equals("booked", true)) {
            holder.binding.cardView.strokeColor = ContextCompat.getColor(holder.binding.root.context, R.color.light_gray1)
            holder.binding.cardView.strokeWidth = 2
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#A8A8A8"))
        }  else {
            if (selectedItem == position) {
                holder.binding.cardView.strokeColor = ContextCompat.getColor(holder.binding.root.context, R.color.green)
                holder.binding.cardView.strokeWidth = 2
                holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#DAFFD0"))
            } else {
                holder.binding.cardView.strokeColor = ContextCompat.getColor(holder.binding.root.context, R.color.light_gray1)
                holder.binding.cardView.strokeWidth = 2
                holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        }
        if (!time.bookingStatus.equals("booked", true)) {
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onTimeItemClickListener!!.onItemClicked(time)
                val previousItem = selectedItem
                selectedItem = position
                notifyItemChanged(previousItem)
                notifyItemChanged(selectedItem)
            })
        }
    }

    override fun getItemCount(): Int {
        return times.size
    }
}
