package com.iprism.khamhaina.activities.multiconferencequickapp.model.test

import com.google.gson.annotations.SerializedName

data class RoleBasedRecording(

	@field:SerializedName("moderator")
	val moderator: String,

	@field:SerializedName("participant")
	val participant: String
)