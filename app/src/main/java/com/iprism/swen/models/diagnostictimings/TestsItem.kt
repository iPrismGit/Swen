package com.iprism.swen.models.diagnostictimings

import com.google.gson.annotations.SerializedName

data class TestsItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("sub_tests")
	val subTests: List<SubTestsItem>,

	@field:SerializedName("id")
	val id: Int
)