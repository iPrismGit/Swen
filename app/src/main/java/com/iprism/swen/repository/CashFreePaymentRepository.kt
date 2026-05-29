package com.iprism.swen.repository

import com.iprism.swen.models.checkpackage.CheckPaymentApiResponse
import com.iprism.swen.models.checkpackage.CheckPaymentRequest
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderApiResponse
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.swen.network.SwenAPi

class CashFreePaymentRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun createOrder(request : CreateCashFreeOrderRequest): CreateCashFreeOrderApiResponse {
        return apiService.createCashFreeOrder(request)
    }

    suspend fun checkPayment(request : CheckPaymentRequest): CheckPaymentApiResponse {
        return apiService.checkPayment(request)
    }
}
