package com.iprism.medrayder.network;

import com.iprism.medrayder.utils.Constants;
import com.iprism.medrayder.videocalling.model.disconnectnotification.DisconnectNotificationApiResponse;
import com.iprism.medrayder.videocalling.model.disconnectnotification.DisconnectNotificationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MedRayderServiceJava {

    @POST(Constants.DISCONNECT_NOTIFICATION_ENDPOINT)
    Call<DisconnectNotificationApiResponse> disconnectCall(@Body DisconnectNotificationRequest request);
}
