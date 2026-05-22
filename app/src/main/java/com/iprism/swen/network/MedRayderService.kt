package com.iprism.swen.network

import com.iprism.swen.models.addfamilymember.AddFamilyMemberApiResponse
import com.iprism.swen.models.addfamilymember.AddFamilyMemberRequest
import com.iprism.swen.models.addfamilymembersub.AddFamilyMemberSubRequest
import com.iprism.swen.models.address.AddAddressApiResponse
import com.iprism.swen.models.address.AddAddressRequest
import com.iprism.swen.models.addresslist.AddressListApiResponse
import com.iprism.swen.models.addresslist.AddressListRequest
import com.iprism.swen.models.admit.AdmitBookingApiResponse
import com.iprism.swen.models.admit.AdmitBookingRequest
import com.iprism.swen.models.admitbookingdetails.AdmitBookingDetailsAPiResponse
import com.iprism.swen.models.admitbookingdetails.AdmitBookingDetailsRequest
import com.iprism.swen.models.ambulancebooking.AmbulanceBookingRequest
import com.iprism.swen.models.ambulancebooking.AmbulanceBookingApiResponse
import com.iprism.swen.models.ambulancetracking.AmbulanceTrackingApiResponse
import com.iprism.swen.models.ambulancetracking.AmbulanceTrackingRequest
import com.iprism.swen.models.bookpharmacyproduct.BookPharmacyProductApiResponse
import com.iprism.swen.models.bookpharmacyproduct.BookPharmacyProductRequest
import com.iprism.swen.models.checkpackage.CheckPaymentApiResponse
import com.iprism.swen.models.checkpackage.CheckPaymentRequest
import com.iprism.swen.models.contactus.ContactUsApiResponse
import com.iprism.swen.models.contactus.ContactUsRequest
import com.iprism.swen.models.contentpages.ContentPagesApiResponse
import com.iprism.swen.models.contentpages.ContentPagesRequest
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderApiResponse
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.swen.models.diagnosticbooking.DiagnosticBookingApiResponse
import com.iprism.swen.models.diagnosticbooking.DiagnosticBookingRequest
import com.iprism.swen.models.diagnosticbookingdetails.DiagnosticTestBookingDetailsApiResponse
import com.iprism.swen.models.diagnosticbookingdetails.DiagnosticTestBookingDetailsRequest
import com.iprism.swen.models.diagnosticprescriptionbooking.DiagnosticPrescriptionBookingRequest
import com.iprism.swen.models.diagnostictestbookings.DiagnosticTestsBookingsApiResponse
import com.iprism.swen.models.diagnostictests.DiagnosticTestsApiResponse
import com.iprism.swen.models.diagnostictests.DiagnosticTestsRequest
import com.iprism.swen.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.swen.models.diagnostictimings.DiagnosticSlotsRequest
import com.iprism.swen.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.swen.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.swen.models.ecard.ECardApiResponse
import com.iprism.swen.models.ecard.ECardRequest
import com.iprism.swen.models.familymembers.FamilyMembersApiResponse
import com.iprism.swen.models.familymembers.FamilyMembersRequest
import com.iprism.swen.models.filters.FiltersApiResponse
import com.iprism.swen.models.filters.FiltersRequest
import com.iprism.swen.models.homepage.HomePageApiResponse
import com.iprism.swen.models.homepage.HomePageRequest
import com.iprism.swen.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingApiResponse
import com.iprism.swen.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingRequest
import com.iprism.swen.models.hospitalambulancebookings.HospitalAmbulanceBookingApiResponse
import com.iprism.swen.models.hospitaldetails.HospitalDetailsApiResponse
import com.iprism.swen.models.hospitaldetails.HospitalDetailsRequest
import com.iprism.swen.models.hospitaldiagnosticbooking.HospitalDiagnosticBookingApiResponse
import com.iprism.swen.models.hospitaldiagnosticbooking.HospitalDiagnosticBookingRequest
import com.iprism.swen.models.hospitaldiagnosticbookingdetails.HospitalDiagnosticTestBookingDetailsApiResponse
import com.iprism.swen.models.hospitaldiagnosticbookingdetails.HospitalDiagnosticTestBookingDetailsRequest
import com.iprism.swen.models.hospitaldiagnosticprescriptionbooking.HospitalDiagnosticPrescriptionBookingApiResponse
import com.iprism.swen.models.hospitaldiagnosticprescriptionbooking.HospitalDiagnosticPrescriptionBookingRequest
import com.iprism.swen.models.hospitaldiagnostictests.HospitalDiagnosticTestsRequest
import com.iprism.swen.models.hospitaldoctorbooking.HospitalDoctorBookingApiResponse
import com.iprism.swen.models.hospitaldoctorbooking.HospitalDoctorBookingRequest
import com.iprism.swen.models.hospitaldoctors.HospitalDoctorsApiResponse
import com.iprism.swen.models.hospitaldoctors.HospitalDoctorsRequest
import com.iprism.swen.models.hospitaldoctortimeslots.HospitalDoctorTimeSlotsRequest
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
import com.iprism.swen.models.hospitalmedicinetimeslots.HospitalDiagnosticTimeRequest
import com.iprism.swen.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingApiResponse
import com.iprism.swen.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingRequest
import com.iprism.swen.models.hospitalpharmacyprescriptionbooking.HospitalPharmacyPrescriptionBookingApiResponse
import com.iprism.swen.models.hospitalpharmacyprescriptionbooking.HospitalPharmacyPrescriptionBookingRequest
import com.iprism.swen.models.hospitals.HospitalsApiResponse
import com.iprism.swen.models.hospitals.HospitalsRequest
import com.iprism.swen.models.labbooking.LabBookingApiResponse
import com.iprism.swen.models.labbooking.LabBookingRequest
import com.iprism.swen.models.labcenters.LabCentersApiResponse
import com.iprism.swen.models.labcenters.LabCentersRequest
import com.iprism.swen.models.labtestbookingdetails.LabTestBookingDetailsApiResponse
import com.iprism.swen.models.labtestbookingdetails.LabTestBookingDetailsRequest
import com.iprism.swen.models.labtestbookings.LabTestBookingsApiResponse
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.models.labtestprescriptionbooking.LabTestPrescriptionBookingApiResponse
import com.iprism.swen.models.labtestprescriptionbooking.LabTestPrescriptionBookingRequest
import com.iprism.swen.models.labtests.LabTestsApiResponse
import com.iprism.swen.models.labtests.LabTestsRequest
import com.iprism.swen.models.labtestslots.LabTestSlotsApiResponse
import com.iprism.swen.models.labtestslots.LabTestSlotsRequest
import com.iprism.swen.models.login.LoginApiResponse
import com.iprism.swen.models.login.LoginRequest
import com.iprism.swen.models.maindatahospitals.MainDataHospitalsApiResponse
import com.iprism.swen.models.maindatahospitals.MainDataHospitalsRequest
import com.iprism.swen.models.medlocker.MedLockerApiResponse
import com.iprism.swen.models.medlocker.MedLockerRequest
import com.iprism.swen.models.notifications.NotificationsApiResponse
import com.iprism.swen.models.notifications.NotificationsRequest
import com.iprism.swen.models.onlinedoctorbooking.OnlineDoctorBookingApiResponse
import com.iprism.swen.models.onlinedoctorbooking.OnlineDoctorBookingRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.swen.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsRequest
import com.iprism.swen.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryApiResponse
import com.iprism.swen.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.swen.models.onlinedoctorrating.OnlineDoctorRatingApiResponse
import com.iprism.swen.models.onlinedoctorrating.OnlineDoctorRatingRequest
import com.iprism.swen.models.onlinedoctors.OnlineDoctorApiResponse
import com.iprism.swen.models.onlinedoctors.OnlineDoctorRequest
import com.iprism.swen.models.onlinedoctorscoupons.CouponsApiResponse
import com.iprism.swen.models.onlinedoctorscoupons.CouponRequest
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsApiResponse
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsRequest
import com.iprism.swen.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesApiResponse
import com.iprism.swen.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesRequest
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
import com.iprism.swen.models.profile.ProfileApiResponse
import com.iprism.swen.models.profile.ProfileRequest
import com.iprism.swen.models.profileedit.ProfileEditApiResponse
import com.iprism.swen.models.profileedit.ProfileEditRequest
import com.iprism.swen.models.register.RegisterApiResponse
import com.iprism.swen.models.register.RegisterRequest
import com.iprism.swen.models.resendotp.ResendOtpApiResponse
import com.iprism.swen.models.resendotp.ResendOtpRequest
import com.iprism.swen.models.subscription.SubscriptionApiResponse
import com.iprism.swen.models.subscription.SubscriptionRequest
import com.iprism.swen.models.subscriptiondetails.SubscriptionDetailsApiResponse
import com.iprism.swen.models.subscriptiondetails.SubscriptionDetailsRequest
import com.iprism.swen.models.treatmentplaning.TreatmentPlaningApiResponse
import com.iprism.swen.models.treatmentplaning.TreatmentPlaningRequest
import com.iprism.swen.models.userdropdowns.UserDropDownApiResponse
import com.iprism.swen.models.userdropdowns.UserDropDownRequest
import com.iprism.swen.models.wallet.WalletApiResponse
import com.iprism.swen.models.wallet.WalletRequest
import com.iprism.swen.models.wallethistory.WalletHistoryApiResponse
import com.iprism.swen.models.wallethistory.WalletHistoryRequest
import com.iprism.swen.utils.Constants
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
interface MedRayderService {

