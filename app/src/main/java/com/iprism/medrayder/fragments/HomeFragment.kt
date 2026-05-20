package com.iprism.medrayder.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.R
import com.iprism.medrayder.activities.AirAmbulanceActivity
import com.iprism.medrayder.activities.ContactUsActivity
import com.iprism.medrayder.activities.HomeActivity
import com.iprism.medrayder.activities.HospitalsActivity
import com.iprism.medrayder.activities.LoginActivity
import com.iprism.medrayder.activities.OnlineDoctorsActivity
import com.iprism.medrayder.activities.SubscriptionActivity
import com.iprism.medrayder.activities.TreatmentPlanningActivity
import com.iprism.medrayder.adapters.HospitalCategoriesAdapter
import com.iprism.medrayder.adapters.LabTestCategoriesAdapter
import com.iprism.medrayder.adapters.MedCategoriesAdapter
import com.iprism.medrayder.adapters.MedicineCategoriesAdapter
import com.iprism.medrayder.adapters.BannersAdapter
import com.iprism.medrayder.databinding.FragmentHomeBinding
import com.iprism.medrayder.interfaces.OnHospitalCatItemClickListener
import com.iprism.medrayder.interfaces.OnItemClickListener
import com.iprism.medrayder.interfaces.OnLabCatItemClickListener
import com.iprism.medrayder.interfaces.OnWellnessCatItemClickListener
import com.iprism.medrayder.models.homepage.BannersItem
import com.iprism.medrayder.models.homepage.CategoriesItem
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.homepage.PharmacyCategoriesItem
import com.iprism.medrayder.models.homepage.SubCategoriesItem
import com.iprism.medrayder.models.notifications.NotificationsRequest
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.videocalling.utilities.Utility
import com.iprism.medrayder.viewmodels.HomeViewModel
import com.iprism.medrayder.viewmodels.LocationViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory
import com.onesignal.OneSignal
import com.smarteist.autoimageslider.SliderAnimations

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var playerId: String? = ""
    var lat = ""
    var lon = ""
    var mobile = ""
    private var isSubscribe: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        OneSignal.setAppId("cebaa375-de95-4fb8-9403-71089f304ffe")
        Log.d("OneSignal", "Device is subscribed: " + OneSignal.getDeviceState()!!.isSubscribed)
        val deviceState = OneSignal.getDeviceState()
        if (deviceState != null) {
            playerId = deviceState.userId ?: ""
            Log.d("OneSignal", "Player ID1: $playerId")
        }
        askNotificationPermission()
        val viewModel = ViewModelProvider(requireActivity())[LocationViewModel::class.java]
        viewModel.location.observe(viewLifecycleOwner) { (lat, lon) ->
            Log.d("HomeFragment", "Received lat: $lat, lon: $lon")
            this.lat = lat.toString()
            this.lon = lon.toString()
        }
        handlePharmacyLL()
        handleLabTestsLl()
        handleDiagnosticLL()
        handleOnlineDoctors()
        handleContactUsLL()
        handleAmbulanceLL()
        handleAdmissionLL()
        handleTreatmentLL()
        handleAirAmbulanceLl()
        handleWhatsapp()
        initViewModel()
        observeResponse()
        observeNotificationCountResponse()
        getHomePage()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchNotifications()
    }

    private fun setTopBanners(banners: List<BannersItem>) {
        val bannersAdapter = BannersAdapter(banners)
        binding.imageSlider.setSliderAdapter(bannersAdapter)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider.startAutoCycle()
        binding.imageSlider1.setSliderAdapter(bannersAdapter)
        binding.imageSlider1.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
    }

    private fun setBottomBanners(banners: List<BannersItem>) {
        val bannersAdapter = BannersAdapter(banners)
        binding.imageSlider.setSliderAdapter(bannersAdapter)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider.startAutoCycle()
        binding.imageSlider1.setSliderAdapter(bannersAdapter)
        binding.imageSlider1.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
    }

    private fun setUpMedCategories(medCategories: List<CategoriesItem>) {
        val medCategoriesAdapter = MedCategoriesAdapter(medCategories)
        binding.medCategoriesRv.layoutManager = GridLayoutManager(context, 4)
        binding.medCategoriesRv.adapter = medCategoriesAdapter
        medCategoriesAdapter.setOnArtistActionListener(object : OnItemClickListener {
            override fun onItemClicked(item: CategoriesItem) {
                if (!isSubscribe) {
                    val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                    intent.putExtra("tag", "subscribe")
                    startActivity(intent)
                    Toast.makeText(
                        requireContext(),
                        "Please subscribe to use this feature",
                        Toast.LENGTH_SHORT
                    ).show()
                    false   // prevent navigation
                } else {
                    val intent = Intent(requireContext(), HospitalsActivity::class.java)
                    intent.putExtra("catId", item.id)
                    intent.putExtra("catName", item.name)
                    intent.putExtra("lat", lat)
                    intent.putExtra("lon", lon)
                    startActivity(intent)
                }
            }
        })
    }

    private fun setUpHospitalCategories(hospitalCategories: List<SubCategoriesItem>) {
        val hospitalCategoriesAdapter = HospitalCategoriesAdapter(hospitalCategories)
        binding.hospitalCategoriesRv.layoutManager = GridLayoutManager(context, 4)
        binding.hospitalCategoriesRv.adapter = hospitalCategoriesAdapter
        hospitalCategoriesAdapter.setOnArtistActionListener(object :
            OnHospitalCatItemClickListener {
            override fun onItemClicked(item: SubCategoriesItem) {
                if (!isSubscribe) {
                    val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                    intent.putExtra("tag", "subscribe")
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Please subscribe to use this feature", Toast.LENGTH_SHORT).show()
                    false   // prevent navigation
                } else {
                    val intent = Intent(requireContext(), HospitalsActivity::class.java)
                    intent.putExtra("catId", item.id)
                    intent.putExtra("catName", item.name)
                    intent.putExtra("lat", lat)
                    intent.putExtra("lon", lon)
                    startActivity(intent)
                }
            }
        })
    }

    private fun setUpLabTestCategories(labCats: List<CategoriesItem>) {
        val labTestCategoriesAdapter = LabTestCategoriesAdapter(labCats)
        binding.labTestCategoriesRv.layoutManager =
            GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
        binding.labTestCategoriesRv.adapter = labTestCategoriesAdapter
        labTestCategoriesAdapter.setOnArtistActionListener(object : OnLabCatItemClickListener {

            override fun onItemClicked(item: CategoriesItem) {
                if (!isSubscribe) {
                    val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                    intent.putExtra("tag", "subscribe")
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Please subscribe to use this feature", Toast.LENGTH_SHORT).show()
                    false   // prevent navigation
                } else {
                    (activity as? HomeActivity)?.changeFragment(3)
                }
            }
        })
    }

    private fun setUpMedicineCategories(pharmacyCategories: List<PharmacyCategoriesItem>) {
        val medicineCategoriesAdapter = MedicineCategoriesAdapter(pharmacyCategories)
        binding.medicineCategoriesRv.layoutManager = GridLayoutManager(context, 4)
        binding.medicineCategoriesRv.adapter = medicineCategoriesAdapter
        medicineCategoriesAdapter.setOnArtistActionListener(object :
            OnWellnessCatItemClickListener {
            override fun onItemClicked(item: PharmacyCategoriesItem) {
                if (!isSubscribe) {
                    val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                    intent.putExtra("tag", "subscribe")
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Please subscribe to use this feature", Toast.LENGTH_SHORT).show()
                    false   // prevent navigation
                } else {
                    (activity as? HomeActivity)?.changeFragment(2)
                }
            }
        })
    }

    private fun initViewModel() {
        val repository = CommonRepository()
        val factory = ViewModelFactory { HomeViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.startShimmer()
                }

                is UiState.Success -> {
                    binding.scrollView.visibility = View.VISIBLE
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    setUpMedCategories(result.data.response.categories)
                    setUpHospitalCategories(result.data.response.subCategories)
                    setUpMedicineCategories(result.data.response.pharmacyCategories)
                    setTopBanners(result.data.response.banners)
                    setUpLabTestCategories(result.data.response.labTestBanners)
                    setBottomBanners(result.data.response.banners)
                    mobile = result.data.response.mobile
                    if (result.data.response.healthCardStatus == 1) {
                        (activity as? HomeActivity)?.setSubscribe(true)
                    } else {
                        (activity as? HomeActivity)?.setSubscribe(false)
                    }
                    isSubscribe = (activity as? HomeActivity)?.isSubscribe() as Boolean
                }

                is UiState.Error -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    if (result.message.equals("token not verified", true)) {
                        val user = User(requireContext())
                        user.logoutUser()
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    requireContext().showToast(result.message)
                }
            }
        }
    }

    private fun getHomePage() {
        val userDetails = requireContext().getUserDetails()
        val request = HomePageRequest(
            userDetails[User.ID]!!.toInt(),
            userDetails[User.LANG].toString(),
            userDetails[User.AUTH_TOKEN].toString(),
            playerId!!
        )
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchHomePage(req)
        }
        Log.d("requestLoading", request.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeNotificationCountResponse() {
        viewModel.notificationCountResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    Log.d("notificationCount", result.data.response.count.toString())
                    (activity as? HomeActivity)?.fetchNotificationCount(result.data.response.count)
                }

                is UiState.Error -> {
                }
            }
        }
    }

    private fun fetchNotifications() {
        val userDetails = requireContext().getUserDetails()
        val addressListRequest = NotificationsRequest(
            userDetails[User.ID]!!.toInt(),
            "count",
            userDetails[User.LANG].toString(),
            1,
            userDetails[User.AUTH_TOKEN].toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), addressListRequest) { req ->
            viewModel.fetchNotifications(req)
        }
        Log.d("requestLoading", addressListRequest.toString())
    }

    private fun handleOnlineDoctors() {
        binding.onlineDoctorLl.setOnClickListener(View.OnClickListener {
            if (!isSubscribe) {
                val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                intent.putExtra("tag", "subscribe")
                startActivity(intent)
                Toast.makeText(
                    requireContext(),
                    "Please subscribe to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
                false   // prevent navigation
            } else {
                startActivity(Intent(requireContext(), OnlineDoctorsActivity::class.java))
            }
        })
    }

    private fun handleWhatsapp() {
        binding.whatsappLl.setOnClickListener(View.OnClickListener {
            openWhatsAppWithoutMessage(requireContext())
        })
    }

    private fun handleTreatmentLL() {
        binding.treatmentLl.setOnClickListener(View.OnClickListener {
            if (!isSubscribe) {
                val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                intent.putExtra("tag", "subscribe")
                startActivity(intent)
                Toast.makeText(
                    requireContext(),
                    "Please subscribe to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
                false   // prevent navigation
            } else {
                startActivity(Intent(requireContext(), TreatmentPlanningActivity::class.java))
            }
        })
    }

    private fun handleContactUsLL() {
        binding.contactUsLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), ContactUsActivity::class.java))
        })
    }

    private fun handleAirAmbulanceLl() {
        binding.airAmbulanceLl.setOnClickListener(View.OnClickListener {
            if (!isSubscribe) {
                val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                intent.putExtra("tag", "subscribe")
                startActivity(intent)
                Toast.makeText(
                    requireContext(),
                    "Please subscribe to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
                false   // prevent navigation
            } else {
                startActivity(Intent(requireContext(), AirAmbulanceActivity::class.java))
            }
        })
    }

    private fun handleAmbulanceLL() {
        binding.ambulanceLl.setOnClickListener(View.OnClickListener {
            if (!isSubscribe) {
                val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                intent.putExtra("tag", "subscribe")
                startActivity(intent)
                Toast.makeText(
                    requireContext(),
                    "Please subscribe to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
                false   // prevent navigation
            } else {
                (activity as? HomeActivity)?.changeFragment(1)
            }
        })
    }

    private fun handleAdmissionLL() {
        binding.admissionLl.setOnClickListener(View.OnClickListener {
            if (!isSubscribe) {
                val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                intent.putExtra("tag", "subscribe")
                startActivity(intent)
                Toast.makeText(
                    requireContext(),
                    "Please subscribe to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
                false   // prevent navigation
            } else {
                (activity as? HomeActivity)?.changeFragment(1)
            }
        })
    }

    private fun handlePharmacyLL() {
        binding.pharmacyLl.setOnClickListener(View.OnClickListener {
            if (!isSubscribe) {
                val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                intent.putExtra("tag", "subscribe")
                startActivity(intent)
                Toast.makeText(
                    requireContext(),
                    "Please subscribe to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
                false   // prevent navigation
            } else {
                (activity as? HomeActivity)?.changeFragment(2)
            }
        })
    }

    private fun handleLabTestsLl() {
        binding.labTestsLl.setOnClickListener(View.OnClickListener {
            if (!isSubscribe) {
                val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                intent.putExtra("tag", "subscribe")
                startActivity(intent)
                Toast.makeText(
                    requireContext(),
                    "Please subscribe to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
                false   // prevent navigation
            } else {
                (activity as? HomeActivity)?.changeFragment(3)
            }
        })
    }

    private fun handleDiagnosticLL() {
        binding.diagnosticLl.setOnClickListener(View.OnClickListener {
            if (!isSubscribe) {
                val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                intent.putExtra("tag", "subscribe")
                startActivity(intent)
                Toast.makeText(
                    requireContext(),
                    "Please subscribe to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
                false   // prevent navigation
            } else {
                (activity as? HomeActivity)?.changeFragment(4)
            }
        })
    }

    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            //requireContext().showToast("Notifications Permission Granted")
        } else {
            requireContext().showToast(getString(R.string.notifications_permission_denied))
            goToSettings()
        }
    }

    private fun goToSettings() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.notification_permission_required))
            .setMessage(getString(R.string.please_enable_notification))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.okay)) { _, _ ->
                Utility.goToPermissionPage(requireActivity())
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    fun openWhatsAppWithoutMessage(context: Context) {
        try {
            val url = "https://api.whatsapp.com/send?phone=$mobile"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}