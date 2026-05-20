import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.swen.fragments.HospitalDiagnosticTestsCompletedBookingsFragment
import com.iprism.swen.fragments.HospitalDiagnosticTestsOnGoingBookingsFragment

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
