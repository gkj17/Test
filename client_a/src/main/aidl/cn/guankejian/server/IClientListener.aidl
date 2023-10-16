package cn.guankejian.server;

interface IClientListener {
    String getClientId();
    //server->client
    void server2client(String key, String value);
}