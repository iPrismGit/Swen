package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.databinding.CartProductItemBinding
import com.iprism.medrayder.interfaces.OnCartProductItemClickListener
import com.iprism.medrayder.models.pharmacyproductcart.ProductsItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.CartProductViewHolder

class PharmacyCartProductsAdapter(var products: List<ProductsItem>) : RecyclerView.Adapter<CartProductViewHolder>() {


    private var onCartProductItemClickListener: OnCartProductItemClickListener? = null

    fun setOnCartProductItemClickListener(onCartProductItemClickListener: OnCartProductItemClickListener?) {
        this.onCartProductItemClickListener = onCartProductItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        val binding = CartProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartProductViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val product = products[position]
        holder.binding.productNameTxt.text = product.name
        holder.binding.priceTxt.text = "₹${product.price}"
        holder.binding.previousPriceTxt.text = "₹${product.originalPrice}"
        if (product.image.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Constants.IMAGES_BASE_URL + product.image)
                .into(holder.binding.productImg)
        }
        holder.binding.quantityTxt.text = product.cartQuantity.toString()
        holder.binding.previousPriceTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.incrementImg.setOnClickListener(View.OnClickListener {
            onCartProductItemClickListener!!.onIncrementClicked(product)
        })
        holder.binding.decrementImg.setOnClickListener(View.OnClickListener {
            onCartProductItemClickListener!!.onDecrementClicked(product)
        })
    }

    override fun getItemCount(): Int {
        return products.size
    }
}
