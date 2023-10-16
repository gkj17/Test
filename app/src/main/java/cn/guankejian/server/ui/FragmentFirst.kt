package cn.guankejian.server.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.server.App
import cn.guankejian.server.R
import cn.guankejian.server.ServerBinder
import cn.guankejian.server.ServerService
import cn.guankejian.server.databinding.FragmentFirstBinding
import cn.guankejian.server.logE
import coil.ImageLoader
import coil.load


@ExperimentalPagingApi
class FragmentFirst constructor(
) : Fragment() {
  private lateinit var binding: FragmentFirstBinding
  lateinit var serverBinder: ServerBinder
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {


    binding = DataBindingUtil.inflate(
      LayoutInflater.from(requireContext()),
      R.layout.fragment_first,
      container,
      false
    )

    App.getInstance().setOnGetClientDataCallback(object:App.OnGetClientDataCallback{
      override fun onGetClientData(key: String, value: String) {
        if(key == "client_a"){
          binding.content1.text = value
        }else{
          binding.content2.text = value
        }
      }
    })


    val intent = Intent(requireContext(), ServerService::class.java)
//    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK


    requireActivity().bindService(intent,conn, Context.BIND_AUTO_CREATE)

    binding.apply {
      send1.setOnClickListener {
        serverBinder.sendMessageToClient("client_a","这是服务端发送给客户端a的消息")
      }
      send2.setOnClickListener {
        serverBinder.sendMessageToClient("client_b","这是服务端发送给客户端b的消息")
      }
    }




    return binding.root
  }


  var conn: ServiceConnection = object : ServiceConnection {
    override fun onServiceDisconnected(name: ComponentName) {}
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
      //返回一个MsgService对象
      serverBinder = (service as ServerBinder)
    }
  }

  override fun onDestroy() {
    requireActivity().unbindService(conn)
    super.onDestroy()
  }



}