package cn.guankejian.test.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.guankejian.test.MZTagAdapter
import cn.guankejian.test.MZViewModel
import cn.guankejian.test.R
import cn.guankejian.test.base.BasePagingDataAdapter
import cn.guankejian.test.bean.UIModel
import cn.guankejian.test.databinding.ActivityMainBinding
import cn.guankejian.test.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentFirst @Inject constructor(
) : Fragment(),
    BasePagingDataAdapter.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    var job: Job? = null
    var otherJob: Job? = null
    var stateJob: Job? = null
    private lateinit var binding: FragmentFirstBinding

    @Inject
    lateinit var adapter: MZTagAdapter
    val controller: NavController by lazy { findNavController() }


    val viewModel: MZViewModel by navGraphViewModels(R.id.main_nav){
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
        initData()
        adapter.setOnItemClickListener(this)
    }



    fun initVar() {
        binding.let {
            it.refreshLayout.setOnRefreshListener(this)
            it.rv.adapter =ConcatAdapter(adapter)
        }
    }

    fun initData() {
        initFlow()
    }

    fun flowStateManager(
        loadStates: CombinedLoadStates,
        itemCount: Int
    ) {
        binding.refreshLayout.isRefreshing = false/*loadStates.refresh is LoadState.Loading*/
        binding.refreshLayout.isEnabled =
            loadStates.refresh !is LoadState.Loading && loadStates.append !is LoadState.Loading && loadStates.prepend !is LoadState.Loading

    }

    private fun initFlow() {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.captureTag().collectLatest {
                    adapter.submitData(it)
                }
            }
        }


        stateJob?.cancel()
        stateJob=viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    flowStateManager(loadStates, adapter.itemCount)
                }
            }
        }

        otherJob?.cancel()
        otherJob=viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .collectLatest {
                        if (it.refresh is LoadState.Error)
                            Snackbar.make(binding.coordinator, "网络错误", Snackbar.LENGTH_SHORT)
                                .show()
                    }
            }
        }



    }

    override fun onItemClick(adapter: BasePagingDataAdapter<*, *>, view: View, position: Int) {
        val item = adapter.getItemData(position)
        if(item is UIModel.IconItem){
            controller.navigate(FragmentFirstDirections.toSecond())
//            startActivity(Intent(this,DetailActivity::class.java))
        }
    }

    override fun onRefresh() {
        adapter.refresh()
    }

}