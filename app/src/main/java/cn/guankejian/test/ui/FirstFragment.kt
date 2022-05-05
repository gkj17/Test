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
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.NavigationUI
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.guankejian.test.*
import cn.guankejian.test.base.BasePagingDataAdapter
import cn.guankejian.test.bean.TagUIModel
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
class FirstFragment @Inject constructor(
) : Fragment(), SwipeRefreshLayout.OnRefreshListener, BasePagingDataAdapter.OnItemClickListener {

    protected var job: Job? = null

     val viewModel: AViewModel by navGraphViewModels(R.id.nav_graph){
         defaultViewModelProviderFactory
     }

     val layoutId = R.layout.fragment_first

    val binding: FragmentFirstBinding get() = _binding!!
    private var _binding: FragmentFirstBinding? = null

    val controller: NavController by lazy { findNavController() }


    @Inject
    lateinit var adapter: MZTagAdapter


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
        initBar()
        initListener()
        initData()
    }

    fun initBar(){
        NavigationUI.setupWithNavController(binding.toolbar, controller)

    }

    fun initListener(){
        binding.refreshLayout.setOnRefreshListener(this)

        adapter.setOnItemClickListener(this)

        binding.rv.adapter = adapter.withLoadStateFooter(
            footer = LoadStateFooteAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener {
            if(it.refresh is LoadState.Loading)
                binding.refreshLayout.isRefreshing = true
            if (it.refresh !is LoadState.Loading)
                binding.refreshLayout.isRefreshing = false
        }
    }

    fun initData(){
        initFlow()
    }

    fun initFlow(){
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch() {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.captureTag().collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job = null
    }

    override fun onRefresh() {
        adapter.refresh()
    }

    override fun onItemClick(adapter: BasePagingDataAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.getItemData(position)
        if(bean is TagUIModel.Item) {
            controller.navigate(FirstFragmentDirections.toDetail(bean.item.toString()))
        }
    }


}