package com.iprism.swen.network;


import com.iprism.swen.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MedRayderApiJava {

    public MedRayderServiceJava createMedRayderService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MedRayderServiceJava.class);
    }
}
