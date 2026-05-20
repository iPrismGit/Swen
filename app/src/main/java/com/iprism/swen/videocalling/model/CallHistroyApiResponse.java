package com.iprism.swen.videocalling.model;

import com.google.gson.annotations.SerializedName;
import com.iprism.kaamhainapartnerapp.activities.multiconferencequickapp.model.Response;

public class CallHistroyApiResponse{

	@SerializedName("response")
	private Response response;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setResponse(Response response){
		this.response = response;
	}

	public Response getResponse(){
		return response;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}