package com.iprism.medrayder.repository

import com.iprism.medrayder.models.address.AddAddressApiResponse
import com.iprism.medrayder.models.address.AddAddressRequest
import com.iprism.medrayder.models.addresslist.AddressListApiResponse
import com.iprism.medrayder.models.addresslist.AddressListRequest
import com.iprism.medrayder.models.checkpackage.CheckPaymentApiResponse
import com.iprism.medrayder.models.checkpackage.CheckPaymentRequest
import com.iprism.medrayder.models.createcashfreeorder.CreateCashFreeOrderApiResponse
import com.iprism.medrayder.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.medrayder.network.MedRayderApi

class CashFreePaymentRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun createOrder(request : CreateCashFreeOrderRequest): CreateCashFreeOrderApiResponse {
        return apiService.createCashFreeOrder(request)
    }

    suspend fun checkPayment(request : CheckPaymentRequest): CheckPaymentApiResponse {
        return apiService.checkPayment(request)
    }
}