    @POST(Constants.LOGIN_ENDPOINT)
    suspend fun login(@Body loginRequest: LoginRequest) : LoginApiResponse

    @POST(Constants.RESEND_OTP_ENDPOINT)
    suspend fun resendOtp(@Body resendOtpRequest: ResendOtpRequest) : ResendOtpApiResponse

    @POST(Constants.REGISTER_ENDPOINT)
    suspend fun registerUser(@Body request : RegisterRequest) : RegisterApiResponse

    @POST(Constants.PROFILE_ENDPOINT)
    suspend fun profileDetails(@Body request : ProfileRequest) : ProfileApiResponse

    @POST(Constants.PROFILE_EDIT_ENDPOINT)
    suspend fun profileEdit(@Body request : ProfileEditRequest) : ProfileEditApiResponse

    @POST(Constants.ADD_FAMILY_MEMBER_ENDPOINT)
    suspend fun addFamilyMember(@Body request : AddFamilyMemberRequest) : AddFamilyMemberApiResponse

    @POST(Constants.SUBSCRIPTION_ENDPOINT)
    suspend fun subscription(@Body request : SubscriptionRequest) : SubscriptionApiResponse

    @POST(Constants.SUBSCRIPTION_DETAILS_ENDPOINT)
    suspend fun fetchSubscriptionDetails(@Body request : SubscriptionDetailsRequest) : SubscriptionDetailsApiResponse

