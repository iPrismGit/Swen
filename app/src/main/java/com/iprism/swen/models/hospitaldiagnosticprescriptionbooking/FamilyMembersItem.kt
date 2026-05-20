package com.iprism.swen.models.hospitaldiagnosticprescriptionbooking

import com.google.gson.annotations.SerializedName

data class FamilyMembersItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String
)