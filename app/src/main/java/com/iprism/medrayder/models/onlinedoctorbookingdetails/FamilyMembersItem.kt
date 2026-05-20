package com.iprism.medrayder.models.onlinedoctorbookingdetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FamilyMembersItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String
) : Serializable