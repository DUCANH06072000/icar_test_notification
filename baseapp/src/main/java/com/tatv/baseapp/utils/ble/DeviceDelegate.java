package com.tatv.baseapp.utils.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

/**
 * Created by tatv on 29/11/2022.
 */
public class DeviceDelegate {
    boolean connectCharacteristics(BluetoothGattService bleGattService, BluetoothGattService bleGattWriteService) { return true; }
    void onDescriptorWrite(BluetoothGatt g, BluetoothGattDescriptor d, int status) { /*nop*/ }
    void onCharacteristicChanged(BluetoothGatt g, BluetoothGattCharacteristic c) {/*nop*/ }
    void onCharacteristicWrite(BluetoothGatt g, BluetoothGattCharacteristic c, int status) { /*nop*/ }
    boolean canWrite() { return true; }
    void disconnect() {/*nop*/ }
}
