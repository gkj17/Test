package cn.guankejian.server.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import cn.guankejian.server.KtorServer
import cn.guankejian.server.PushDataCallBack
import cn.guankejian.server.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

//        KtorServer.start(this,object : PushDataCallBack{
//            override fun onSearchKeyWordChanged(value: String?) {
//            }
//
//            override fun onSourceUrlChanged(value: String?) {
//            }
//
//            override fun onLiveUrlChanged(value: String?) {
//            }
//
//            override fun onEpgUrlChanged(value: String?) {
//            }
//
//            override fun onPushUrlChanged(value: String?) {
//            }
//
//        })
    }

    override fun onDestroy() {
        // 结束服务器
//        KtorServer.stop()
        super.onDestroy()
    }

}