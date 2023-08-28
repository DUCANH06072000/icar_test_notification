package com.tatv.baseapp.utils.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.tatv.baseapp.utils.convert.ByteUtils;
import com.tatv.baseapp.utils.log.LogUtils;
import com.welie.blessed.BluetoothCentralManager;
import com.welie.blessed.BluetoothCentralManagerCallback;
import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.BluetoothPeripheralCallback;
import com.welie.blessed.GattStatus;
import com.welie.blessed.HciStatus;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * wrap BLE communication into socket like class
 * - connect, disconnect and write as methods,
 * - read + status is returned by SerialListener
 */
public abstract class BLESocket extends BluetoothGattCallback {
    private static final String TAG = "BLESocket";

    // BLE standard does not limit, some BLE 4.2 devices support 251, various source say that Android has max 512
    private static final int MAX_MTU = 512;
    private static final int DEFAULT_MTU = 23;

    private final ArrayList<byte[]> writeBuffer;
    private final IntentFilter pairingIntentFilter;

    private Context context;
    private BLESerialListener listener;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BLEScanListener mBleScanListener;
    private boolean autoConnect;
    private DeviceDelegate delegate;
    private BluetoothDevice device;
    private BluetoothGatt gatt;
    private BluetoothGattCharacteristic readCharacteristic, writeCharacteristic;

    private boolean writePending;
    private boolean canceled;
    private boolean connected;
    private boolean scanning = false;
    private int SCAN_PERIOD = 8;
    private int payloadSize = DEFAULT_MTU - 3;

    public abstract String getReadService();

    public abstract String getReadCharacteristic();

    public abstract String getWriteService();

    public abstract String getWriteCharacteristic();

    public BLESocket(Context context) {
        this.context = context;
        writeBuffer = new ArrayList<>();
        pairingIntentFilter = new IntentFilter();
        pairingIntentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        pairingIntentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
    }

    public static final String DEVICE_TYPE_C39X = "C39X";
    public static final String DEVICE_TYPE_I3C = "I3C";
    public static final String DEVICE_TYPE_IP24 = "IP24";
    private static String deviceType = DEVICE_TYPE_C39X;

    private static BLESocket instance;

    public static BLESocket build(Context context) {
        return build(context, deviceType);
    }

    public static BLESocket build(Context context, String deviceType) {
        if (instance != null) {
            instance.disconnect();
        }
        switch (deviceType) {
            case DEVICE_TYPE_I3C:
                instance = new I3cBleSocket(context);
                break;
            case DEVICE_TYPE_IP24:
                instance = new Ip24BleSocket(context);
                break;
            case DEVICE_TYPE_C39X:
            default:
                instance = new C39xBleSocket(context);
        }
        return instance;
    }


    @SuppressLint("MissingPermission")
    public void connect() {
        Log.e(TAG, "connect");
        if (gatt != null) {
            canceled = false;
            new Handler(Looper.getMainLooper()).post(() -> {
                new Handler().postDelayed(() -> gatt.connect(), 1000);
            });
        }
    }