    @POST(Constants.GET_SINGLE_SUBSCRIPTION_ENDPOINT)
    suspend fun singleSubscription(@Body request : AddFamilyMemberSubRequest) : SubscriptionApiResponse

    @POST(Constants.HOME_PAGE_ENDPOINT)
    suspend fun fetchHomePage(@Body homePageRequest: HomePageRequest) : HomePageApiResponse

    @POST(Constants.FAMILY_MEMBERS_ENDPOINT)
    suspend fun fetchFamilyMembers(@Body request : FamilyMembersRequest) : FamilyMembersApiResponse

    @POST(Constants.CONTENT_PAGES_ENDPOINT)
    suspend fun fetchContentPage(@Body request : ContentPagesRequest) : ContentPagesApiResponse


    @GET(Constants.USER_DROPDOWN_ENDPOINT)
    suspend fun fetchUserDropDowns() : UserDropDownApiResponse

    @POST(Constants.MED_LOCKER_ENDPOINT)
    suspend fun medLocker(@Body request : MedLockerRequest) : MedLockerApiResponse

    @POST(Constants.TREATMENT_PLANING_ENDPOINT)
    suspend fun treatmentPlanning(@Body request : TreatmentPlaningRequest) : TreatmentPlaningApiResponse

    @POST(Constants.CONTACT_US_ENDPOINT)
    suspend fun contactUs(@Body request : ContactUsRequest) : ContactUsApiResponse

