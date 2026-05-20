package com.iprism.kaamhainapartnerapp.activities.multiconferencequickapp.model.test

import com.google.gson.annotations.SerializedName
import com.iprism.khamhaina.activities.multiconferencequickapp.model.test.Data
import com.iprism.khamhaina.activities.multiconferencequickapp.model.test.Settings
import com.iprism.khamhaina.activities.multiconferencequickapp.model.test.Sip

data class Room(

	@field:SerializedName("room_id")
	val roomId: String,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("settings")
	val settings: Settings,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("owner_ref")
	val ownerRef: String,

	@field:SerializedName("created")
	val created: String,

	@field:SerializedName("service_id")
	val serviceId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("sip")
	val sip: Sip
)