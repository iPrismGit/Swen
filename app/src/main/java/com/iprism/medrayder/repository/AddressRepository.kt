package com.iprism.medrayder.repository

import com.iprism.medrayder.models.address.AddAddressApiResponse
import com.iprism.medrayder.models.address.AddAddressRequest
import com.iprism.medrayder.models.addresslist.AddressListApiResponse
import com.iprism.medrayder.models.addresslist.AddressListRequest
import com.iprism.medrayder.models.familymembers.FamilyMembersApiResponse
import com.iprism.medrayder.models.familymembers.FamilyMembersRequest
import com.iprism.medrayder.models.homepage.HomePageApiResponse
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.userdropdowns.UserDropDownApiResponse
import com.iprism.medrayder.models.userdropdowns.UserDropDownRequest
import com.iprism.medrayder.models.wallet.WalletApiResponse
import com.iprism.medrayder.models.wallet.WalletRequest
import com.iprism.medrayder.network.MedRayderApi

class AddressRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun addAddress(request : AddAddressRequest): AddAddressApiResponse {
        return apiService.addAddress(request)
    }

    suspend fun fetchAddressList(request : AddressListRequest): AddressListApiResponse {
        return apiService.fetchAddressList(request)
    }
}
