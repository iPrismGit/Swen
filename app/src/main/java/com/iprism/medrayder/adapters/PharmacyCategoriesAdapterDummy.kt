package com.iprism.medrayder.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.activities.PharmacyProductsActivity
import com.iprism.medrayder.databinding.PharmacyCategoryItemBinding
import com.iprism.medrayder.viewholders.PharmacyCatViewHolder

class PharmacyCategoriesAdapterDummy(var images: Int) : RecyclerView.Adapter<PharmacyCatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyCatViewHolder {
        val binding = PharmacyCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PharmacyCatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PharmacyCatViewHolder, position: Int) {
        /*holder.binding.root.setOnClickListener(View.OnClickListener {
            holder.binding.root.context.startActivity(Intent(holder.binding.root.context, PharmacyProductsActivity::class.java))
        })*/
    }

    override fun getItemCount(): Int = 12
}
