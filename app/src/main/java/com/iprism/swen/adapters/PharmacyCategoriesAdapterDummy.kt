package com.iprism.swen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.databinding.PharmacyCategoryItemBinding
import com.iprism.swen.viewholders.PharmacyCatViewHolder

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
