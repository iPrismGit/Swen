package com.iprism.medrayder.repository

import com.iprism.medrayder.models.bookpharmacyproduct.BookPharmacyProductApiResponse
import com.iprism.medrayder.models.bookpharmacyproduct.BookPharmacyProductRequest
import com.iprism.medrayder.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsApiResponse
import com.iprism.medrayder.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsRequest
import com.iprism.medrayder.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingApiResponse
import com.iprism.medrayder.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingRequest
import com.iprism.medrayder.models.pharmacies.PharmaciesApiResponse
import com.iprism.medrayder.models.pharmacies.PharmaciesRequest
import com.iprism.medrayder.models.pharmacyDetails.PharmacyDetailsApiResponse
import com.iprism.medrayder.models.pharmacyDetails.PharmacyDetailsRequest
import com.iprism.medrayder.models.pharmacyaddtocart.PharmacyProductAddToCartApiResponse
import com.iprism.medrayder.models.pharmacyaddtocart.PharmacyProductAddToCartRequest
import com.iprism.medrayder.models.pharmacyongoingbookings.PharmacyOnGoingBookingsApiResponse
import com.iprism.medrayder.models.pharmacyongoingbookings.PharmacyOnGoingBookingsRequest
import com.iprism.medrayder.models.pharmacyprescriptionbooking.PharmacyPrescriptionBookingApiResponse
import com.iprism.medrayder.models.pharmacyprescriptionbooking.PharmacyPrescriptionBookingRequest
import com.iprism.medrayder.models.pharmacyproductcart.PharmacyProductCartApiResponse
import com.iprism.medrayder.models.pharmacyproductcart.PharmacyProductCartRequest
import com.iprism.medrayder.models.pharmacyproducts.PharmacyProductsApiResponse
import com.iprism.medrayder.models.pharmacyproducts.PharmacyProductsRequest
import com.iprism.medrayder.network.MedRayderApi

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