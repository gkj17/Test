package cn.guankejian.test.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.guankejian.test.MZTagAdapter
import cn.guankejian.test.MZViewModel
import cn.guankejian.test.R
import cn.guankejian.test.base.BasePagingDataAdapter
import cn.guankejian.test.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    BasePagingDataAdapter.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    var job: Job? = null
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var adapter: MZTagAdapter


    val viewModel: MZViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initVar()
        initData()
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

    private fun initFlow() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.captureTag().collectLatest {
                adapter.submitData(it)
            }
        }


        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.refreshLayout.isRefreshing =loadStates.refresh is LoadState.Loading
            }
        }
    }

    override fun onItemClick(adapter: BasePagingDataAdapter<*, *>, view: View, position: Int) {
    }

    override fun onRefresh() {
        adapter.refresh()
    }

}