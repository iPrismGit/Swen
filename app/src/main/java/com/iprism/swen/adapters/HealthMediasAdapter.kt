package com.iprism.swen.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.models.healthmedia.MainDataItem
import com.iprism.swen.databinding.HealthMediaItemBinding
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.ItemLoadingViewHolder

class HealthMediasAdapter(private var items : ArrayList<MainDataItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding = HealthMediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return if (viewType == VIEW_TYPE_ITEM) {
           HealthMediasAdapter.HealthMediaViewHolder(binding)
       } else {
           ItemLoadingViewHolder(itemLoadingBinding)
       }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is HealthMediaViewHolder) {
            val context = holder.binding.root.context
            val item = items[position]
            if (item!!.image.isNotEmpty()) {
                Glide.with(context)
                    .load(Constants.IMAGES_BASE_URL + item.image)
                    .into(holder.binding.mediaImg)
            }
            Log.d("healthMediaImage", Constants.IMAGES_BASE_URL + item.image)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun showLoadingFooter() {
        items.add(null)
        notifyItemInserted(items.size - 1)
    }

    fun removeLoadingFooter() {
        val index = items.indexOf(null)
        if (index != -1) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class HealthMediaViewHolder(var binding: HealthMediaItemBinding) : RecyclerView.ViewHolder(binding.root)
}