package cn.guankejian.test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import cn.guankejian.test.R
import cn.guankejian.test.databinding.FragmentFirstBinding
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentFirst @Inject constructor(
) : Fragment(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: FragmentFirstBinding

    private lateinit var tabFragmentsCreators: Map<Int, () -> Fragment>

    lateinit var one: FragmentDetail
    lateinit var two: FragmentTest
    lateinit var three: FragmentTest



    val controller: NavController by lazy { findNavController() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
    }



    fun initVar() {
       one = FragmentDetail()
       two = FragmentTest("two")
       three = FragmentTest("three")

        tabFragmentsCreators = mapOf(
            0 to { one },
            1 to { two },
            2 to { three },
        )

        binding.vp.adapter =
            object : FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {
                override fun createFragment(position: Int): Fragment {
                    return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
                }

                override fun getItemCount(): Int {
                    return tabFragmentsCreators.size
                }
            }




        binding.bottomNavView.setOnItemSelectedListener(this)

        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavView.menu.getItem(position).isChecked = true
            }
        })

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.one -> {
                binding.vp.setCurrentItem(0, true)
            }
            R.id.two -> {
                binding.vp.setCurrentItem(1, true)
            }
            R.id.three -> {
                binding.vp.setCurrentItem(2, true)
            }
        }
        return false
    }


}