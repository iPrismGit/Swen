package com.iprism.swen.models.insertsurgicalquote

import com.google.gson.annotations.SerializedName

data class InsertSurgicalQuoteRequest(

	@field:SerializedName("surgery_name")
	val surgeryName: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("health_issue")
	val healthIssue: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("age")
	val age: String,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("payment_type_category")
	val paymentTypeCategory: String,

	@field:SerializedName("insurence_company_name")
	val insurenceCompanyName: String,

	@field:SerializedName("tpa_name")
	val tpaName: String,

	@field:SerializedName("no_of_persons_covered")
	val noOfPersonsCovered: String,
) {
	override fun toString(): String {
		return "InsertSurgicalQuoteRequest(surgeryName='$surgeryName', healthIssue='$healthIssue', userId=$userId, catId=$catId, name='$name', authToken='$authToken', mainDataId=$mainDataId, age='$age', paymentType='$paymentType', paymentTypeCategory='$paymentTypeCategory', insurenceCompanyName='$insurenceCompanyName', tpaName='$tpaName', noOfPersonsCovered='$noOfPersonsCovered')"
	}
}