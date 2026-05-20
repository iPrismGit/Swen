package com.iprism.swen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.swen.databinding.FragmentLabTestUpcomingBookingBinding

class LabTestUpcomingBookingFragment : Fragment() {

    private lateinit var binding : FragmentLabTestUpcomingBookingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLabTestUpcomingBookingBinding.inflate(layoutInflater)
        return binding.root
    }
}