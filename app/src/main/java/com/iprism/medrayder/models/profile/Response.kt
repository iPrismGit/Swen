package com.iprism.medrayder.models.profile

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("profile_id")
	val profileId: List<ProfileIdItem>,

	@field:SerializedName("profile")
	val profile: List<ProfileItem>,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("subscription_id")
	val subscriptionId: String
)