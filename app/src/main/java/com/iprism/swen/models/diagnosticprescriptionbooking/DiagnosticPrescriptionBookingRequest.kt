package com.iprism.swen.models.diagnosticprescriptionbooking

import com.google.gson.annotations.SerializedName

data class DiagnosticPrescriptionBookingRequest(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("user_id")
	val userId: Int,

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

	@field:SerializedName("diagnostic_id")
	val diagnosticId: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: Int
) {
	override fun toString(): String {
		return "DiagnosticPrescriptionBookingRequest(userId=$userId, name='$name', mobile='$mobile', viewType='$viewType', language='$language', diagnosticId=$diagnosticId, familyMemberId=$familyMemberId, authToken=$authToken, image='$image')"
	}
}