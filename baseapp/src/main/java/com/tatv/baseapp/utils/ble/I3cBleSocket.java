package com.tatv.baseapp.utils.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * Created by tatv on 16/03/2023.
 */
public class I3cBleSocket extends BLESocket{
    public I3cBleSocket(Context context) {
        super(context);
    }

    @Override
    public String getReadService() {
        return "0000ffe0-0000-1000-8000-00805f9b34fb";
    }

    @Override
    public String getReadCharacteristic() {
        return "0000ffe4-0000-1000-8000-00805f9b34fb";
    }

    @Override
    public String getWriteService() {
        return "0000ffe0-0000-1000-8000-00805f9b34fb";
    }

    @Override
    public String getWriteCharacteristic() {
        return "0000ffe9-0000-1000-8000-00805f9b34fb";
    }
}
