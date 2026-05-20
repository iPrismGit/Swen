package com.iprism.medrayderonlinedoctor.videocalling.createroom

import com.google.gson.annotations.SerializedName

data class Room(

    @field:SerializedName("room_id")
	val roomId: String,

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