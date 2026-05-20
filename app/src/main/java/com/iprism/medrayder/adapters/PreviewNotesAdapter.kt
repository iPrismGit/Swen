package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.medrayder.databinding.PreviewMedcineTestItemBinding

class PreviewNotesAdapter(private var notes: List<String>):Adapter<PreviewNotesAdapter.PreviewMedicineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewNotesAdapter.PreviewMedicineViewHolder {
        val binding = PreviewMedcineTestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreviewMedicineViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PreviewNotesAdapter.PreviewMedicineViewHolder, position: Int) {
        val note = notes[position]
        val count = position + 1
        holder.binding.nameTxt.text = "$count. ${note.replaceFirstChar { it.uppercase() }} "
        holder.binding.timeTxt.visibility = View.GONE
    }

    override fun getItemCount(): Int {
       return notes.size
    }

    class PreviewMedicineViewHolder(var binding: PreviewMedcineTestItemBinding) : ViewHolder(binding.root)
}