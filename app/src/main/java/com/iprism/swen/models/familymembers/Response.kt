package com.iprism.swen.models.familymembers

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem

data class Response(

	@field:SerializedName("family_members")
	val familyMembers: ArrayList<FamilyMembersItem>
)