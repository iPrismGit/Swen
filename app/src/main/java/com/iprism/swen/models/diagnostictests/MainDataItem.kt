package com.iprism.swen.models.diagnostictests

import com.google.gson.annotations.SerializedName

data class MainDataItem(

	@field:SerializedName("report_in")
	val reportIn: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("one_person_discount")
	val onePersonDiscount: Int,

	@field:SerializedName("tests")
	val tests: List<TestsItem>,

	@field:SerializedName("one_person")
	val onePerson: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("fasting")
	val fasting: String,

	@field:SerializedName("id")
	val id: Int
)