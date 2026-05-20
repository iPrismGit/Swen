package com.iprism.medrayder.models.userdropdowns

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("gender")
	val gender: ArrayList<CategoriesItem>,

	@field:SerializedName("categories")
	val categories: ArrayList<CategoriesItem>,

	@field:SerializedName("blood_groups")
	val bloodGroups: ArrayList<CategoriesItem>
)