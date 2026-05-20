package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.FilterCatItemBinding
import com.iprism.medrayder.interfaces.OnFilterCatItemClickListener
import com.iprism.medrayder.interfaces.OnFilterSpecialityItemActionListener
import com.iprism.medrayder.models.filters.DetailsItem
import com.iprism.medrayder.viewholders.FilterCatViewHolder

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
