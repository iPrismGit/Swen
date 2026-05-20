package com.iprism.medrayder.models.profile

import com.google.gson.annotations.SerializedName

data class ProfileIdItem(

	@field:SerializedName("profile_id")
	val profileId: Int,

	@field:SerializedName("type")
	val type: String
)