package com.iprism.medrayder.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.medrayder.activities.HospitalDetailsActivity
import com.iprism.medrayder.adapters.HospitalDoctorSpecialitiesAdapter
import com.iprism.medrayder.adapters.OnlineDoctorSpecialitiesAdapter
import com.iprism.medrayder.databinding.FragmentHospitalBookingBinding
import com.iprism.medrayder.interfaces.OnSpecialityItemClickListener
import com.iprism.medrayder.models.hospitaldetails.SpecialitiesItem

class HospitalBookingFragment(private var specialities: List<SpecialitiesItem>) : Fragment() {

    private lateinit var binding: FragmentHospitalBookingBinding
    var specialityId = ""
    var specialityName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHospitalBookingBinding.inflate(layoutInflater)
        setUpMedicineCategories(specialities)
        handleDiagnosticLL()
        return binding.root
    }

    private fun setUpMedicineCategories(specialities : List<SpecialitiesItem>) {
        val hospitalDoctorSpecialitiesAdapter = HospitalDoctorSpecialitiesAdapter(specialities)
        binding.specilitiesRv.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.specilitiesRv.adapter = hospitalDoctorSpecialitiesAdapter
        hospitalDoctorSpecialitiesAdapter.setOnArtistActionListener(object :
            OnSpecialityItemClickListener {
            override fun onItemClicked(id: String, name : String) {
                specialityId = id
                specialityName = name
            }
        })
    }

    private fun handleDiagnosticLL() {
        (activity as? HospitalDetailsActivity)?.setView("hospitalBooking")
    }
}