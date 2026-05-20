package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.SubscriptionItemBinding
import com.iprism.medrayder.interfaces.OnSubscriptionItemClickListener
import com.iprism.medrayder.models.subscription.HealthCardsItem
import com.iprism.medrayder.viewholders.SubscriptionViewHolder

class PlansAdapter(private val plans : List<HealthCardsItem>) : RecyclerView.Adapter<SubscriptionViewHolder>() {

    private var onSubscriptionItemClickListener: OnSubscriptionItemClickListener? = null

    fun setOnSubscriptionItemClickListener(onSubscriptionItemClickListener: OnSubscriptionItemClickListener?) {
        this.onSubscriptionItemClickListener = onSubscriptionItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val binding = SubscriptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriptionViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        val item = plans[position]
        val context = holder.itemView.context
        holder.binding.priceTxt.text = "₹" + item.discountPrice.toString()
        holder.binding.previousPriceTxt.text = "₹" + item.price.toString()
        holder.binding.timeTxt.text = "${context.getString(R.string.for1)} " + item.duration
        holder.binding.planNameTxt.text = item.name
        holder.binding.previousPriceTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onSubscriptionItemClickListener!!.onItemClicked(item)
        })
    }

    override fun getItemCount(): Int {
        return plans.size
    }
}
