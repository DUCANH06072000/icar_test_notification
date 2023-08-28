package com.tatv.baseapp.utils.ble;

public interface BLESerialListener {
    void onSerialConnect      ();
    void onSerialDisconnect  ();
    void onSerialConnectError (Exception e);
    void onSerialRead         (byte[] data);
    void onSerialIoError      (Exception e);
}
