package com.iprism.medrayder.models.labtestprescriptionbooking

import com.google.gson.annotations.SerializedName

data class LabTestPrescriptionBookingRequest(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("address_id")
	val addressId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("lab_test_id")
	val labTestId: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: Int,
) {
	override fun toString(): String {
		return "LabTestPrescriptionBookingRequest(image=$image, userId=$userId, addressId=$addressId, name='$name', mobile='$mobile', viewType='$viewType', language='$language', labTestId=$labTestId, familyMemberId=$familyMemberId, authToken=$authToken)"
	}
}