package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.FamilyMemberItemBinding
import com.iprism.medrayder.interfaces.OnMultipleFamilyItemClickListener
import com.iprism.medrayder.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.medrayder.viewholders.FamilyMembersViewHolder

class MultipleFamilyMembersAdapter(private var familyMembers : ArrayList<FamilyMembersItem>, val maxSelection : Int) : RecyclerView.Adapter<FamilyMembersViewHolder>() {


    private var onMultipleFamilyItemClickListener: OnMultipleFamilyItemClickListener? = null
    private val selectedMembers = ArrayList<FamilyMembersItem>()

    fun setOnMultipleFamilyItemClickListener(onMultipleFamilyItemClickListener: OnMultipleFamilyItemClickListener?) {
        this.onMultipleFamilyItemClickListener = onMultipleFamilyItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyMembersViewHolder {
        val binding = FamilyMemberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FamilyMembersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FamilyMembersViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val familyMember = familyMembers[position]
        val context = holder.itemView.context
        holder.binding.nameTxt.text = familyMember.name
        val isSelected = selectedMembers.contains(familyMember)
        if (isSelected) {
            holder.binding.cardView.strokeColor = ContextCompat.getColor(context, R.color.green)
            holder.binding.cardView.strokeWidth = 2
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#DAFFD0"))
        } else {
            holder.binding.cardView.strokeColor = ContextCompat.getColor(context, R.color.light_gray1)
            holder.binding.cardView.strokeWidth = 2
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        holder.binding.root.setOnClickListener {
            if (isSelected) {
                selectedMembers.remove(familyMember)
                onMultipleFamilyItemClickListener?.onItemClicked(selectedMembers)
                notifyItemChanged(position)
            } else {
                if (selectedMembers.size < maxSelection) {
                    selectedMembers.add(familyMember)
                    onMultipleFamilyItemClickListener?.onItemClicked(selectedMembers)
                    notifyItemChanged(position)
                } else {
                    Toast.makeText(holder.binding.cardView.context, "${context.getString(R.string.you_can_select_max)} $maxSelection ${context.getString(R.string.member)}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return familyMembers.size
    }
}
