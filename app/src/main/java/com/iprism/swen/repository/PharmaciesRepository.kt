package com.iprism.swen.repository

import com.iprism.swen.models.bookpharmacyproduct.BookPharmacyProductApiResponse
import com.iprism.swen.models.bookpharmacyproduct.BookPharmacyProductRequest
import com.iprism.swen.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsApiResponse
import com.iprism.swen.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsRequest
import com.iprism.swen.models.pharmacies.PharmaciesApiResponse
import com.iprism.swen.models.pharmacies.PharmaciesRequest
import com.iprism.swen.models.pharmacyDetails.PharmacyDetailsApiResponse
import com.iprism.swen.models.pharmacyDetails.PharmacyDetailsRequest
import com.iprism.swen.models.pharmacyaddtocart.PharmacyProductAddToCartApiResponse
import com.iprism.swen.models.pharmacyaddtocart.PharmacyProductAddToCartRequest
import com.iprism.swen.models.pharmacyongoingbookings.PharmacyOnGoingBookingsApiResponse
import com.iprism.swen.models.pharmacyongoingbookings.PharmacyOnGoingBookingsRequest
import com.iprism.swen.models.pharmacyprescriptionbooking.PharmacyPrescriptionBookingApiResponse
import com.iprism.swen.models.pharmacyprescriptionbooking.PharmacyPrescriptionBookingRequest
import com.iprism.swen.models.pharmacyproductcart.PharmacyProductCartApiResponse
import com.iprism.swen.models.pharmacyproductcart.PharmacyProductCartRequest
import com.iprism.swen.models.pharmacyproducts.PharmacyProductsApiResponse
import com.iprism.swen.models.pharmacyproducts.PharmacyProductsRequest
import com.iprism.swen.network.MedRayderApi

class PharmaciesRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun fetchPharmacies(request: PharmaciesRequest): PharmaciesApiResponse {
        return apiService.fetchPharmacies(request)
    }

    suspend fun fetchPharmacyDetails(request: PharmacyDetailsRequest): PharmacyDetailsApiResponse {
        return apiService.fetchPharmacyDetails(request)
    }

    suspend fun fetchPharmacyProducts(request: PharmacyProductsRequest): PharmacyProductsApiResponse {
        return apiService.fetchPharmacyProducts(request)
    }

    suspend fun addToCartPharmacyProduct(request: PharmacyProductAddToCartRequest): PharmacyProductAddToCartApiResponse {
        return apiService.addToCartPharmacyProduct(request)
    }

    suspend fun getPharmacyProductCart(request: PharmacyProductCartRequest): PharmacyProductCartApiResponse {
        return apiService.getPharmacyProductCart(request)
    }

    suspend fun bookPharmacyProduct(request: BookPharmacyProductRequest): BookPharmacyProductApiResponse {
        return apiService.bookPharmacyProduct(request)
    }

    suspend fun fetchPharmacyOngoingBookings(request: PharmacyOnGoingBookingsRequest): PharmacyOnGoingBookingsApiResponse {
        return apiService.fetchPharmacyOngoingBookings(request)
    }

    suspend fun fetchPharmacyCompletedBookings(request: PharmacyOnGoingBookingsRequest): PharmacyOnGoingBookingsApiResponse {
        return apiService.fetchPharmacyCompletedBookings(request)
    }

    suspend fun fetchPharmacyBookingDetails(request: HospitalMedicineBookingDetailsRequest): HospitalMedicineBookingDetailsApiResponse {
        return apiService.fetchPharmacyBookingDetails(request)
    }

    suspend fun bookPharmacyPrescriptionOrder(request: PharmacyPrescriptionBookingRequest): PharmacyPrescriptionBookingApiResponse {
        return apiService.bookPharmacyPrescriptionOrder(request)
    }
}