    @SuppressLint("MissingPermission")
    public void disconnect() {
        Log.e(TAG, "disconnect");
        if (gatt != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                gatt.disconnect();
                gatt.close();
                canceled = true;
                connected = false;
                readCharacteristic=null;
                writeCharacteristic=null;
                delegate=null;
                if (listener != null)
                    listener.onSerialDisconnect();
            });
        }
    }


    @SuppressLint("MissingPermission")
    public void destroy() {
        Log.e(TAG, "destroy");
        if (gatt != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                gatt.disconnect();
            });
        }
    }


    public void scan(BLEScanListener bleScanListener) {
        scan(SCAN_PERIOD, bleScanListener);
    }

    @SuppressLint("MissingPermission")
    public void scan(int time, BLEScanListener bleScanListener) {
        Log.e(TAG, "scan");
        mBleScanListener = bleScanListener;
        if (context != null) {
            if (mBluetoothManager == null) {
                mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = mBluetoothManager.getAdapter();
            }
            stopScan();
            new Handler().postDelayed(() -> {
                stopScan();
            }, time);
            scanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            Log.e(TAG, "you haven't initialized the object");
        }
    }

    @SuppressLint("MissingPermission")
    public void stopScan() {
        Log.e(TAG, "stopScan");
        scanning = false;
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        if (mBleScanListener != null) {
            mBleScanListener.onEndOfScan();
        }
    }


    @SuppressLint("MissingPermission")
    private BluetoothAdapter.LeScanCallback mLeScanCallback = (device, rssi, scanRecord) -> {
        if (device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
            if (mBleScanListener != null) {
                mBleScanListener.onScanning(new BleDevice(device.getName(), device.getAddress()));
            }
        }
    };

    BluetoothCentralManager central;
    BluetoothPeripheral peripheral;

    /**
     * connect-success and most connect-errors are returned asynchronously to listener
     */
    public void connect(String macAddress, boolean autoConnect, BLESerialListener listener) {
        if (device != null && gatt != null) {
            disconnect();

        }
        device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
        connect(autoConnect, listener);
    }

    @SuppressLint("MissingPermission")
    public void connect(boolean autoConnect, BLESerialListener listener) {
        try {
            canceled = false;
            this.listener = listener;
            this.autoConnect = autoConnect;
            Log.e(TAG, "connect " + device);
            if (Build.VERSION.SDK_INT < 23) {
                Log.e(TAG, "connectGatt");
                gatt = device.connectGatt(context, autoConnect, this);
            } else {
                Log.e(TAG, "connectGatt,LE");
                gatt = device.connectGatt(context, autoConnect, this, BluetoothDevice.TRANSPORT_LE);
            }
            if (gatt == null)
                throw new IOException("connectGatt failed");
            // continues asynchronously in onPairingBroadcastReceive() and onConnectionStateChange()
        } catch (IOException e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    public boolean isConnected() {
        if (gatt != null) {
            if (gatt.getConnectionState(device) == BluetoothProfile.STATE_CONNECTED) {
                return true;
            }
        }
        return false;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        // status directly taken from gat_api.h, e.g. 133=0x85=GATT_ERROR ~= timeout
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.e(TAG, "connect status " + status + ", discoverServices");
            if (!gatt.discoverServices())
                onSerialConnectError(new IOException("discoverServices failed"));
//            connect(autoConnect, listener);
        } else if ((status == 8 && newState == BluetoothProfile.STATE_DISCONNECTED) || (status == 133 && newState == BluetoothProfile.STATE_DISCONNECTED)) {
            Log.e(TAG, "onConnectionStateChange " + status + ", disconnect");
            gatt.disconnect();
            gatt.close();
            gatt.getDevice().connectGatt(context, false, this);
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            onSerialConnectError(new IOException("gatt status " + status));
        } else {
            onSerialConnectError(new IOException("unknown connect state " + newState + " " + status));
        }
        // continues asynchronously in onServicesDiscovered()
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        Log.e(TAG, "servicesDiscovered, status " + status);
        if (canceled)
            return;
        connectCharacteristics1(gatt);
    }

    private void connectCharacteristics1(BluetoothGatt gatt) {
        Log.d(TAG, "connectCharacteristics1");
        boolean sync = true;
        writePending = false;
        BluetoothGattService bleGattService = null;
        BluetoothGattService bleGattWriteService = null;

        for (BluetoothGattService gattService : gatt.getServices()) {
            Log.e(TAG, gattService.getUuid().toString());
            if (gattService.getUuid().toString().equals(getReadService())) {
                bleGattService = gattService;
            }
            if (gattService.getUuid().toString().equals(getWriteService())) {
                bleGattWriteService = gattService;
            }
        }
        if (bleGattService != null && bleGattWriteService != null) {
            delegate = new CustomDelegate();
            sync = delegate.connectCharacteristics(bleGattService, bleGattWriteService);
        }
        if (canceled) {
            return;
        }

        if (delegate == null || readCharacteristic == null || writeCharacteristic == null) {
            Log.e(TAG, "delegate: " + (delegate == null));
            Log.e(TAG, "readCharacteristic: " + (readCharacteristic == null));
            Log.e(TAG, "writeCharacteristic: " + (writeCharacteristic == null));
            for (BluetoothGattService gattService : gatt.getServices()) {
                Log.e(TAG, "service " + gattService.getUuid());
                for (BluetoothGattCharacteristic characteristic : gattService.getCharacteristics())
                    Log.e(TAG, "characteristic " + characteristic.getUuid());
            }
            onSerialConnectError(new IOException("no serial profile found"));
            return;
        }
        if (sync) {
            connectCharacteristics2(gatt);
        }
    }

    @SuppressLint("MissingPermission")
    private void connectCharacteristics2(BluetoothGatt gatt) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e(TAG, "request max MTU");
            if (!gatt.requestMtu(MAX_MTU))
                onSerialConnectError(new IOException("request MTU failed"));
            // continues asynchronously in onMtuChanged
        } else {
            connectCharacteristics3(gatt);
        }
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        Log.e(TAG, "mtu size " + mtu + ", status=" + status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            payloadSize = mtu - 3;
            Log.e(TAG, "payload size " + payloadSize);
        }
        connectCharacteristics3(gatt);
    }

    @SuppressLint("MissingPermission")
    private void connectCharacteristics3(BluetoothGatt gatt) {
        Log.d(TAG, "connectCharacteristics3");
        int writeProperties = writeCharacteristic.getProperties();
        if ((writeProperties & (BluetoothGattCharacteristic.PROPERTY_WRITE +
                BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) == 0) {
            onSerialConnectError(new IOException("write characteristic not writable"));
            return;
        }

        if (!gatt.setCharacteristicNotification(readCharacteristic, true)) {
            onSerialConnectError(new IOException("no notification for read characteristic"));
            return;
        }
        checkDeviceAdvance(gatt);
    }



    @SuppressLint("MissingPermission")
    private void checkDeviceAdvance(BluetoothGatt gatt) {
        if (readCharacteristic.getDescriptors().isEmpty()){
            onSerialConnectError(new IOException("no CCCD descriptor for read characteristic"));
            return;
        }else {
            Log.e(TAG,"getDescriptor: "+readCharacteristic.getDescriptors().size() );
        }
        BluetoothGattDescriptor readDescriptor = readCharacteristic.getDescriptor(readCharacteristic.getDescriptors().get(0).getUuid());
        if (readDescriptor == null) {
            onSerialConnectError(new IOException("no CCCD descriptor for read characteristic"));
            return;
        }
        int readProperties = readCharacteristic.getProperties();
        if ((readProperties & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
            readDescriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        } else if ((readProperties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
            readDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            onSerialConnectError(new IOException("no indication/notification for read characteristic (" + readProperties + ")"));
            return;
        }
        Log.d(TAG, "checkDeviceAdvance3");
        Log.e(TAG, "writing read characteristic descriptor");
        if (!gatt.writeDescriptor(readDescriptor)) {
            onSerialConnectError(new IOException("read characteristic CCCD descriptor not writable"));
        }
        Log.d(TAG, "checkDeviceAdvance4");
        // continues asynchronously in onDescriptorWrite()
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        delegate.onDescriptorWrite(gatt, descriptor, status);
        if (canceled)
            return;
        Log.d(TAG, "onDescriptorWrite: " + descriptor.getCharacteristic());
        if (descriptor.getCharacteristic() == readCharacteristic) {
            Log.e(TAG, "writing read characteristic descriptor finished, status=" + status);
            if (status != BluetoothGatt.GATT_SUCCESS) {
                onSerialConnectError(new IOException("write descriptor failed"));
            } else {
                // onCharacteristicChanged with incoming data can happen after writeDescriptor(ENABLE_INDICATION/NOTIFICATION)
                // before confirmed by this method, so receive data can be shown before device is shown as 'Connected'.
                onSerialConnect();
            }
        }
    }

    /*
     * read
     */
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (canceled)
            return;
        delegate.onCharacteristicChanged(gatt, characteristic);
        if (characteristic == readCharacteristic) { // NOPMD - test object identity
            byte[] data = readCharacteristic.getValue();
            onSerialRead(data);
            if (!connected) {
                onSerialConnect();
            }
            Log.e(TAG, "read, len=" + data.length);
        }
    }


    /*
     * write
     */

    public void write(String msg) throws IOException {
        byte[] data = ByteUtils.strToByteArray(msg);
        write(data);
    }

    @SuppressLint("MissingPermission")
    public void write(byte[] data) throws IOException {
        if (canceled || !connected || writeCharacteristic == null)
            throw new IOException("not connected");
        byte[] data0;
        synchronized (writeBuffer) {
            if (data.length <= payloadSize) {
                data0 = data;
            } else {
                data0 = Arrays.copyOfRange(data, 0, payloadSize);
            }
            if (!writePending && writeBuffer.isEmpty() && delegate.canWrite()) {
                writePending = true;
            } else {
                writeBuffer.add(data0);
                Log.e(TAG, "write queued, len=" + data0.length);
                data0 = null;
            }
            if (data.length > payloadSize) {
                for (int i = 1; i < (data.length + payloadSize - 1) / payloadSize; i++) {
                    int from = i * payloadSize;
                    int to = Math.min(from + payloadSize, data.length);
                    writeBuffer.add(Arrays.copyOfRange(data, from, to));
                    Log.e(TAG, "write queued, len=" + (to - from));
                }
            }
        }
        if (data0 != null) {
            writeCharacteristic.setValue(data0);
            if (!gatt.writeCharacteristic(writeCharacteristic)) {
                onSerialIoError(new IOException("write failed"));
            } else {
                Log.e(TAG, "write started, len=" + data0.length);
            }
        }
        // continues asynchronously in onCharacteristicWrite()
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.d(TAG, "onCharacteristicWrite");
        if (canceled || !connected || writeCharacteristic == null)
            return;
        Log.d(TAG, "onCharacteristicWrite1");
        if (status != BluetoothGatt.GATT_SUCCESS) {
            onSerialIoError(new IOException("write failed"));
            return;
        }
        Log.d(TAG, "onCharacteristicWrite2");
        delegate.onCharacteristicWrite(gatt, characteristic, status);
        if (canceled)
            return;
        Log.d(TAG, "onCharacteristicWrite3");
        if (characteristic == writeCharacteristic) { // NOPMD - test object identity
            Log.e(TAG, "write finished, status=" + status);
            writeNext();
        }
    }

    @SuppressLint("MissingPermission")
    private void writeNext() {
        final byte[] data;
        synchronized (writeBuffer) {
            if (!writeBuffer.isEmpty() && delegate.canWrite()) {
                writePending = true;
                data = writeBuffer.remove(0);
            } else {
                writePending = false;
                data = null;
            }
        }
        if (data != null) {
            writeCharacteristic.setValue(data);
            if (!gatt.writeCharacteristic(writeCharacteristic)) {
                onSerialIoError(new IOException("write failed"));
            } else {
                Log.e(TAG, "write started, len=" + data.length);
            }
        }
    }

    /**
     * SerialListener
     */
    private void onSerialConnect() {
        connected = true;
        if (listener != null)
            listener.onSerialConnect();
    }

    private void onSerialConnectError(Exception e) {
        canceled = true;
        connected = false;
        if (listener != null)
            listener.onSerialConnectError(e);
    }

    private void onSerialRead(byte[] data) {
        if (listener != null)
            listener.onSerialRead(data);
    }

    private void onSerialIoError(Exception e) {
        writePending = false;
        canceled = true;
        if (listener != null)
            listener.onSerialIoError(e);
    }

    /**
     * device delegates
     */

    private class CustomDelegate extends DeviceDelegate {
        @Override
        boolean connectCharacteristics(BluetoothGattService bleGattService, BluetoothGattService bleGattWriteService) {
            Log.e(TAG,"connectCharacteristics" +getWriteCharacteristic());
            readCharacteristic = bleGattService.getCharacteristic(UUID.fromString(getReadCharacteristic()));
            writeCharacteristic = bleGattWriteService.getCharacteristic(UUID.fromString(getWriteCharacteristic()));
            return true;
        }
    }

}
