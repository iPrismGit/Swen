package com.iprism.swen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.DoctorTypeItemBinding
import com.iprism.swen.interfaces.OnSurgicalQuoteCatClickListener
import com.iprism.swen.models.homepage.SubCategoriesItem
import com.iprism.swen.utils.Constants

class HomeVisitServicesAdapter(var context: Context, var items: List<SubCategoriesItem>) :
    RecyclerView.Adapter<HomeVisitServicesAdapter.HomeSurgicalQuoteViewHolder>() {

    private lateinit var listener: OnSurgicalQuoteCatClickListener

    fun setupListener(listener: OnSurgicalQuoteCatClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeVisitServicesAdapter.HomeSurgicalQuoteViewHolder {
        val binding =
            DoctorTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeSurgicalQuoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeVisitServicesAdapter.HomeSurgicalQuoteViewHolder,
        position: Int
    ) {
        val surgicalQuote = items[position]
        holder.binding.categoryName.text = surgicalQuote.name
        if (surgicalQuote.image.isNotEmpty()) {
            Glide.with(context)
                .load(Constants.IMAGES_BASE_URL + surgicalQuote.image)
                .placeholder(R.drawable.general_problem_img)
                .error(R.drawable.error_img)
                .into(holder.binding.categoryImg);
        }
        holder.binding.root.setOnClickListener { view ->
            listener.onItemClicked(surgicalQuote.id, surgicalQuote.name)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class HomeSurgicalQuoteViewHolder(var binding: DoctorTypeItemBinding) : RecyclerView.ViewHolder(binding.root)
}