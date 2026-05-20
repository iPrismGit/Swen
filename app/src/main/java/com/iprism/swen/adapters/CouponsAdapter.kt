package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.R
import com.iprism.swen.databinding.CouponItemBinding
import com.iprism.swen.interfaces.OnCouponItemClickListener
import com.iprism.swen.models.onlinedoctorscoupons.CouponsItem
import com.iprism.swen.viewholders.CouponViewHolder

class CouponsAdapter(private val coupons : List<CouponsItem>) : RecyclerView.Adapter<CouponViewHolder>() {

    private var onCouponItemClickListener: OnCouponItemClickListener? = null

    fun setOnDoctorItemClickListener(onCouponItemClickListener: OnCouponItemClickListener?) {
        this.onCouponItemClickListener = onCouponItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val binding = CouponItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val item = coupons[position]
        val context = holder.binding.root.context
        holder.binding.nameTxt.text = item.name
        holder.binding.descTxt.text = item.description
        holder.binding.offTxt.text = context.getString(R.string.upto) + item.percentage + "% " + context.getString(R.string.off)
        holder.binding.applyTxt.setOnClickListener(View.OnClickListener {
            onCouponItemClickListener!!.onItemClicked(item)
        })
    }

    override fun getItemCount(): Int {
        return coupons.size
    }
}
