package com.iprism.khamhaina.activities.multiconferencequickapp.model.test

import com.google.gson.annotations.SerializedName

data class LiveRecording(

	@field:SerializedName("auto_recording")
	val autoRecording: Boolean,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("quality")
	val quality: String
)