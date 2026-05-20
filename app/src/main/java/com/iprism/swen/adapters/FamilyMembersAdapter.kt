package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.R
import com.iprism.swen.databinding.FamilyMemberItemBinding
import com.iprism.swen.interfaces.OnFamilyItemClickListener
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.viewholders.FamilyMembersViewHolder

class FamilyMembersAdapter(private var familyMembers : ArrayList<FamilyMembersItem>) : RecyclerView.Adapter<FamilyMembersViewHolder>() {

    private var selectedItem = -1

    private var onFamilyItemClickListener: OnFamilyItemClickListener? = null

    fun setOnDoctorItemClickListener(familyItemClickListener: OnFamilyItemClickListener?) {
        this.onFamilyItemClickListener = familyItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyMembersViewHolder {
        val binding = FamilyMemberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FamilyMembersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FamilyMembersViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val familyMember = familyMembers[position]
        holder.binding.nameTxt.text = familyMember.name
        if (selectedItem == position) {
            holder.binding.cardView.strokeColor = ContextCompat.getColor(holder.binding.root.context, R.color.green)
            holder.binding.cardView.strokeWidth = 2
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#DAFFD0"))
        } else {
            holder.binding.cardView.strokeColor = ContextCompat.getColor(holder.binding.root.context, R.color.light_gray1)
            holder.binding.cardView.strokeWidth = 2
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onFamilyItemClickListener!!.onItemClicked(familyMember)
            val previousItem = selectedItem
            selectedItem = position
            notifyItemChanged(previousItem)
            notifyItemChanged(selectedItem)
        })
    }

    override fun getItemCount(): Int {
        return familyMembers.size
    }
}
