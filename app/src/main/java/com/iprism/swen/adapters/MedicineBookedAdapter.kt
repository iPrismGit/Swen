package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.MedicineBookedItemBinding
import com.iprism.swen.models.hospitalmedbookingdetails.ProductsItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.MedicineBookedItemViewHolder

class MedicineBookedAdapter(private val products : List<ProductsItem>) : RecyclerView.Adapter<MedicineBookedItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineBookedItemViewHolder {
        val binding = MedicineBookedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicineBookedItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MedicineBookedItemViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = products[position]
        holder.binding.productNameTxt.text = item.productName
        holder.binding.hospitalNameTxt.text = item.hospitalName
        holder.binding.priceTxt.text = "₹${item.price}"
        holder.binding.qtyTxt.text = "${context.getString(R.string.qty)} : ${item.cartQuantity}"
        if (item.image.isNotEmpty()) {
            Glide.with(context)
                .load(Constants.IMAGES_BASE_URL + item.image)
                .into(holder.binding.productImg)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}
