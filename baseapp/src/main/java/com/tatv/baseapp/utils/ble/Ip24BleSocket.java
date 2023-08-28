package com.tatv.baseapp.utils.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * Created by tatv on 16/03/2023.
 */
public class Ip24BleSocket extends BLESocket{
    public Ip24BleSocket(Context context) {
        super(context);
    }

    @Override
    public String getReadService() {
        return "0000ffe0-0000-1000-8000-00805f9b89f5";
    }

    @Override
    public String getReadCharacteristic() {
        return "1111ffe2-0000-1000-8000-00805f9b89f5";
    }

    @Override
    public String getWriteService() {
        return "0000ffe0-0000-1000-8000-00805f9b89f5";
    }

    @Override
    public String getWriteCharacteristic() {
        return "1111ffe1-0000-1000-8000-00805f9b89f5";
    }
}
