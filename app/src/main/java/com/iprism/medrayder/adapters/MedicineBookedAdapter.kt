package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.MedicineBookedItemBinding
import com.iprism.medrayder.models.hospitalmedbookingdetails.ProductsItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.MedicineBookedItemViewHolder

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
