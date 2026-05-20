package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.databinding.AddressItemBinding
import com.iprism.medrayder.interfaces.OnAddressItemClickListener
import com.iprism.medrayder.models.addresslist.AddressItem
import com.iprism.medrayder.viewholders.AddressViewHolder

class AddressListAdapter(private val addressList : List<AddressItem>, private var type : String) : RecyclerView.Adapter<AddressViewHolder>() {

    private var onAddressItemClickListener: OnAddressItemClickListener? = null

    fun setOnAddressItemClickListener(onAddressItemClickListener: OnAddressItemClickListener?) {
        this.onAddressItemClickListener = onAddressItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val item = addressList[position]
        holder.binding.addressTypeTxt.text = item.addressType
        holder.binding.addressTxt.text = listOf(
            item.hno,
            item.buildingNo,
            item.landmark,
            item.address
        ).filter { !it.isNullOrBlank() }
            .joinToString(", ")
        if (type.equals("home", true)) {
            holder.binding.button.visibility = View.GONE
            holder.binding.makeDefaultAddressTxt.visibility = View.GONE
            holder.binding.rightArrowImg.visibility = View.VISIBLE
        } else {
            if (item.defaultAddress == 1) {
                holder.binding.button.visibility = View.VISIBLE
                holder.binding.makeDefaultAddressTxt.visibility = View.GONE
            } else {
                holder.binding.button.visibility = View.GONE
                holder.binding.makeDefaultAddressTxt.visibility = View.GONE
            }
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onAddressItemClickListener!!.onItemClicked(item)
        })
    }

    override fun getItemCount(): Int {
        return addressList.size
    }
}
