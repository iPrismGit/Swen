package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.databinding.FilterCatItemBinding
import com.iprism.swen.interfaces.OnFilterCatItemClickListener
import com.iprism.swen.models.filters.DetailsItem
import com.iprism.swen.viewholders.FilterCatViewHolder

class FilterCatsAdapter(var cats: List<DetailsItem>) : RecyclerView.Adapter<FilterCatViewHolder>() {

    private var selectedItem = 0

    private var listener: OnFilterCatItemClickListener? = null

    fun setListener(listener: OnFilterCatItemClickListener?) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCatViewHolder {
        val binding = FilterCatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterCatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterCatViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val cat = cats[position]
        holder.binding.genderTxt.text = cat.categoryName
        if (selectedItem == position) {
            holder.binding.genderTxt.setBackgroundColor(Color.parseColor("#DAECFF"))
        } else {
            holder.binding.genderTxt.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            val previousItem = selectedItem
            selectedItem = position
            notifyItemChanged(previousItem)
            notifyItemChanged(selectedItem)
            listener!!.onItemClicked(position.toString())
        })
    }

    override fun getItemCount(): Int {
        return cats.size
    }
}
