package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.R
import com.iprism.swen.databinding.WalletHistoryItemBinding
import com.iprism.swen.interfaces.OnCouponItemClickListener
import com.iprism.swen.models.wallethistory.ResponseItem
import com.iprism.swen.viewholders.WalletHistoryViewHolder

class WalletHistoryAdapter(private val items : List<ResponseItem>) : RecyclerView.Adapter<WalletHistoryViewHolder>() {

    private var onCouponItemClickListener: OnCouponItemClickListener? = null

    fun setOnDoctorItemClickListener(onCouponItemClickListener: OnCouponItemClickListener?) {
        this.onCouponItemClickListener = onCouponItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletHistoryViewHolder {
        val binding = WalletHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletHistoryViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WalletHistoryViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context
        holder.binding.timeTxt.text = item.createdOn
        if (item.type.equals("debited", true)) {
            holder.binding.transactionTypeTxt.text = "${context.getString(R.string.wallet)} ${context.getString(R.string.debited)}"
            holder.binding.descriptionTxt.text = context.getString(R.string.order_placed)
            holder.binding.amountTxt.text = "-₹${item.amount}"
            holder.binding.amountTxt.setTextColor(ContextCompat.getColor(holder.binding.amountTxt.context, R.color.red))
        } else if (item.type.equals("recharged", true)) {
            holder.binding.transactionTypeTxt.text = "${context.getString(R.string.wallet)} ${context.getString(R.string.recharged)}"
            holder.binding.descriptionTxt.text = context.getString(R.string.recharge_successful)
            holder.binding.amountTxt.text = "₹${item.amount}"
            holder.binding.amountTxt.setTextColor(ContextCompat.getColor(holder.binding.amountTxt.context, R.color.green))
        } else if (item.type.equals("refund", true)) {
            holder.binding.transactionTypeTxt.text = "${context.getString(R.string.wallet)} ${context.getString(R.string.refunded)}"
            holder.binding.descriptionTxt.text = context.getString(R.string.refund_successful)
            holder.binding.amountTxt.text = "₹${item.amount}"
            holder.binding.amountTxt.setTextColor(ContextCompat.getColor(holder.binding.amountTxt.context, R.color.blue))
        } else if (item.type.equals("credited", true)) {
            holder.binding.transactionTypeTxt.text = "${context.getString(R.string.wallet)} ${context.getString(R.string.credited)}"
            holder.binding.descriptionTxt.text = context.getString(R.string.credited_successful)
            holder.binding.amountTxt.text = "₹${item.amount}"
            holder.binding.amountTxt.setTextColor(ContextCompat.getColor(holder.binding.amountTxt.context, R.color.green))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
