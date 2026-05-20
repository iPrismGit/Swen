package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.databinding.DocumentItemBinding
import com.iprism.medrayder.models.medlocker.ImagesItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.DocumentViewHolder

class DocumentsListAdapter(private val docs : List<String>) : RecyclerView.Adapter<DocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val binding = DocumentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val doc = docs[position]
        Glide.with(holder.itemView.context)
            .load(Constants.IMAGES_BASE_URL + doc)
            .into(holder.binding.docImg)
    }

    override fun getItemCount(): Int {
        return docs.size
    }
}
