import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.swen.fragments.LabTestsCompletedBookingsFragment
import com.iprism.swen.fragments.LabTestsOnGoingBookingsFragment

class LabTestsBookingsViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2 // Number of fragments (Home, Medicines, etc.)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LabTestsOnGoingBookingsFragment()
            1 -> LabTestsCompletedBookingsFragment()
            else -> LabTestsOnGoingBookingsFragment()
        }
    }
}
