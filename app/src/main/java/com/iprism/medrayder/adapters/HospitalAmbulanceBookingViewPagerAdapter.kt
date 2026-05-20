import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.medrayder.fragments.AmbulanceCompletedBookingsFragment
import com.iprism.medrayder.fragments.AmbulanceOnGoingBookingsFragment
import com.iprism.medrayder.fragments.DiagnosticFragment
import com.iprism.medrayder.fragments.HomeFragment
import com.iprism.medrayder.fragments.HospitalAdmissionCompletedFragment
import com.iprism.medrayder.fragments.HospitalAdmissionOnGoingFragment
import com.iprism.medrayder.fragments.HospitalFragment
import com.iprism.medrayder.fragments.HospitalMedicineCompletedFragment
import com.iprism.medrayder.fragments.HospitalMedicineOnGoingFragment
import com.iprism.medrayder.fragments.LabTestsFragment
import com.iprism.medrayder.fragments.MedicineFragment

class HospitalAmbulanceBookingViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2 // Number of fragments (Home, Medicines, etc.)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AmbulanceOnGoingBookingsFragment()
            1 -> AmbulanceCompletedBookingsFragment()
            else -> AmbulanceOnGoingBookingsFragment()
        }
    }
}
