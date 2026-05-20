package com.iprism.medrayder.repository

import com.iprism.medrayder.models.contactus.ContactUsApiResponse
import com.iprism.medrayder.models.contactus.ContactUsRequest
import com.iprism.medrayder.models.contentpages.ContentPagesApiResponse
import com.iprism.medrayder.models.contentpages.ContentPagesRequest
import com.iprism.medrayder.models.familymembers.FamilyMembersApiResponse
import com.iprism.medrayder.models.familymembers.FamilyMembersRequest
import com.iprism.medrayder.models.filters.FiltersApiResponse
import com.iprism.medrayder.models.filters.FiltersRequest
import com.iprism.medrayder.models.homepage.HomePageApiResponse
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.medlocker.MedLockerApiResponse
import com.iprism.medrayder.models.medlocker.MedLockerRequest
import com.iprism.medrayder.models.notifications.NotificationsApiResponse
import com.iprism.medrayder.models.notifications.NotificationsRequest
import com.iprism.medrayder.models.treatmentplaning.TreatmentPlaningApiResponse
import com.iprism.medrayder.models.treatmentplaning.TreatmentPlaningRequest
import com.iprism.medrayder.models.userdropdowns.UserDropDownApiResponse
import com.iprism.medrayder.models.userdropdowns.UserDropDownRequest
import com.iprism.medrayder.models.wallet.WalletApiResponse
import com.iprism.medrayder.models.wallet.WalletRequest
import com.iprism.medrayder.network.MedRayderApi

class CommonRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun fetchHomePage(request : HomePageRequest): HomePageApiResponse {
        return apiService.fetchHomePage(request)
    }

    suspend fun fetchFamilyMembers(request : FamilyMembersRequest): FamilyMembersApiResponse {
        return apiService.fetchFamilyMembers(request)
    }

    suspend fun fetchContentPage(request : ContentPagesRequest): ContentPagesApiResponse {
        return apiService.fetchContentPage(request)
    }

    suspend fun medLocker(request : MedLockerRequest): MedLockerApiResponse {
        return apiService.medLocker(request)
    }

    suspend fun treatmentPlanning(request : TreatmentPlaningRequest): TreatmentPlaningApiResponse {
        return apiService.treatmentPlanning(request)
    }

    suspend fun contactUs(request : ContactUsRequest): ContactUsApiResponse {
        return apiService.contactUs(request)
    }

    suspend fun fetchNotifications(request : NotificationsRequest): NotificationsApiResponse {
        return apiService.fetchNotifications(request)
    }
}
