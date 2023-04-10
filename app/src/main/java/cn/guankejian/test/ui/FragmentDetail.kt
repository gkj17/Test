package cn.guankejian.test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.guankejian.test.R
import cn.guankejian.test.databinding.DetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentDetail @Inject constructor(
) : Fragment(){
    private lateinit var binding: DetailBinding



    private val tablesName = arrayOf("a", "b","c")


    private lateinit var tabFragmentsCreators: Map<Int, () -> Fragment>

    lateinit var aFragment: FragmentTest
    lateinit var bFragment: FragmentTest
    lateinit var cFragment: FragmentTest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail, container, false)

        aFragment = FragmentTest("a")
        bFragment = FragmentTest("b")
        cFragment = FragmentTest("c")

        tabFragmentsCreators = mapOf(
            0 to { aFragment },
            1 to { bFragment },
            2 to { cFragment }
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

        TabLayoutMediator(
            binding.tabLayout,
            binding.vp
        ) { tab, position ->
            tab.text = tablesName[position]
        }.attach()



        return binding.root
    }


}