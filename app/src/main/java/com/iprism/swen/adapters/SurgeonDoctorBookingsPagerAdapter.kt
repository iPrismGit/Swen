package com.iprism.swen.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.swen.fragments.SurgeonDoctorCompletedBookingsFragment
import com.iprism.swen.fragments.SurgeonDoctorOnGoingBookingsFragment

class SurgeonDoctorBookingsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SurgeonDoctorOnGoingBookingsFragment()
            1 -> SurgeonDoctorCompletedBookingsFragment()
            else -> SurgeonDoctorOnGoingBookingsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}