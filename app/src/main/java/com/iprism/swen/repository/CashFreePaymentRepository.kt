package com.iprism.swen.repository

import com.iprism.swen.models.checkpackage.CheckPaymentApiResponse
import com.iprism.swen.models.checkpackage.CheckPaymentRequest
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderApiResponse
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.swen.network.MedRayderApi

class CashFreePaymentRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun createOrder(request : CreateCashFreeOrderRequest): CreateCashFreeOrderApiResponse {
        return apiService.createCashFreeOrder(request)
    }

    suspend fun checkPayment(request : CheckPaymentRequest): CheckPaymentApiResponse {
        return apiService.checkPayment(request)
    }
}
