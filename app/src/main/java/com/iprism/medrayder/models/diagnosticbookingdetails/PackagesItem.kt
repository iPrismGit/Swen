package com.iprism.medrayder.models.diagnosticbookingdetails

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.diagnostictests.TestsItem

data class PackagesItem(

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

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int
)