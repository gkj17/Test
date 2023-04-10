package cn.guankejian.test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.test.R
import cn.guankejian.test.databinding.FragmentTestBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentTest @Inject constructor(
    val tip:String
) : Fragment() {
    private lateinit var binding: FragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.text.text = "Test text ${tip}"

    }

}