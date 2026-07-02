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
import com.iprism.swen.models.homepage.SurgicalQuote
import com.iprism.swen.utils.Constants

class SurgeonSymptomsDoctorsAdapter(var context: Context, var surgicalQuotes: List<SubCategoriesItem>) :
    RecyclerView.Adapter<SurgeonSymptomsDoctorsAdapter.HomeSurgicalQuoteViewHolder>() {

    private lateinit var listener: OnSurgicalQuoteCatClickListener

    fun setupListener(listener: OnSurgicalQuoteCatClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SurgeonSymptomsDoctorsAdapter.HomeSurgicalQuoteViewHolder {
        val binding =
            DoctorTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeSurgicalQuoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SurgeonSymptomsDoctorsAdapter.HomeSurgicalQuoteViewHolder,
        position: Int
    ) {
        val surgicalQuote = surgicalQuotes[position]
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
        return surgicalQuotes.size
    }

    class HomeSurgicalQuoteViewHolder(var binding: DoctorTypeItemBinding) : RecyclerView.ViewHolder(binding.root)
}