    @POST(Constants.NOTIFICATIONS_ENDPOINT)
    suspend fun fetchNotifications(@Body request : NotificationsRequest) : NotificationsApiResponse


    @POST(Constants.WALLET_ENDPOINT)
    suspend fun wallet(@Body request : WalletRequest) : WalletApiResponse

    @POST(Constants.WALLET_HISTORY_ENDPOINT)
    suspend fun fetchWalletHistory(@Body request : WalletHistoryRequest) : WalletHistoryApiResponse

    @POST(Constants.ADDRESS_ENDPOINT)
    suspend fun addAddress(@Body request : AddAddressRequest) : AddAddressApiResponse

    @POST(Constants.ADDRESS_LIST_ENDPOINT)
    suspend fun fetchAddressList(@Body request : AddressListRequest) : AddressListApiResponse

    @POST(Constants.FILTERS_ENDPOINT)
    suspend fun getFilters(@Body request : FiltersRequest) : FiltersApiResponse


    @POST(Constants.ONLINE_DOCTOR_SPECIALITIES_ENDPOINT)
    suspend fun getOnlineDoctorsSpecialities(@Body onlineDoctorSpecialitiesRequest: OnlineDoctorSpecialitiesRequest) : OnlineDoctorSpecialitiesApiResponse

    @POST(Constants.ONLINE_DOCTORS_ENDPOINT)
    suspend fun getOnlineDoctors(@Body onlineDoctorRequest: OnlineDoctorRequest) : OnlineDoctorApiResponse

    @POST(Constants.ONLINE_DOCTORS_BOOKING_DETAILS_ENDPOINT)
    suspend fun getOnlineDoctorBookingDetails(@Body onlineDoctorBookingDetailsRequest: OnlineDoctorBookingDetailsRequest) : OnlineDoctorBookingDetailsApiResponse

    @POST(Constants.ONLINE_DOCTOR_BOOKING_ENDPOINT)
    suspend fun bookOnlineDoctor(@Body onlineDoctorBookingRequest: OnlineDoctorBookingRequest) : OnlineDoctorBookingApiResponse

    @POST(Constants.ONLINE_DOCTORS_COUPONS_ENDPOINT)
    suspend fun getOnlineDoctorsCoupons(@Body couponRequest: CouponRequest) : CouponsApiResponse

    @POST(Constants.ONLINE_DOCTOR_BOOKING_HISTORY_ENDPOINT)
    suspend fun getOnlineDoctorBookings(@Body onlineDoctorBookingHistoryRequest: OnlineDoctorBookingHistoryRequest) : OnlineDoctorBookingHistoryApiResponse

    @POST(Constants.ONLINE_DOCTOR_BOOKING_HISTORY_COMPLETED_ENDPOINT)
    suspend fun getOnlineDoctorCompletedBookings(@Body onlineDoctorBookingHistoryRequest: OnlineDoctorBookingHistoryRequest) : OnlineDoctorBookingHistoryApiResponse

    @POST(Constants.ONLINE_DOCTOR_SINGLE_BOOKING_COMPLETED_ENDPOINT)
    suspend fun getOnlineDoctorBookingDetails(@Body onlineDoctorSingleBookingDetailsRequest: OnlineDoctorSingleBookingDetailsRequest) : OnlineDoctorSingleBookingDetailsApiResponse

    @POST(Constants.ONLINE_DOCTOR_RATING_ENDPOINT)
    suspend fun insertOnlineDoctorRating(@Body onlineDoctorRatingRequest: OnlineDoctorRatingRequest) : OnlineDoctorRatingApiResponse

    @POST(Constants.HOSPITAL_DOCTOR_RATING_ENDPOINT)
    suspend fun insertHospitalDoctorRating(@Body onlineDoctorRatingRequest: OnlineDoctorRatingRequest) : OnlineDoctorRatingApiResponse

    @POST(Constants.HOSPITALS_ENDPOINT)
    suspend fun getHospitals(@Body hospitalsRequest: HospitalsRequest) : HospitalsApiResponse

