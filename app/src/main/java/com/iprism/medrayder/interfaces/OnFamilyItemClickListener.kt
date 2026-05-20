package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.medrayder.models.onlinedoctors.DoctorsItem

interface OnFamilyItemClickListener {

    fun onItemClicked(familyMembersItem: FamilyMembersItem)
}