package com.iprism.swen.repository

import com.iprism.swen.models.addfamilymember.AddFamilyMemberApiResponse
import com.iprism.swen.models.addfamilymember.AddFamilyMemberRequest
import com.iprism.swen.models.login.LoginApiResponse
import com.iprism.swen.models.login.LoginRequest
import com.iprism.swen.models.profile.ProfileApiResponse
import com.iprism.swen.models.profile.ProfileRequest
import com.iprism.swen.models.profileedit.ProfileEditApiResponse
import com.iprism.swen.models.profileedit.ProfileEditRequest
import com.iprism.swen.models.register.RegisterApiResponse
import com.iprism.swen.models.register.RegisterRequest
import com.iprism.swen.models.resendotp.ResendOtpApiResponse
import com.iprism.swen.models.resendotp.ResendOtpRequest
import com.iprism.swen.models.userdropdowns.UserDropDownApiResponse
import com.iprism.swen.models.userdropdowns.UserDropDownRequest
import com.iprism.swen.network.SwenAPi

class AuthRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun login(loginRequest: LoginRequest): LoginApiResponse {
        return apiService.login(loginRequest)
    }

    suspend fun resendOtp(resendOtpRequest: ResendOtpRequest): ResendOtpApiResponse {
        return apiService.resendOtp(resendOtpRequest)
    }

    suspend fun fetchUserDropDowns(request : UserDropDownRequest): UserDropDownApiResponse {
        return apiService.fetchUserDropDowns()
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