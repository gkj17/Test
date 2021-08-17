package cn.guankejian.test.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.test.MZViewModel
import cn.guankejian.test.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val viewModel: MZViewModel by viewModels()

    var hasSave = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.save.setOnClickListener {
            viewModel.save()
            Toast.makeText(this, "save successfully!", Toast.LENGTH_SHORT).show()
            hasSave = true
        }
        binding.get.setOnClickListener {
            if (hasSave)
                lifecycleScope.launch(CoroutineExceptionHandler { _, throwable ->
                    throwable.printStackTrace()
                }) {
                    val value = viewModel.get().value as Int
                    println(value)
                }
            else
                Toast.makeText(this, "Please save the data first!", Toast.LENGTH_SHORT).show()
        }
    }


}