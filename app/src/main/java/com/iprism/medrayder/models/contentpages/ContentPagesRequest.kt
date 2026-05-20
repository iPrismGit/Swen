package com.iprism.medrayder.models.contentpages

import com.google.gson.annotations.SerializedName

data class ContentPagesRequest(

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("language")
	val language: String
)
