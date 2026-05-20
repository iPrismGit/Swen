package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.addfamilymember.AddFamilyMemberApiResponse
import com.iprism.swen.models.addfamilymember.AddFamilyMemberRequest
import com.iprism.swen.models.profile.ProfileApiResponse
import com.iprism.swen.models.profile.ProfileRequest
import com.iprism.swen.models.profileedit.ProfileEditApiResponse
import com.iprism.swen.models.profileedit.ProfileEditRequest
import com.iprism.swen.models.userdropdowns.UserDropDownApiResponse
import com.iprism.swen.models.userdropdowns.UserDropDownRequest
import com.iprism.swen.repository.AuthRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class AddFamilyMemberViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<UserDropDownApiResponse>>()
    val response: LiveData<UiState<UserDropDownApiResponse>> = _response

    private val _addFamilyResponse = MutableLiveData<UiState<AddFamilyMemberApiResponse>>()
    val addFamilyResponse: LiveData<UiState<AddFamilyMemberApiResponse>> = _addFamilyResponse

    private val _profileResponse = MutableLiveData<UiState<ProfileApiResponse>>()
    val profileResponse: LiveData<UiState<ProfileApiResponse>> = _profileResponse

    private val _profileEditResponse = MutableLiveData<UiState<ProfileEditApiResponse>>()
    val profileEditResponse: LiveData<UiState<ProfileEditApiResponse>> = _profileEditResponse

    fun fetchUserDropDowns(request: UserDropDownRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchUserDropDowns(request)
                if (response.status) {
                    _response.value = UiState.Success(response)
                } else {
                    _response.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _response.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun registerUser(request: AddFamilyMemberRequest) {
        viewModelScope.launch {
            _addFamilyResponse.value = UiState.Loading
            try {
                val response = repository.addFamilyMember(request)
                if (response.status) {
                    _addFamilyResponse.value = UiState.Success(response)
                } else {
                    _addFamilyResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _addFamilyResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun profileDetails(request: ProfileRequest) {
        viewModelScope.launch {
            _profileResponse.value = UiState.Loading
            try {
                val response = repository.profileDetails(request)
                if (response.status) {
                    _profileResponse.value = UiState.Success(response)
                } else {
                    _profileResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _profileResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun profileEdit(request: ProfileEditRequest) {
        viewModelScope.launch {
            _profileEditResponse.value = UiState.Loading
            try {
                val response = repository.profileEdit(request)
                if (response.status) {
                    _profileEditResponse.value = UiState.Success(response)
                } else {
                    _profileEditResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _profileEditResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}