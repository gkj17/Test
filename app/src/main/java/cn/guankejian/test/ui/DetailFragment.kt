package cn.guankejian.test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.NavigationUI
import androidx.paging.ExperimentalPagingApi
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.guankejian.test.*
import cn.guankejian.test.base.BasePagingDataAdapter
import cn.guankejian.test.bean.TagUIModel
import cn.guankejian.test.databinding.FragmentDetailBinding
import cn.guankejian.test.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@ExperimentalPagingApi
@AndroidEntryPoint
class DetailFragment @Inject constructor(
) : Fragment(){

    val viewModel: AViewModel by navGraphViewModels(R.id.nav_graph){
        defaultViewModelProviderFactory
    }

    val layoutId = R.layout.fragment_detail

    val binding: FragmentDetailBinding get() = _binding!!
    private var _binding: FragmentDetailBinding? = null

    val args:DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        binding.title.text = args.title
    }



}