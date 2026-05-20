package com.iprism.medrayder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.databinding.FilterSubcatItemBinding
import com.iprism.medrayder.interfaces.OnFilterSpecialityItemActionListener
import com.iprism.medrayder.models.filters.SpecialitiesItem
import com.iprism.medrayder.viewholders.FilterSubCatViewHolder

class FilterSubCatsAdapter : RecyclerView.Adapter<FilterSubCatViewHolder>() {

    private var items: List<SpecialitiesItem>? = null
    private var listener: OnFilterSpecialityItemActionListener? = null

    fun setListener(listener: OnFilterSpecialityItemActionListener?) {
        this.listener = listener
    }

    fun setFilterValues(items: List<SpecialitiesItem>) {
        this.items= items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterSubCatViewHolder {
        val binding = FilterSubcatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterSubCatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterSubCatViewHolder, position: Int) {
        val item = items!![position]
        holder.binding.checkBox.text = item.specialityName
        holder.binding.checkBox.isChecked = item.status
        holder.binding.checkBox.setOnClickListener(View.OnClickListener {
            val status: Boolean = holder.binding.checkBox.isChecked
            if (status) {
                val filterResponse = SpecialitiesItem(item.specialityName, item.specialityId, true)
                listener!!.onFilterValueClicked(position, filterResponse)
            } else {
                val filterResponse = SpecialitiesItem(item.specialityName, item.specialityId, false)
                listener!!.onFilterValueClicked(position, filterResponse)
            }
        })
    }

    override fun getItemCount(): Int {
        return items!!.size
    }
}
