package com.iprism.swen.models.addresslist

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("address")
	val address: List<AddressItem>
)