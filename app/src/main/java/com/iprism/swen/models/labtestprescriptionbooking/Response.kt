package com.iprism.swen.models.labtestprescriptionbooking

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.labtestslots.Address
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.pharmacyproductcart.ReceiptDetails

data class Response(

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("family_members")
	val familyMembers: List<FamilyMembersItem>,

	@field:SerializedName("receipt_details")
	val receiptDetails: ReceiptDetails
)