    @POST(Constants.MAIN_DATA_ENDPOINT)
    suspend fun getMainData(@Body request: MainDataHospitalsRequest) : MainDataHospitalsApiResponse

    @POST(Constants.HOSPITAL_DETAILS_ENDPOINT)
    suspend fun getHospitalDetails(@Body hospitalDetailsRequest: HospitalDetailsRequest) : HospitalDetailsApiResponse

    @POST(Constants.HOSPITAL_DOCTORS_ENDPOINT)
    suspend fun getHospitalDoctors(@Body hospitalDoctorsRequest: HospitalDoctorsRequest) : HospitalDoctorsApiResponse

    @POST(Constants.HOSPITAL_DOCTOR_TIME_SLOTS_ENDPOINT)
    suspend fun getHospitalDoctorTimeSlots(@Body hospitalDoctorTimeSlotsRequest: HospitalDoctorTimeSlotsRequest) : OnlineDoctorBookingDetailsApiResponse

    @POST(Constants.HOSPITAL_DOCTOR_BOOKING_ENDPOINT)
    suspend fun bookHospitalDoctor(@Body hospitalDoctorBookingRequest: HospitalDoctorBookingRequest) : HospitalDoctorBookingApiResponse

    @POST(Constants.HOSPITAL_DOCTOR_COUPONS_ENDPOINT)
    suspend fun getHospitalDoctorCoupons(@Body couponRequest: CouponRequest) : CouponsApiResponse

    @POST(Constants.HOSPITAL_DOCTOR_BOOKING_HISTORY_ENDPOINT)
    suspend fun getHospitalDoctorBookingsHistory(@Body onlineDoctorBookingHistoryRequest: OnlineDoctorBookingHistoryRequest) : OnlineDoctorBookingHistoryApiResponse

    @POST(Constants.HOSPITAL_DOCTOR_BOOKING_HISTORY_COMPLETED_ENDPOINT)
    suspend fun getHospitalDoctorCompletedBookingHistory(@Body onlineDoctorBookingHistoryRequest: OnlineDoctorBookingHistoryRequest) : OnlineDoctorBookingHistoryApiResponse

    @POST(Constants.HOSPITAL_DOCTOR_BOOKING_SINGLE_DETAILS_ENDPOINT)
    suspend fun getHospitalDoctorSingleBookingDetails(@Body onlineDoctorSingleBookingDetailsRequest: OnlineDoctorSingleBookingDetailsRequest) : OnlineDoctorSingleBookingDetailsApiResponse

    @POST(Constants.HOSPITAL_ADMIT_BOOKING_ENDPOINT)
    suspend fun bookHospitalAdmit(@Body request: AdmitBookingRequest) : AdmitBookingApiResponse

    @POST(Constants.HOSPITAL_AMBULANCE_BOOKING_ENDPOINT)
    suspend fun bookHospitalAmbulance(@Body request: AmbulanceBookingRequest) : AmbulanceBookingApiResponse

    @POST(Constants.HOSPITAL_AMBULANCE_TRACKING_ENDPOINT)
    suspend fun trackAmbulance(@Body request: AmbulanceTrackingRequest) : AmbulanceTrackingApiResponse

    @POST(Constants.HOSPITAL_ADMIT_ONGOING_BOOKINGS_ENDPOINT)
    suspend fun fetchAdmitOnGoingBookings(@Body request: HospitalAdmissionOnGoingRequest) : HospitalAdmissionOnGoingApiResponse

    @POST(Constants.HOSPITAL_ADMIT_COMPLETED_BOOKINGS_ENDPOINT)
    suspend fun fetchAdmitCompletedBookings(@Body request: HospitalAdmissionOnGoingRequest) : HospitalAdmissionOnGoingApiResponse

    @POST(Constants.HOSPITAL_ADMIT_BOOKING_DETAILS_ENDPOINT)
    suspend fun fetchAdmitBookingDetails(@Body request: AdmitBookingDetailsRequest) : AdmitBookingDetailsAPiResponse

    @POST(Constants.HOSPITAL_AMBULANCE_ONGOING_BOOKINGS_ENDPOINT)
    suspend fun fetchAmbulanceOngoingBookings(@Body request: LabTestBookingsRequest) : HospitalAmbulanceBookingApiResponse

