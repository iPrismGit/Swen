package com.iprism.swen.repository

import com.iprism.swen.models.address.AddAddressApiResponse
import com.iprism.swen.models.address.AddAddressRequest
import com.iprism.swen.models.addresslist.AddressListApiResponse
import com.iprism.swen.models.addresslist.AddressListRequest
import com.iprism.swen.network.MedRayderApi

class AddressRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun addAddress(request : AddAddressRequest): AddAddressApiResponse {
        return apiService.addAddress(request)
    }

    suspend fun fetchAddressList(request : AddressListRequest): AddressListApiResponse {
        return apiService.fetchAddressList(request)
    }
}
