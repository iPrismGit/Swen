package com.iprism.medrayder.videocalling.model.disconnectnotification;

import com.google.gson.annotations.SerializedName;

public class DisconnectNotificationRequest{

	@SerializedName("doctor_id")
	private String doctorId;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("type")
	private String type;

	@SerializedName("player_id")
	private String playerId;

	@SerializedName("consult_type")
	private String consultType;

	@SerializedName("family_member_id")
	private String family_memberId;

	@SerializedName("booking_id")
	private String booking_id;

	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}

	public String getDoctorId(){
		return doctorId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	@Override
	public String toString() {
		return "DisconnectNotificationRequest{" +
				"doctorId='" + doctorId + '\'' +
				", userId='" + userId + '\'' +
				", type='" + type + '\'' +
				", playerId='" + playerId + '\'' +
				", consultType='" + consultType + '\'' +
				", family_memberId='" + family_memberId + '\'' +
				", booking_id='" + booking_id + '\'' +
				'}';
	}

	public DisconnectNotificationRequest(String doctorId, String userId) {
		this.doctorId = doctorId;
		this.userId = userId;
	}

	public DisconnectNotificationRequest(String doctorId, String userId, String type, String playerId, String consultType, String family_memberId, String booking_id) {
		this.doctorId = doctorId;
		this.userId = userId;
		this.type = type;
		this.playerId = playerId;
		this.consultType = consultType;
		this.family_memberId = family_memberId;
		this.booking_id = booking_id;
	}
}