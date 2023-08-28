package com.tatv.baseapp.utils.device;

/**
 * Created by tatv on 05/01/2023.
 */
public class Device {
    public static final String MOBILEPHONE = "MOBILE PHONE";
    public static final String ANDROID_BOX = "ANDROID BOX";
    public static final String OWNICEC970 = "OWNICE C970";
    public static final String OWNICEC970PLUS = "OWNICE C970 (+360)";
    public static final String OWNICEC960 = "OWNICE C960";
    public static final String OWNICEC800 = "OWNICE C800";
    public static final String OWNICECC500S = "OWNICE C500+";
    public static final String ELLIVIEWS4 = "ELLIVIEW S4";
    public static final String ELLIVIEWU4 = "ELLIVIEW U4";
    public static final String ELLIVIEWS3 = "ELLIVIEW S3";
    public static final String ELLIVIEWU3 = "ELLIVIEW U3";
    public static final String ELLIVIEWD4 = "ELLIVIEW D4";

    private int id;
    private String name;
    private boolean selected;

    public Device() {}

    public Device(int id, String name, boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public Device setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Device setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public Device setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", selected=" + selected +
                '}';
    }
}
