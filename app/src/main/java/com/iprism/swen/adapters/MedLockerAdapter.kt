package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.databinding.MedLockerItemBinding
import com.iprism.swen.interfaces.OnDocumentItemClickListener
import com.iprism.swen.models.medlocker.MedLockerItem
import com.iprism.swen.viewholders.MedLockerViewHolder

class MedLockerAdapter(private val items : List<MedLockerItem>) : RecyclerView.Adapter<MedLockerViewHolder>() {

    private var onDocumentItemClickListener: OnDocumentItemClickListener? = null

    fun setOnDocumentItemClickListener(onDocumentItemClickListener: OnDocumentItemClickListener?) {
        this.onDocumentItemClickListener = onDocumentItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedLockerViewHolder {
        val binding = MedLockerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedLockerViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MedLockerViewHolder, position: Int) {
        val item = items[position]
        holder.binding.nameTxt.text = item.name
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onDocumentItemClickListener!!.onItemClicked(item)
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }
}