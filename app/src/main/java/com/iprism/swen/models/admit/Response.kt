package com.iprism.swen.models.admit

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.Slots

data class Response(

	@field:SerializedName("main_data")
	val mainData: MainData,

	@field:SerializedName("dates")
	val dates: List<DatesItem>,

	@field:SerializedName("slots")
	val slots: Slots,

	@field:SerializedName("family_members")
	val familyMembers: ArrayList<FamilyMembersItem>
)