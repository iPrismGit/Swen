import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.swen.fragments.HospitalDoctorCompletedBookingsFragment
import com.iprism.swen.fragments.HospitalDoctorOnGoingBookingsFragment

class HospitalDoctorBookingsViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2 // Number of fragments (Home, Medicines, etc.)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HospitalDoctorOnGoingBookingsFragment()
            1 -> HospitalDoctorCompletedBookingsFragment()
            else -> HospitalDoctorOnGoingBookingsFragment()
        }
    }
}