    @POST(Constants.HOSPITAL_AMBULANCE_COMPLETED_BOOKINGS_ENDPOINT)
    suspend fun fetchAmbulanceCompletedBookings(@Body request: LabTestBookingsRequest) : HospitalAmbulanceBookingApiResponse



    @POST(Constants.HOSPITAL_MED_CATEGORIES_ENDPOINT)
    suspend fun fetchHospitalMedCategories(@Body request: HospitalMedicinesRequest) : HospitalMedicinesApiResponse

    @POST(Constants.HOSPITAL_MED_PRODUCTS_ENDPOINT)
    suspend fun fetchHospitalMedProducts(@Body request: HospitalMedicineProductsRequest) : HospitalMedicineProductsApiResponse

    @POST(Constants.HOSPITAL_MED_PRODUCT_ADD_TO_CART_ENDPOINT)
    suspend fun addToCartHospitalMedicineProduct(@Body request: HospitalMedineProductsAddToCartRequest) : HospitalMedineProductsAddToCartApiResponse

    @POST(Constants.HOSPITAL_MED_PRODUCT_CART_ENDPOINT)
    suspend fun getHospitalProductCart(@Body request : HospitalMedicineProductCartRequest) : PharmacyProductCartApiResponse

    @POST(Constants.HOSPITAL_MED_PRODUCT_BOOKING_ENDPOINT)
    suspend fun bookHospitalMedicine(@Body request : HospitalMedicineBookingRequest) : HospitalMedicineBookingApiResponse

    @POST(Constants.HOSPITAL_MED_COUPONS_ENDPOINT)
    suspend fun getHospitalMedicineCoupons(@Body couponRequest: CouponRequest) : CouponsApiResponse

    @POST(Constants.HOSPITAL_MED_ONGOING_BOOKINGS_ENDPOINT)
    suspend fun fetchHospitalMedOngoingBookings(@Body request : HospitalMedicineOngoingBookingRequest) : HospitalMedicineOngoingBookingApiResponse

    @POST(Constants.HOSPITAL_MED_COMPLETED_BOOKINGS_ENDPOINT)
    suspend fun fetchHospitalMedCompletedBookings(@Body request : HospitalMedicineOngoingBookingRequest) : HospitalMedicineOngoingBookingApiResponse

    @POST(Constants.HOSPITAL_MED_BOOKINGS_DETAILS_ENDPOINT)
    suspend fun fetchHospitalMedBookingDetails(@Body request : HospitalMedicineBookingDetailsRequest) : HospitalMedicineBookingDetailsApiResponse

    @POST(Constants.HOSPITAL_MED_PRESCRIPTION_BOOKING_ENDPOINT)
    suspend fun bookHospitalMedPrescription(@Body request : HospitalPharmacyPrescriptionBookingRequest) : HospitalPharmacyPrescriptionBookingApiResponse


    @POST(Constants.HOSPITAL_DIAGNOSTIC_TESTS_ENDPOINT)
    suspend fun fetchHospitalDiagnosticTests(@Body request: HospitalDiagnosticTestsRequest) : DiagnosticTestsApiResponse

    @POST(Constants.HOSPITAL_DIAGNOSTIC_TIME_ENDPOINT)
    suspend fun fetchHospitalDiagnosticTimeSlots(@Body request: HospitalDiagnosticTimeRequest) : DiagnosticSlotsApiResponse

    @POST(Constants.HOSPITAL_DIAGNOSTIC_BOOKING_ENDPOINT)
    suspend fun bookHospitalDiagnostic(@Body request: HospitalDiagnosticBookingRequest) : HospitalDiagnosticBookingApiResponse

    @POST(Constants.HOSPITAL_DIAGNOSTIC_COUPONS_ENDPOINT)
    suspend fun fetchHospitalDiagnosticCoupons(@Body couponRequest: CouponRequest) : CouponsApiResponse

    @POST(Constants.HOSPITAL_DIAGNOSTIC_ONGOING_BOOKINGS_ENDPOINT)
    suspend fun fetchHospitalDiagnosticTestsOngoingBookings(@Body request: LabTestBookingsRequest) : DiagnosticTestsBookingsApiResponse

