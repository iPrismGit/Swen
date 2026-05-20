package com.iprism.medrayder.models.subscription

import com.google.gson.annotations.SerializedName

data class FamilyMember(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String
)