package com.iprism.swen.interfaces

import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem

interface OnMultipleFamilyItemClickListener {

    fun onItemClicked(familyMembers: ArrayList<FamilyMembersItem>)
}