    @POST(Constants.HOSPITAL_DIAGNOSTIC_COMPLETED_BOOKINGS_ENDPOINT)
    suspend fun fetchHospitalDiagnosticTestsCompletedBookings(@Body request: LabTestBookingsRequest) : DiagnosticTestsBookingsApiResponse

    @POST(Constants.HOSPITAL_DIAGNOSTIC_BOOKING_DETAILS_ENDPOINT)
    suspend fun fetchHospitalDiagnosticTestBookingDetails(@Body request: HospitalDiagnosticTestBookingDetailsRequest) : HospitalDiagnosticTestBookingDetailsApiResponse

    @POST(Constants.HOSPITAL_DIAGNOSTIC_PRESCRIPTION_BOOKING_ENDPOINT)
    suspend fun bookHospitalDiagnosticPrescriptionBooking(@Body request: HospitalDiagnosticPrescriptionBookingRequest) : HospitalDiagnosticPrescriptionBookingApiResponse


    @POST(Constants.DIAGNOSTIC_CENTERS_ENDPOINT)
    suspend fun getDiagnosticCenters(@Body request: DiagnosticCentersRequest) : DiagnosticCentersApiResponse

    @POST(Constants.DIAGNOSTIC_TESTS_ENDPOINT)
    suspend fun fetchDiagnosticTests(@Body request: DiagnosticTestsRequest) : DiagnosticTestsApiResponse

    @POST(Constants.DIAGNOSTIC_BOOKING_DETAILS_ENDPOINT)
    suspend fun fetchDiagnosticBookingDetails(@Body request: DiagnosticSlotsRequest) : DiagnosticSlotsApiResponse

    @POST(Constants.DIAGNOSTIC_COUPONS_ENDPOINT)
    suspend fun fetchDiagnosticCoupons(@Body couponRequest: CouponRequest) : CouponsApiResponse

    @POST(Constants.DIAGNOSTIC_PRESCRIPTION_BOOKING_ENDPOINT)
    suspend fun bookPrescriptionDiagnosticTest(@Body request: DiagnosticPrescriptionBookingRequest) : LabTestPrescriptionBookingApiResponse

    @POST(Constants.DIAGNOSTIC_TEST_ONGOING_BOOKINGS_ENDPOINT)
    suspend fun fetchDiagnosticTestsOngoingBookings(@Body request: LabTestBookingsRequest) : DiagnosticTestsBookingsApiResponse

    @POST(Constants.DIAGNOSTIC_TEST_COMPLETED_BOOKINGS_ENDPOINT)
    suspend fun fetchDiagnosticTestsCompletedBookings(@Body request: LabTestBookingsRequest) : DiagnosticTestsBookingsApiResponse

    @POST(Constants.DIAGNOSTIC_TEST_BOOKING_DETAILS_ENDPOINT)
    suspend fun fetchDiagnosticTestBookingDetails(@Body request: DiagnosticTestBookingDetailsRequest) : DiagnosticTestBookingDetailsApiResponse

    @POST(Constants.DIAGNOSTIC_BOOKING_ENDPOINT)
    suspend fun bookDiagnostic(@Body diagnosticBookingRequest: DiagnosticBookingRequest) : DiagnosticBookingApiResponse


    @POST(Constants.PHARMACIES_ENDPOINT)
    suspend fun fetchPharmacies(@Body request: PharmaciesRequest) : PharmaciesApiResponse

    @POST(Constants.PHARMACY_DETAILS_ENDPOINT)
    suspend fun fetchPharmacyDetails(@Body request : PharmacyDetailsRequest) : PharmacyDetailsApiResponse

    @POST(Constants.PHARMACY_PRODUCTS_ENDPOINT)
    suspend fun fetchPharmacyProducts(@Body request : PharmacyProductsRequest) : PharmacyProductsApiResponse

    @POST(Constants.PHARMACY_PRODUCT_ADD_TO_CART_ENDPOINT)
    suspend fun addToCartPharmacyProduct(@Body request : PharmacyProductAddToCartRequest) : PharmacyProductAddToCartApiResponse

