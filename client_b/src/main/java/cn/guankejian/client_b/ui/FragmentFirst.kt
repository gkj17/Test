package cn.guankejian.client_b.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.os.RemoteException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.client_b.R
import cn.guankejian.client_b.databinding.FragmentFirstBinding
import cn.guankejian.client_b.logD
import cn.guankejian.server.IClientListener
import cn.guankejian.server.IServerListener


@ExperimentalPagingApi
class FragmentFirst constructor(
) : Fragment() {
  private lateinit var binding: FragmentFirstBinding
  var mBinder: IServerListener? = null
  private val listener = object : IClientListener.Stub() {
    override fun getClientId(): String {
      return "client_b"
    }
    override fun server2client(key: String, value: String) {
      "收到服务器发送的数据----：$key -> $value".logD()

//        binding.content.text = "${key} -> ${value}"
    }

    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
      return try {
        super.onTransact(code, data, reply, flags)
      } catch (e: RuntimeException) {
        e.printStackTrace()
        throw e
      }
    }
  }

  private val mConnection: ServiceConnection = object : ServiceConnection {
    override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
      "onServiceConnected() called with: name = $componentName, binder = $iBinder".logD()

      mBinder = IServerListener.Stub.asInterface(iBinder)
      try {
        "绑定并且注册监听成功".logD()
        mBinder?.registerListener(listener)
      } catch (e: RemoteException) {
        e.printStackTrace()
      }

    }

    override fun onServiceDisconnected(componentName: ComponentName) {
      mBinder = null
    }

  }

  private fun bindService() {
    val intent = Intent("cn.guankejian.server.action")
    intent.setClassName("cn.guankejian.server","cn.guankejian.server.ServerService")


    val bindSucc = requireActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    if (bindSucc) {
      Toast.makeText(requireContext(), "bind ok", Toast.LENGTH_SHORT).show()
    } else {
      Toast.makeText(requireContext(), "bind fail", Toast.LENGTH_SHORT).show()
    }

  }



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

    binding.apply{
      bind.setOnClickListener {
        bindService()
      }

      send.setOnClickListener {
        mBinder?.client2Server("client_b","【客户端b的消息】")
      }
    }


    return binding.root
  }



}