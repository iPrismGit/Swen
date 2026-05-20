package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.databinding.TreatmentPlanningItemBinding
import com.iprism.swen.interfaces.OnTreatmentPlaningItemClickListener
import com.iprism.swen.models.treatmentplaning.TreatmentPlanningItem
import com.iprism.swen.viewholders.TreatmentPlanningViewHolder

class TreatmentPlaningAdapter(private val items : List<TreatmentPlanningItem>) : RecyclerView.Adapter<TreatmentPlanningViewHolder>() {

    private var itemClickListener: OnTreatmentPlaningItemClickListener? = null

    fun setOnDocumentItemClickListener(itemClickListener: OnTreatmentPlaningItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentPlanningViewHolder {
        val binding = TreatmentPlanningItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TreatmentPlanningViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TreatmentPlanningViewHolder, position: Int) {
        val item = items[position]
        holder.binding.nameTxt.text = item.name
        holder.binding.root.setOnClickListener(View.OnClickListener {
            itemClickListener!!.onItemClicked(item)
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }
}