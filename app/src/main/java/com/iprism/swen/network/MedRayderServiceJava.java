package com.iprism.swen.network;

import com.iprism.swen.utils.Constants;
import com.iprism.swen.videocalling.model.disconnectnotification.DisconnectNotificationApiResponse;
import com.iprism.swen.videocalling.model.disconnectnotification.DisconnectNotificationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MedRayderServiceJava {

    @POST(Constants.DISCONNECT_NOTIFICATION_ENDPOINT)
    Call<DisconnectNotificationApiResponse> disconnectCall(@Body DisconnectNotificationRequest request);
}
