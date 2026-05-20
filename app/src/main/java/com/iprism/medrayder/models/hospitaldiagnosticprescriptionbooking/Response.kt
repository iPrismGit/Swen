package com.iprism.medrayder.models.hospitaldiagnosticprescriptionbooking

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("family_members")
	val familyMembers: List<FamilyMembersItem>,

	@field:SerializedName("receipt_details")
	val receiptDetails: ReceiptDetails
)