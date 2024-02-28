package cn.guankejian.server;

public interface PushDataCallBack {

    void onSearchKeyWordChanged(String value);

    void onSourceUrlChanged(String value);

    void onLiveUrlChanged(String value);

    void onEpgUrlChanged(String value);

    void onPushUrlChanged(String value);

}