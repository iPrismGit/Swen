package com.iprism.swen.repository

import com.iprism.swen.models.allsurgeryquotes.AllSurgeryQuotesApiResponse
import com.iprism.swen.models.allsurgeryquotes.AllSurgeryQuotesRequest
import com.iprism.swen.models.insertsurgicalquote.InsertSurgicalQuoteApiResponse
import com.iprism.swen.models.insertsurgicalquote.InsertSurgicalQuoteRequest
import com.iprism.swen.network.SwenAPi

class SurgicalQuoteRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun fetchAllSurgicalQuoteCategories(request: AllSurgeryQuotesRequest): AllSurgeryQuotesApiResponse {
        return apiService.fetchAllSurgeryQuoteCategories(request)
    }

    suspend fun insertSurgicalQuote(request: InsertSurgicalQuoteRequest): InsertSurgicalQuoteApiResponse {
        return apiService.insertSurgicalQuote(request)
    }
}