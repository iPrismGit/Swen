package com.iprism.medrayder.models.subscription

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("health_cards")
	val healthCards: List<HealthCardsItem>,

	@field:SerializedName("family_members")
	val familyMembers: List<FamilyMember>,

	@field:SerializedName("bill_summary")
	val billSummary: BillSummary
)