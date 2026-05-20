package com.iprism.medrayder.models.familymembers

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.onlinedoctorbookingdetails.FamilyMembersItem

data class Response(

	@field:SerializedName("family_members")
	val familyMembers: ArrayList<FamilyMembersItem>
)