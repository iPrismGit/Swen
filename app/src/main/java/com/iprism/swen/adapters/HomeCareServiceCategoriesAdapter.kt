package com.iprism.swen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.databinding.ServiceItemBinding
import com.iprism.swen.interfaces.OnServiceItemClickListener
import com.iprism.swen.models.homeservices.ResponseItem
import com.iprism.swen.utils.Constants

class HomeCareServiceCategoriesAdapter(var items : ArrayList<ResponseItem>) : RecyclerView.Adapter<HomeCareServiceCategoriesAdapter.HomeCareServiceViewHolder>() {

    private lateinit var listener : OnServiceItemClickListener

    fun setupListener(listener: OnServiceItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): HomeCareServiceViewHolder {
        val binding = ServiceItemBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return HomeCareServiceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeCareServiceViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.binding.categoryName.text = item.name
        Glide.with(holder.itemView.context)
            .load(Constants.IMAGES_BASE_URL + item.image)
            .into(holder.binding.categoryImg)
        holder.binding.root.setOnClickListener { view ->
            listener.onItemClick(item.id, item.name)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class HomeCareServiceViewHolder(val binding: ServiceItemBinding) : RecyclerView.ViewHolder(binding.root)
}