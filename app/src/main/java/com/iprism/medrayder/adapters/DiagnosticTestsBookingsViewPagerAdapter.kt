import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.medrayder.fragments.DiagnosticFragment
import com.iprism.medrayder.fragments.DiagnosticTestsCompletedBookingsFragment
import com.iprism.medrayder.fragments.DiagnosticTestsOnGoingBookingsFragment
import com.iprism.medrayder.fragments.HomeFragment
import com.iprism.medrayder.fragments.HospitalFragment
import com.iprism.medrayder.fragments.HospitalMedicineCompletedFragment
import com.iprism.medrayder.fragments.HospitalMedicineOnGoingFragment
import com.iprism.medrayder.fragments.LabTestsCompletedBookingsFragment
import com.iprism.medrayder.fragments.LabTestsFragment
import com.iprism.medrayder.fragments.LabTestsOnGoingBookingsFragment
import com.iprism.medrayder.fragments.MedicineFragment
import com.iprism.medrayder.fragments.OnlineDoctorCompletedBookingsFragment
import com.iprism.medrayder.fragments.OnlineDoctorOnGoingBookingsFragment

class DiagnosticTestsBookingsViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2 // Number of fragments (Home, Medicines, etc.)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DiagnosticTestsOnGoingBookingsFragment()
            1 -> DiagnosticTestsCompletedBookingsFragment()
            else -> DiagnosticTestsOnGoingBookingsFragment()
        }
    }
}
