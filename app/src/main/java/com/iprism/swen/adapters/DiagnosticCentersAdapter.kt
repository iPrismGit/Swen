package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.databinding.LabTestCenterItemBinding
import com.iprism.swen.interfaces.OnDiagnosticItemClickListener
import com.iprism.swen.models.dignosticcenters.MainDataItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.ItemLoadingViewHolder
import com.iprism.swen.viewholders.LabTestCenterViewHolder

class DiagnosticCentersAdapter(private var diagnostics: ArrayList<MainDataItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onDiagnosticItemClickListener: OnDiagnosticItemClickListener? = null

    fun setOnDoctorItemClickListener(onHospitalItemClickListener: OnDiagnosticItemClickListener?) {
        this.onDiagnosticItemClickListener = onHospitalItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (diagnostics[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LabTestCenterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            LabTestCenterViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LabTestCenterViewHolder) {
            val context = holder.binding.root.context
            val diagnostic = diagnostics[position]
            if (diagnostic!!.logo.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(Constants.IMAGES_BASE_URL + diagnostic.logo)
                    .into(holder.binding.profileImg)
            }
            holder.binding.nameTxt.text = diagnostic.name
            holder.binding.locationTxt.text = diagnostic.location
            holder.binding.timingsTxt.text = "${context.getString(R.string.opens)} ${diagnostic.openTime} / ${context.getString(R.string.closed)} ${diagnostic.closeTime}"
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onDiagnosticItemClickListener!!.onItemClicked(diagnostic.id.toString(), diagnostic.name)
            })
        }
    }

    override fun getItemCount(): Int {
        return diagnostics.size
    }

    fun showLoadingFooter() {
        diagnostics.add(null)
        notifyItemInserted(diagnostics.size - 1)
    }

    fun removeLoadingFooter() {
        val index = diagnostics.indexOf(null)
        if (index != -1) {
            diagnostics.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
