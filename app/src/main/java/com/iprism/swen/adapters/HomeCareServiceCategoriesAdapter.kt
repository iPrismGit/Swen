package com.iprism.swen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.databinding.ServiceItemBinding
import com.iprism.swen.interfaces.OnServiceItemClickListener

class HomeCareServiceCategoriesAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener : OnServiceItemClickListener

    fun setupListener(listener: OnServiceItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): RecyclerView.ViewHolder {
        val binding = ServiceItemBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return HomeCareServiceViewHolder(binding)
    }

    override fun onBindViewHolder(
        p0: RecyclerView.ViewHolder,
        p1: Int
    ) {
       val holder = p0 as HomeCareServiceViewHolder
        holder.binding.apply {
            root.setOnClickListener { p0 ->
                listener.onItemClick(p1)
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    class HomeCareServiceViewHolder(val binding: ServiceItemBinding) : RecyclerView.ViewHolder(binding.root)
}