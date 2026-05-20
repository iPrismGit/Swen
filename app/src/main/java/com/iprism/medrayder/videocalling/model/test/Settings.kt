package com.iprism.khamhaina.activities.multiconferencequickapp.model.test

import com.google.gson.annotations.SerializedName

data class Settings(

	@field:SerializedName("canvas")
	val canvas: Boolean,

	@field:SerializedName("abwd")
	val abwd: Boolean,

	@field:SerializedName("knock")
	val knock: Boolean,

	@field:SerializedName("watermark")
	val watermark: Boolean,

	@field:SerializedName("media_zone")
	val mediaZone: String,

	@field:SerializedName("scheduled")
	val scheduled: Boolean,

	@field:SerializedName("single_file_recording")
	val singleFileRecording: Boolean,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("auto_recording")
	val autoRecording: Boolean,

	@field:SerializedName("quality")
	val quality: String,

	@field:SerializedName("mode")
	val mode: String,

	@field:SerializedName("duration")
	val duration: String,

	@field:SerializedName("role_based_recording")
	val roleBasedRecording: RoleBasedRecording,

	@field:SerializedName("viewers")
	val viewers: Int,

	@field:SerializedName("max_active_talkers")
	val maxActiveTalkers: String,

	@field:SerializedName("screen_share")
	val screenShare: Boolean,

	@field:SerializedName("active_talker")
	val activeTalker: Boolean,

	@field:SerializedName("wait_for_moderator")
	val waitForModerator: Boolean,

	@field:SerializedName("encryption")
	val encryption: Boolean,

	@field:SerializedName("media_configuration")
	val mediaConfiguration: String,

	@field:SerializedName("live_recording")
	val liveRecording: LiveRecording,

	@field:SerializedName("adhoc")
	val adhoc: Boolean,

	@field:SerializedName("participants")
	val participants: String,

	@field:SerializedName("moderators")
	val moderators: String
)