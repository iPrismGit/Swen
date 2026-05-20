import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.medrayder.fragments.DiagnosticFragment
import com.iprism.medrayder.fragments.HomeFragment
import com.iprism.medrayder.fragments.HospitalDiagnosticTestsCompletedBookingsFragment
import com.iprism.medrayder.fragments.HospitalDiagnosticTestsOnGoingBookingsFragment
import com.iprism.medrayder.fragments.HospitalFragment
import com.iprism.medrayder.fragments.HospitalMedicineCompletedFragment
import com.iprism.medrayder.fragments.HospitalMedicineOnGoingFragment
import com.iprism.medrayder.fragments.LabTestsFragment
import com.iprism.medrayder.fragments.MedicineFragment
import com.iprism.medrayder.fragments.OnlineDoctorCompletedBookingsFragment
import com.iprism.medrayder.fragments.OnlineDoctorOnGoingBookingsFragment

class HospitalDiagnosticBookingsViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2 // Number of fragments (Home, Medicines, etc.)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HospitalDiagnosticTestsOnGoingBookingsFragment()
            1 -> HospitalDiagnosticTestsCompletedBookingsFragment()
            else -> HospitalDiagnosticTestsOnGoingBookingsFragment()
        }
    }
}
