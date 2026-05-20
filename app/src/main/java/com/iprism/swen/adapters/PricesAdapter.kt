package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.R
import com.iprism.swen.databinding.PriceItemBinding
import com.iprism.swen.interfaces.OnWalletPriceActionListener
import com.iprism.swen.viewholders.PriceViewHolder

class PricesAdapter(prices : List<String>) : RecyclerView.Adapter<PriceViewHolder>() {

    private var prices : List<String>? = prices
    private var onWalletPriceActionListener: OnWalletPriceActionListener? = null
    private var selectedPosition = RecyclerView.NO_POSITION


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val binding = PriceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PriceViewHolder(binding)
    }

    fun setOnWalletActionListener(onWalletPriceActionListener: OnWalletPriceActionListener?) {
        this.onWalletPriceActionListener = onWalletPriceActionListener
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PriceViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.priceTxt.text = prices!![position]
        if (position == selectedPosition) {
            holder.binding.priceTxt.setBackgroundColor(holder.itemView.context.getColor(R.color.blue))
            holder.binding.priceTxt.setTextColor(holder.itemView.context.getColor(R.color.white))
        } else {
            holder.binding.priceTxt.setBackgroundColor(holder.itemView.context.getColor(R.color.white))
            holder.binding.priceTxt.setTextColor(holder.itemView.context.getColor(R.color.black))
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onWalletPriceActionListener!!.onItemClicked(prices!![position].replace(",", "").replace("₹", ""))
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(position)
        })
    }

    override fun getItemCount(): Int {
        return prices!!.size
    }
}