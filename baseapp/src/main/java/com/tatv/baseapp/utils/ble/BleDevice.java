package com.tatv.baseapp.utils.ble;

/**
 * Created by tatv on 17/04/2023.
 */
public class BleDevice {
    private String name;
    private String macAddress;
    private boolean connected;
    private boolean selected;

    public BleDevice() { }

    public BleDevice(String name, String macAddress) {
        this.name = name;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public BleDevice setName(String name) {
        this.name = name;
        return this;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public BleDevice setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public boolean isConnected() {
        return connected;
    }

    public BleDevice setConnected(boolean connected) {
        this.connected = connected;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public BleDevice setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public String toString() {
        return "BleDevice{" +
                "name='" + name + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", connected=" + connected +
                ", selected=" + selected +
                '}';
    }
}
