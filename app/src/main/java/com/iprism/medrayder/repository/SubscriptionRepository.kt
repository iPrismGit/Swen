package com.iprism.medrayder.repository

import com.iprism.medrayder.models.addfamilymembersub.AddFamilyMemberSubRequest
import com.iprism.medrayder.models.address.AddAddressApiResponse
import com.iprism.medrayder.models.address.AddAddressRequest
import com.iprism.medrayder.models.addresslist.AddressListApiResponse
import com.iprism.medrayder.models.addresslist.AddressListRequest
import com.iprism.medrayder.models.ecard.ECardApiResponse
import com.iprism.medrayder.models.ecard.ECardRequest
import com.iprism.medrayder.models.familymembers.FamilyMembersApiResponse
import com.iprism.medrayder.models.familymembers.FamilyMembersRequest
import com.iprism.medrayder.models.homepage.HomePageApiResponse
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.subscription.SubscriptionApiResponse
import com.iprism.medrayder.models.subscription.SubscriptionRequest
import com.iprism.medrayder.models.subscriptiondetails.SubscriptionDetailsApiResponse
import com.iprism.medrayder.models.subscriptiondetails.SubscriptionDetailsRequest
import com.iprism.medrayder.models.userdropdowns.UserDropDownApiResponse
import com.iprism.medrayder.models.userdropdowns.UserDropDownRequest
import com.iprism.medrayder.models.wallet.WalletApiResponse
import com.iprism.medrayder.models.wallet.WalletRequest
import com.iprism.medrayder.network.MedRayderApi

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
