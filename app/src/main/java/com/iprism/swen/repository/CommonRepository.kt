package com.iprism.swen.repository

import com.iprism.swen.models.contactus.ContactUsApiResponse
import com.iprism.swen.models.contactus.ContactUsRequest
import com.iprism.swen.models.contentpages.ContentPagesApiResponse
import com.iprism.swen.models.contentpages.ContentPagesRequest
import com.iprism.swen.models.familymembers.FamilyMembersApiResponse
import com.iprism.swen.models.familymembers.FamilyMembersRequest
import com.iprism.swen.models.homepage.HomePageApiResponse
import com.iprism.swen.models.homepage.HomePageRequest
import com.iprism.swen.models.medlocker.MedLockerApiResponse
import com.iprism.swen.models.medlocker.MedLockerRequest
import com.iprism.swen.models.notifications.NotificationsApiResponse
import com.iprism.swen.models.notifications.NotificationsRequest
import com.iprism.swen.models.treatmentplaning.TreatmentPlaningApiResponse
import com.iprism.swen.models.treatmentplaning.TreatmentPlaningRequest
import com.iprism.swen.network.MedRayderApi

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
