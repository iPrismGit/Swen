package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.databinding.ItemLoadingBinding
import com.iprism.medrayder.databinding.MedicineProductItemBinding
import com.iprism.medrayder.interfaces.OnProductItemClickListener
import com.iprism.medrayder.models.pharmacyproducts.MainDataItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.ItemLoadingViewHolder
import com.iprism.medrayder.viewholders.PharmacyProductViewHolder

class PharmacyProductsAdapter(var products: ArrayList<MainDataItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onProductItemClickListener: OnProductItemClickListener? = null

    fun setOnProductItemClickListener(onProductItemClickListener: OnProductItemClickListener?) {
        this.onProductItemClickListener = onProductItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (products[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MedicineProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            PharmacyProductViewHolder(binding)
        } else {
           ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PharmacyProductViewHolder) {
            val product = products[position]
            holder.binding.nameTxt.text = product!!.name
            holder.binding.priceTxt.text = "₹${product.discountPrice}"
            holder.binding.previousPriceTxt.text = "₹${product.price}"
            if (product.image.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(Constants.IMAGES_BASE_URL + product.image)
                    .into(holder.binding.productImg)
            }
            if (product.cartQuantity == 0) {
                holder.binding.addBtn.visibility = View.VISIBLE
                holder.binding.productQuantityLl.visibility = View.GONE
            } else {
                holder.binding.addBtn.visibility = View.GONE
                holder.binding.productQuantityLl.visibility = View.VISIBLE
                holder.binding.quantityTxt.text = product.cartQuantity.toString()
            }
            holder.binding.previousPriceTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.addBtn.setOnClickListener(View.OnClickListener {
                onProductItemClickListener!!.onAddClicked(product, position)
            })
            holder.binding.increaseQuantityImg.setOnClickListener(View.OnClickListener {
                onProductItemClickListener!!.onIncrementClicked(product, position)
            })
            holder.binding.decreaseQuantityImg.setOnClickListener(View.OnClickListener {
                onProductItemClickListener!!.onDecrementClicked(product, position)
            })
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun showLoadingFooter() {
        products.add(null)
        notifyItemInserted(products.size - 1)
    }

    fun removeLoadingFooter() {
        val index = products.indexOf(null)
        if (index != -1) {
            products.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addProductCount(position: Int) {
        products[position]?.cartQuantity = products[position]?.cartQuantity?.plus(1)!!
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun decreaseProductCount(position: Int) {
        products[position]?.let {
            val currentQty = it.cartQuantity ?: 0
            if (currentQty > 0) {
                it.cartQuantity = currentQty - 1
                notifyItemChanged(position)
            }
        }
    }
}