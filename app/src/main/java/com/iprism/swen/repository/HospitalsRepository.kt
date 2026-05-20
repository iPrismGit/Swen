package com.iprism.swen.repository

import com.iprism.swen.models.admit.AdmitBookingApiResponse
import com.iprism.swen.models.admit.AdmitBookingRequest
import com.iprism.swen.models.admitbookingdetails.AdmitBookingDetailsAPiResponse
import com.iprism.swen.models.admitbookingdetails.AdmitBookingDetailsRequest
import com.iprism.swen.models.ambulancebooking.AmbulanceBookingApiResponse
import com.iprism.swen.models.ambulancebooking.AmbulanceBookingRequest
import com.iprism.swen.models.ambulancetracking.AmbulanceTrackingApiResponse
import com.iprism.swen.models.ambulancetracking.AmbulanceTrackingRequest
import com.iprism.swen.models.filters.FiltersApiResponse
import com.iprism.swen.models.filters.FiltersRequest
import com.iprism.swen.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingApiResponse
import com.iprism.swen.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingRequest
import com.iprism.swen.models.hospitalambulancebookings.HospitalAmbulanceBookingApiResponse
import com.iprism.swen.models.hospitaldetails.HospitalDetailsApiResponse
import com.iprism.swen.models.hospitaldetails.HospitalDetailsRequest
import com.iprism.swen.models.hospitaldoctorbooking.HospitalDoctorBookingApiResponse
import com.iprism.swen.models.hospitaldoctorbooking.HospitalDoctorBookingRequest
import com.iprism.swen.models.hospitaldoctors.HospitalDoctorsApiResponse
import com.iprism.swen.models.hospitaldoctors.HospitalDoctorsRequest
import com.iprism.swen.models.hospitaldoctortimeslots.HospitalDoctorTimeSlotsRequest
import com.iprism.swen.models.hospitals.HospitalsApiResponse
import com.iprism.swen.models.hospitals.HospitalsRequest
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.models.maindatahospitals.MainDataHospitalsApiResponse
import com.iprism.swen.models.maindatahospitals.MainDataHospitalsRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.swen.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryApiResponse
import com.iprism.swen.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.swen.models.onlinedoctorrating.OnlineDoctorRatingApiResponse
import com.iprism.swen.models.onlinedoctorrating.OnlineDoctorRatingRequest
import com.iprism.swen.models.onlinedoctorscoupons.CouponsApiResponse
import com.iprism.swen.models.onlinedoctorscoupons.CouponRequest
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsApiResponse
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsRequest
import com.iprism.swen.network.MedRayderApi

class HospitalsRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun getHospitals(request : HospitalsRequest): HospitalsApiResponse {
        return apiService.getHospitals(request)
    }

    suspend fun fetchFilters(request : FiltersRequest): FiltersApiResponse {
        return apiService.getFilters(request)
    }

    suspend fun getMainDataHospitals(request : MainDataHospitalsRequest): MainDataHospitalsApiResponse {
        return apiService.getMainData(request)
    }

    suspend fun getHospitalDetails(request : HospitalDetailsRequest): HospitalDetailsApiResponse {
        return apiService.getHospitalDetails(request)
    }

    suspend fun getHospitalDoctors(request : HospitalDoctorsRequest): HospitalDoctorsApiResponse {
        return apiService.getHospitalDoctors(request)
    }

    suspend fun getHospitalDoctorTimeSlots(request: HospitalDoctorTimeSlotsRequest): OnlineDoctorBookingDetailsApiResponse {
        return apiService.getHospitalDoctorTimeSlots(request)
    }

    suspend fun bookHospitalDoctor(request: HospitalDoctorBookingRequest): HospitalDoctorBookingApiResponse {
        return apiService.bookHospitalDoctor(request)
    }

    suspend fun getHospitalDoctorCoupons(request: CouponRequest): CouponsApiResponse {
        return apiService.getHospitalDoctorCoupons(request)
    }

    suspend fun getHospitalDoctorBookingsHistory(request: OnlineDoctorBookingHistoryRequest): OnlineDoctorBookingHistoryApiResponse {
        return apiService.getHospitalDoctorBookingsHistory(request)
    }

    suspend fun getHospitalDoctorCompletedBookings(request: OnlineDoctorBookingHistoryRequest): OnlineDoctorBookingHistoryApiResponse {
        return apiService.getHospitalDoctorCompletedBookingHistory(request)
    }

    suspend fun getHospitalDoctorSingleBookingDetails(request: OnlineDoctorSingleBookingDetailsRequest): OnlineDoctorSingleBookingDetailsApiResponse {
        return apiService.getHospitalDoctorSingleBookingDetails(request)
    }

    suspend fun bookHospitalAdmit(request: AdmitBookingRequest): AdmitBookingApiResponse {
        return apiService.bookHospitalAdmit(request)
    }

    suspend fun fetchHospitalAdmissionOngoingBookings(request: HospitalAdmissionOnGoingRequest): HospitalAdmissionOnGoingApiResponse {
        return apiService.fetchAdmitOnGoingBookings(request)
    }

    suspend fun fetchHospitalAdmissionCompletedBookings(request: HospitalAdmissionOnGoingRequest): HospitalAdmissionOnGoingApiResponse {
        return apiService.fetchAdmitCompletedBookings(request)
    }

    suspend fun fetchHospitalAdmissionBookingDetails(request: AdmitBookingDetailsRequest): AdmitBookingDetailsAPiResponse {
        return apiService.fetchAdmitBookingDetails(request)
    }

    suspend fun bookHospitalAmbulance(request: AmbulanceBookingRequest): AmbulanceBookingApiResponse {
        return apiService.bookHospitalAmbulance(request)
    }

    suspend fun trackAmbulance(request: AmbulanceTrackingRequest): AmbulanceTrackingApiResponse {
        return apiService.trackAmbulance(request)
    }

    suspend fun fetchHospitalAmbulanceOngoingBookings(request: LabTestBookingsRequest): HospitalAmbulanceBookingApiResponse {
        return apiService.fetchAmbulanceOngoingBookings(request)
    }

    suspend fun insertHospitalDoctorRating(request: OnlineDoctorRatingRequest): OnlineDoctorRatingApiResponse {
        return apiService.insertHospitalDoctorRating(request)
    }

    suspend fun fetchHospitalAmbulanceCompletedBookings(request: LabTestBookingsRequest): HospitalAmbulanceBookingApiResponse {
        return apiService.fetchAmbulanceCompletedBookings(request)
    }
}