    @POST(Constants.PHARMACY_PRODUCT_CART_ENDPOINT)
    suspend fun getPharmacyProductCart(@Body request : PharmacyProductCartRequest) : PharmacyProductCartApiResponse

    @POST(Constants.PHARMACY_COUPONS_ENDPOINT)
    suspend fun fetchPharmacyCoupons(@Body couponRequest: CouponRequest) : CouponsApiResponse

    @POST(Constants.BOOK_PHARMACY_PRODUCT_ENDPOINT)
    suspend fun bookPharmacyProduct(@Body   bookPharmacyProductRequest: BookPharmacyProductRequest) : BookPharmacyProductApiResponse

    @POST(Constants.PHARMACY_ONGOING_BOOKINGS)
    suspend fun fetchPharmacyOngoingBookings(@Body request: PharmacyOnGoingBookingsRequest) : PharmacyOnGoingBookingsApiResponse

    @POST(Constants.PHARMACY_COMPLETED_BOOKINGS)
    suspend fun fetchPharmacyCompletedBookings(@Body request: PharmacyOnGoingBookingsRequest) : PharmacyOnGoingBookingsApiResponse

    @POST(Constants.PHARMACY_BOOKING_DETAILS_ENDPOINT)
    suspend fun fetchPharmacyBookingDetails(@Body request : HospitalMedicineBookingDetailsRequest) : HospitalMedicineBookingDetailsApiResponse

    @POST(Constants.PHARMACY_PRESCRIPTION_BOOKING_ENDPOINT)
    suspend fun bookPharmacyPrescriptionOrder(@Body request : PharmacyPrescriptionBookingRequest) : PharmacyPrescriptionBookingApiResponse


    @POST(Constants.LAB_CENTERS_ENDPOINT)
    suspend fun fetchLabCenters(@Body labCentersRequest: LabCentersRequest) : LabCentersApiResponse

    @POST(Constants.LAB_TESTS_ENDPOINT)
    suspend fun fetchLabTests(@Body labTestsRequest: LabTestsRequest) : LabTestsApiResponse

    @POST(Constants.LAB_BOOKING_DETAILS_ENDPOINT)
    suspend fun fetchLabBookingDetails(@Body labTestSlotsRequest: LabTestSlotsRequest) : LabTestSlotsApiResponse

    @POST(Constants.LAB_COUPONS_ENDPOINT)
    suspend fun fetchLabCoupons(@Body couponRequest: CouponRequest) : CouponsApiResponse

    @POST(Constants.LAB_BOOKING_ENDPOINT)
    suspend fun bookLab(@Body labBookingRequest: LabBookingRequest) : LabBookingApiResponse

    @POST(Constants.LAB_PRESCRIPTION_BOOKING_ENDPOINT)
    suspend fun bookPrescriptionLabTest(@Body request: LabTestPrescriptionBookingRequest) : LabTestPrescriptionBookingApiResponse

    @POST(Constants.LAB_TEST_ONGOING_BOOKINGS_ENDPOINT)
    suspend fun fetchLabTestsOngoingBookings(@Body request: LabTestBookingsRequest) : LabTestBookingsApiResponse

    @POST(Constants.LAB_TEST_COMPLETED_BOOKINGS_ENDPOINT)
    suspend fun fetchLabTestsCompletedBookings(@Body request: LabTestBookingsRequest) : LabTestBookingsApiResponse

    @POST(Constants.LAB_TEST_BOOKING_DETAILS_ENDPOINT)
    suspend fun fetchLabTestBookingDetails(@Body request: LabTestBookingDetailsRequest) : LabTestBookingDetailsApiResponse

    @POST(Constants.CREATE_CASH_FREE_ENDPOINT)
    suspend fun createCashFreeOrder(@Body request: CreateCashFreeOrderRequest) : CreateCashFreeOrderApiResponse

    @POST(Constants.CHECK_PAYMENT_ENDPOINT)
    suspend fun checkPayment(@Body request: CheckPaymentRequest) : CheckPaymentApiResponse

    @POST(Constants.E_CARD_ENDPOINT)
    suspend fun fetchECard(@Body request: ECardRequest) : ECardApiResponse
}