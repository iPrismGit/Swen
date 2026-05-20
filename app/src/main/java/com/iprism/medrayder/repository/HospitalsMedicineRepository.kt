package com.iprism.medrayder.repository

import com.iprism.medrayder.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsApiResponse
import com.iprism.medrayder.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsRequest
import com.iprism.medrayder.models.hospitalmedicineaddtocart.HospitalMedineProductsAddToCartApiResponse
import com.iprism.medrayder.models.hospitalmedicineaddtocart.HospitalMedineProductsAddToCartRequest
import com.iprism.medrayder.models.hospitalmedicinebooking.HospitalMedicineBookingApiResponse
import com.iprism.medrayder.models.hospitalmedicinebooking.HospitalMedicineBookingRequest
import com.iprism.medrayder.models.hospitalmedicineproductcart.HospitalMedicineProductCartRequest
import com.iprism.medrayder.models.hospitalmedicineproducts.HospitalMedicineProductsApiResponse
import com.iprism.medrayder.models.hospitalmedicineproducts.HospitalMedicineProductsRequest
import com.iprism.medrayder.models.hospitalmedicines.HospitalMedicinesApiResponse
import com.iprism.medrayder.models.hospitalmedicines.HospitalMedicinesRequest
import com.iprism.medrayder.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingApiResponse
import com.iprism.medrayder.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingRequest
import com.iprism.medrayder.models.hospitalpharmacyprescriptionbooking.HospitalPharmacyPrescriptionBookingApiResponse
import com.iprism.medrayder.models.hospitalpharmacyprescriptionbooking.HospitalPharmacyPrescriptionBookingRequest
import com.iprism.medrayder.models.pharmacyproductcart.PharmacyProductCartApiResponse
import com.iprism.medrayder.network.MedRayderApi

class HospitalsMedicineRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun fetchHospitalMedCategories(request : HospitalMedicinesRequest): HospitalMedicinesApiResponse {
        return apiService.fetchHospitalMedCategories(request)
    }

    suspend fun fetchHospitalMedProducts(request : HospitalMedicineProductsRequest): HospitalMedicineProductsApiResponse {
        return apiService.fetchHospitalMedProducts(request)
    }

    suspend fun addToCartProduct(request: HospitalMedineProductsAddToCartRequest): HospitalMedineProductsAddToCartApiResponse {
        return apiService.addToCartHospitalMedicineProduct(request)
    }

    suspend fun getHospitalMedProductCart(request: HospitalMedicineProductCartRequest): PharmacyProductCartApiResponse {
        return apiService.getHospitalProductCart(request)
    }

    suspend fun bookHospitalMedicineBooking(request: HospitalMedicineBookingRequest): HospitalMedicineBookingApiResponse {
        return apiService.bookHospitalMedicine(request)
    }

    suspend fun fetchHospitalMedOngoingBookings(request: HospitalMedicineOngoingBookingRequest): HospitalMedicineOngoingBookingApiResponse {
        return apiService.fetchHospitalMedOngoingBookings(request)
    }

    suspend fun fetchHospitalMedCompletedBookings(request: HospitalMedicineOngoingBookingRequest): HospitalMedicineOngoingBookingApiResponse {
        return apiService.fetchHospitalMedCompletedBookings(request)
    }

    suspend fun fetchHospitalMedBookingDetails(request: HospitalMedicineBookingDetailsRequest): HospitalMedicineBookingDetailsApiResponse {
        return apiService.fetchHospitalMedBookingDetails(request)
    }

    suspend fun bookHospitalMedPrescription(request: HospitalPharmacyPrescriptionBookingRequest): HospitalPharmacyPrescriptionBookingApiResponse {
        return apiService.bookHospitalMedPrescription(request)
    }
}