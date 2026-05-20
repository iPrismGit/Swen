import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.swen.fragments.HomeFragment
import com.iprism.swen.fragments.PharmaciesFragment
import com.iprism.swen.fragments.WellnessProductsFragment

class MedicinePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WellnessProductsFragment()
            1 -> PharmaciesFragment()
            else -> HomeFragment()
        }
    }
}
