package com.iprism.swen.repository

import com.iprism.swen.models.addfamilymembersub.AddFamilyMemberSubRequest
import com.iprism.swen.models.ecard.ECardApiResponse
import com.iprism.swen.models.ecard.ECardRequest
import com.iprism.swen.models.subscription.SubscriptionApiResponse
import com.iprism.swen.models.subscription.SubscriptionRequest
import com.iprism.swen.models.subscriptiondetails.SubscriptionDetailsApiResponse
import com.iprism.swen.models.subscriptiondetails.SubscriptionDetailsRequest
import com.iprism.swen.network.MedRayderApi

class SubscriptionRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun subscription(request : SubscriptionRequest): SubscriptionApiResponse {
        return apiService.subscription(request)
    }

    suspend fun fetchSubscriptionDetails(request : SubscriptionDetailsRequest): SubscriptionDetailsApiResponse {
        return apiService.fetchSubscriptionDetails(request)
    }

    suspend fun singleSubscription(request : AddFamilyMemberSubRequest): SubscriptionApiResponse {
        return apiService.singleSubscription(request)
    }

    suspend fun fetchECard(request : ECardRequest): ECardApiResponse {
        return apiService.fetchECard(request)
    }
}
