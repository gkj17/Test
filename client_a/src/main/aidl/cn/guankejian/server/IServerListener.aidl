package cn.guankejian.server;

import cn.guankejian.server.IClientListener;


interface IServerListener {
    //client -> server
    void client2Server(String clientId, String message);

    void registerListener(IClientListener listener);

    void unregisterListener(IClientListener listener);
}