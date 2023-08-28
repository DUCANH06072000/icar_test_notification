package com.tatv.baseapp.utils.ble;

/**
 * Created by tatv on 17/04/2023.
 */
public interface BLEScanListener {
    void onScanning(BleDevice bleDevice);
    void onEndOfScan();
}
