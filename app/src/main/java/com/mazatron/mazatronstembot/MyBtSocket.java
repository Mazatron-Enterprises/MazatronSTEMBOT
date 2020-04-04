package com.mazatron.mazatronstembot;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

public class MyBtSocket extends Application {

    private static MyBtSocket sInstance;
    public static MyBtSocket getApplication() {
        return sInstance;
    }

    BluetoothSocket mbtSocket = null;
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public void setupBluetoothConnection(BluetoothSocket btSocket) {
          // Either setup your connection here, or pass it in
        mbtSocket = btSocket;
    }

    public BluetoothSocket getCurrentBluetoothConnection() {
        return mbtSocket;

    }

}
