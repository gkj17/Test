package  cn.guankejian.test.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import cn.guankejian.test.R
import cn.guankejian.test.databinding.NetworkStateItemBinding

class LoadStateFooterViewHolder(
        val parent: ViewGroup,
        val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false)
) {
    private val binding = NetworkStateItemBinding.bind(itemView)
    private val retry = binding.retryButton

    companion object {
        const val DELAY_MILLION: Long = 800
    }


    init {
        retry.setOnClickListener {
            retry()
        }
    }

    fun bindState(loadState: LoadState) {
        binding.loadingLayout.postDelayed({
            binding.loadingLayout.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState is LoadState.Error


            if (loadState is LoadState.Error) {
                Snackbar
                        .make(parent, loadState.error.message.toString(), Snackbar.LENGTH_SHORT)
                        .setAction("重试") {

                            retry()
                        }
                        .show();
            }
        }, DELAY_MILLION)

//                errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
    }
    private fun retry(){
        retry.visibility = View.INVISIBLE
        binding.loadingLayout.isVisible = true
        retryCallback.invoke()
    }
}