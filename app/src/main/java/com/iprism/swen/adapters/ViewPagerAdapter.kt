import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.swen.fragments.DiagnosticFragment
import com.iprism.swen.fragments.HomeFragment
import com.iprism.swen.fragments.HospitalFragment
import com.iprism.swen.fragments.LabTestsFragment
import com.iprism.swen.fragments.PharmaciesFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 5 // Number of fragments (Home, Medicines, etc.)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> HospitalFragment()
            2 -> PharmaciesFragment()
            3 -> LabTestsFragment()
            4 -> DiagnosticFragment()
            else -> HomeFragment()
        }
    }
}
