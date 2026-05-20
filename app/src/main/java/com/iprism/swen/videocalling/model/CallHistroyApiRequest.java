package com.iprism.swen.videocalling.model;

import com.google.gson.annotations.SerializedName;

public class CallHistroyApiRequest{

	@SerializedName("room_id")
	private String roomId;

	@SerializedName("duration")
	private String duration;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("connect_time")
	private String connectTime;

	@SerializedName("call_type")
	private String callType;

	@SerializedName("disconnect_time")
	private String disconnectTime;

	@SerializedName("emp_id")
	private String empId;

	public CallHistroyApiRequest(String roomId, String duration, String userId, String connectTime, String callType, String disconnectTime, String empId) {
		this.roomId = roomId;
		this.duration = duration;
		this.userId = userId;
		this.connectTime = connectTime;
		this.callType = callType;
		this.disconnectTime = disconnectTime;
		this.empId = empId;
	}

	public CallHistroyApiRequest(String roomId, String duration, String userId, String callType, String disconnectTime, String empId) {
		this.roomId = roomId;
		this.duration = duration;
		this.userId = userId;
		this.callType = callType;
		this.disconnectTime = disconnectTime;
		this.empId = empId;
	}

	public void setRoomId(String roomId){
		this.roomId = roomId;
	}

	public String getRoomId(){
		return roomId;
	}

	public void setDuration(String duration){
		this.duration = duration;
	}

	public String getDuration(){
		return duration;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setConnectTime(String connectTime){
		this.connectTime = connectTime;
	}

	public String getConnectTime(){
		return connectTime;
	}

	public void setCallType(String callType){
		this.callType = callType;
	}

	public String getCallType(){
		return callType;
	}

	public void setDisconnectTime(String disconnectTime){
		this.disconnectTime = disconnectTime;
	}

	public String getDisconnectTime(){
		return disconnectTime;
	}

	public void setEmpId(String empId){
		this.empId = empId;
	}

	public String getEmpId(){
		return empId;
	}

	@Override
	public String toString() {
		return "CallHistroyApiRequest{" +
				"roomId='" + roomId + '\'' +
				", duration='" + duration + '\'' +
				", userId='" + userId + '\'' +
				", connectTime='" + connectTime + '\'' +
				", callType='" + callType + '\'' +
				", disconnectTime='" + disconnectTime + '\'' +
				", empId='" + empId + '\'' +
				'}';
	}
}