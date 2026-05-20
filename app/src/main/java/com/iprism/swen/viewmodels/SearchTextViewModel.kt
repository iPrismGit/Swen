package com.iprism.swen.viewmodels

import SearchData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchTextViewModel : ViewModel() {
    private val _searchData = MutableLiveData<SearchData>()
    val searchData: LiveData<SearchData> = _searchData

    fun setSearchData(search: String, type: String, lat: String, lon: String) {
        _searchData.value = SearchData(lat, lon, type, search)
    }
}