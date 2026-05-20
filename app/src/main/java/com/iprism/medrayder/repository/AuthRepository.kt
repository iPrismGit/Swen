package com.iprism.medrayder.repository

import com.iprism.medrayder.models.addfamilymember.AddFamilyMemberApiResponse
import com.iprism.medrayder.models.addfamilymember.AddFamilyMemberRequest
import com.iprism.medrayder.models.login.LoginApiResponse
import com.iprism.medrayder.models.login.LoginRequest
import com.iprism.medrayder.models.profile.ProfileApiResponse
import com.iprism.medrayder.models.profile.ProfileRequest
import com.iprism.medrayder.models.profileedit.ProfileEditApiResponse
import com.iprism.medrayder.models.profileedit.ProfileEditRequest
import com.iprism.medrayder.models.register.RegisterApiResponse
import com.iprism.medrayder.models.register.RegisterRequest
import com.iprism.medrayder.models.resendotp.ResendOtpApiResponse
import com.iprism.medrayder.models.resendotp.ResendOtpRequest
import com.iprism.medrayder.models.userdropdowns.UserDropDownApiResponse
import com.iprism.medrayder.models.userdropdowns.UserDropDownRequest
import com.iprism.medrayder.network.MedRayderApi

class AuthRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun login(loginRequest: LoginRequest): LoginApiResponse {
        return apiService.login(loginRequest)
    }

    suspend fun resendOtp(resendOtpRequest: ResendOtpRequest): ResendOtpApiResponse {
        return apiService.resendOtp(resendOtpRequest)
    }

    suspend fun fetchUserDropDowns(request : UserDropDownRequest): UserDropDownApiResponse {
        return apiService.fetchUserDropDowns(request)
    }

    suspend fun registerUser(request : RegisterRequest): RegisterApiResponse {
        return apiService.registerUser(request)
    }

    suspend fun addFamilyMember(request : AddFamilyMemberRequest): AddFamilyMemberApiResponse {
        return apiService.addFamilyMember(request)
    }

    suspend fun profileDetails(request : ProfileRequest): ProfileApiResponse {
        return apiService.profileDetails(request)
    }

    suspend fun profileEdit(request : ProfileEditRequest): ProfileEditApiResponse {
        return apiService.profileEdit(request)
    }
}