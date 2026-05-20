package com.iprism.medrayder.repository

import com.iprism.medrayder.models.familymembers.FamilyMembersApiResponse
import com.iprism.medrayder.models.familymembers.FamilyMembersRequest
import com.iprism.medrayder.models.homepage.HomePageApiResponse
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.userdropdowns.UserDropDownApiResponse
import com.iprism.medrayder.models.userdropdowns.UserDropDownRequest
import com.iprism.medrayder.models.wallet.WalletApiResponse
import com.iprism.medrayder.models.wallet.WalletRequest
import com.iprism.medrayder.models.wallethistory.WalletHistoryApiResponse
import com.iprism.medrayder.models.wallethistory.WalletHistoryRequest
import com.iprism.medrayder.network.MedRayderApi

class WalletRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun wallet(request : WalletRequest): WalletApiResponse {
        return apiService.wallet(request)
    }

    suspend fun fetchWalletHistory(request : WalletHistoryRequest): WalletHistoryApiResponse {
        return apiService.fetchWalletHistory(request)
    }
}
