package com.iprism.swen.repository

import com.iprism.swen.models.wallet.WalletApiResponse
import com.iprism.swen.models.wallet.WalletRequest
import com.iprism.swen.models.wallethistory.WalletHistoryApiResponse
import com.iprism.swen.models.wallethistory.WalletHistoryRequest
import com.iprism.swen.network.MedRayderApi

class WalletRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun wallet(request : WalletRequest): WalletApiResponse {
        return apiService.wallet(request)
    }

    suspend fun fetchWalletHistory(request : WalletHistoryRequest): WalletHistoryApiResponse {
        return apiService.fetchWalletHistory(request)
    }
}
