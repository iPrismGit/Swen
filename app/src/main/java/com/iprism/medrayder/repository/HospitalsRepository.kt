package com.iprism.medrayder.repository

import com.iprism.medrayder.models.admit.AdmitBookingApiResponse
import com.iprism.medrayder.models.admit.AdmitBookingRequest
import com.iprism.medrayder.models.admitbookingdetails.AdmitBookingDetailsAPiResponse
import com.iprism.medrayder.models.admitbookingdetails.AdmitBookingDetailsRequest
import com.iprism.medrayder.models.ambulancebooking.AmbulanceBookingApiResponse
import com.iprism.medrayder.models.ambulancebooking.AmbulanceBookingRequest
import com.iprism.medrayder.models.ambulancetracking.AmbulanceTrackingApiResponse
import com.iprism.medrayder.models.ambulancetracking.AmbulanceTrackingRequest
import com.iprism.medrayder.models.filters.FiltersApiResponse
import com.iprism.medrayder.models.filters.FiltersRequest
import com.iprism.medrayder.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingApiResponse
import com.iprism.medrayder.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingRequest
import com.iprism.medrayder.models.hospitalambulancebookings.HospitalAmbulanceBookingApiResponse
import com.iprism.medrayder.models.hospitaldetails.HospitalDetailsApiResponse
import com.iprism.medrayder.models.hospitaldetails.HospitalDetailsRequest
import com.iprism.medrayder.models.hospitaldoctorbooking.HospitalDoctorBookingApiResponse
import com.iprism.medrayder.models.hospitaldoctorbooking.HospitalDoctorBookingRequest
import com.iprism.medrayder.models.hospitaldoctors.HospitalDoctorsApiResponse
import com.iprism.medrayder.models.hospitaldoctors.HospitalDoctorsRequest
import com.iprism.medrayder.models.hospitaldoctortimeslots.HospitalDoctorTimeSlotsRequest
import com.iprism.medrayder.models.hospitals.HospitalsApiResponse
import com.iprism.medrayder.models.hospitals.HospitalsRequest
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsRequest
import com.iprism.medrayder.models.maindatahospitals.MainDataHospitalsApiResponse
import com.iprism.medrayder.models.maindatahospitals.MainDataHospitalsRequest
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryApiResponse
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.medrayder.models.onlinedoctorrating.OnlineDoctorRatingApiResponse
import com.iprism.medrayder.models.onlinedoctorrating.OnlineDoctorRatingRequest
import com.iprism.medrayder.models.onlinedoctorscoupons.CouponsApiResponse
import com.iprism.medrayder.models.onlinedoctorscoupons.CouponRequest
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsRequest
import com.iprism.medrayder.network.MedRayderApi

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