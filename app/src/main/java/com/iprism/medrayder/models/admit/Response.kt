package com.iprism.medrayder.models.admit

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.medrayder.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.medrayder.models.onlinedoctorbookingdetails.Slots

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