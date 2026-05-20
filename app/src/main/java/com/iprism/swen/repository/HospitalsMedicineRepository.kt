package com.iprism.swen.repository

import com.iprism.swen.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsApiResponse
import com.iprism.swen.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsRequest
import com.iprism.swen.models.hospitalmedicineaddtocart.HospitalMedineProductsAddToCartApiResponse
import com.iprism.swen.models.hospitalmedicineaddtocart.HospitalMedineProductsAddToCartRequest
import com.iprism.swen.models.hospitalmedicinebooking.HospitalMedicineBookingApiResponse
import com.iprism.swen.models.hospitalmedicinebooking.HospitalMedicineBookingRequest
import com.iprism.swen.models.hospitalmedicineproductcart.HospitalMedicineProductCartRequest
import com.iprism.swen.models.hospitalmedicineproducts.HospitalMedicineProductsApiResponse
import com.iprism.swen.models.hospitalmedicineproducts.HospitalMedicineProductsRequest
import com.iprism.swen.models.hospitalmedicines.HospitalMedicinesApiResponse
import com.iprism.swen.models.hospitalmedicines.HospitalMedicinesRequest
import com.iprism.swen.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingApiResponse
import com.iprism.swen.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingRequest
import com.iprism.swen.models.hospitalpharmacyprescriptionbooking.HospitalPharmacyPrescriptionBookingApiResponse
import com.iprism.swen.models.hospitalpharmacyprescriptionbooking.HospitalPharmacyPrescriptionBookingRequest
import com.iprism.swen.models.pharmacyproductcart.PharmacyProductCartApiResponse
import com.iprism.swen.network.MedRayderApi

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