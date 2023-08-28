package com.tatv.baseapp.utils.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.tatv.baseapp.utils.log.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by tatv on 05/01/2023.
 */
public class DeviceUtils {
    private static final String TAG = "DeviceUtils";
    private Context context;

    private final List<Device> devices = new ArrayList<>();
    private File path;
    private File file;

    /**
     * Singleton
     * */
    private static DeviceUtils instance;

    public static DeviceUtils getInstance(){
        if(instance == null){
            instance = new DeviceUtils();
        }
        return instance;
    }

    public DeviceUtils() {
        devices.addAll(getMockDevices());
    }


    /**
     * Khởi tạo
     * */
    public void init(Context context){
        this.context = context;
    }


    /**
     * Trả về AppId
     * */
    @SuppressLint("HardwareIds")
    public String getAppId(){
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * Trả về appId random
     * @param appPrefix Tiền tố ứng dụng
     * */
    public String getAppIdRandom(String appPrefix) {
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();

        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        int randomInt = random.nextInt(alphabet.length());
        int randomInt1 = random.nextInt(999999999);
        char[] alphabetArr = alphabet.toCharArray();
        return  appPrefix + alphabetArr[randomInt] + randomInt + randomInt1 + currentTime;
    }

    /**
     * Trả về appId random
     * */
    public String getAppIdRandom() {
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();

        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        int randomInt = random.nextInt(alphabet.length());
        int randomInt1 = random.nextInt(999999999);
        char[] alphabetArr = alphabet.toCharArray();
        return  "" + alphabetArr[randomInt] + randomInt + randomInt1 + currentTime;
    }

    /**
     * Trả về Device Model (Device Name)
     * */
    public String getModel(){
        return Build.MODEL;
    }

    /**
     * Kiểm tra quyền
     */
    private boolean isPermissionGranted() {
        if (context != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                Log.e(TAG, "permission denied");
                return false;
            }
        } else {
            Log.e(TAG, "non init");
            return false;
        }
    }

    /**
     * Lấy đường dẫn
     */
    private File getPath() {
        if (path == null) {
            path = new File(String.format("%s/%s/", Environment.getExternalStorageDirectory().getAbsolutePath(), ".icar"));
        }
        if (!path.exists()) {
            path.mkdirs();
        }
        return path;
    }

    /**
     * Lấy file lưu
     * */
    private File getFile(){
        File path = getPath();
        if(file == null){
            file = new File(String.format("%s/%s", path, ".device.icv"));
        }else {
            if (!file.exists()) {
                file = new File(String.format("%s/%s", path, ".device.icv"));
            }
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return file;
    }

    /**
     * Lấy danh sách thiết bị
     * */
    public List<Device> getDevices(){
        if(isPermissionGranted()){
            FileInputStream is;
            BufferedReader reader;
            try {
                is = new FileInputStream(getFile());
                reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                while(line != null){
                    if(line.length() > 0){
                        String[] data = line.split(",");
                        if(data.length == 3){
                            try {
                                Device device = new Device(Integer.parseInt(data[0]), data[1], Boolean.parseBoolean(data[2]));
                                for(int i = 0; i < devices.size(); i++){
                                    if(devices.get(i).getId() == device.getId()){
                                        devices.get(i).setSelected(device.isSelected());
                                    }
                                }
                            }catch (Exception e){
                                LogUtils.e(TAG, "getDevices: " + e.getMessage());
                            }
                        }
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                LogUtils.e(TAG,"getDevices: " + e.getMessage());
            }
        }else {
            LogUtils.e(TAG,"getDevices: permission denied");
        }

        return devices;
    }

    /**
     * Lấy danh sách thiết bị mặc định
     * */
    private List<Device> getMockDevices(){
        return Arrays.asList(
                new Device(0, Device.OWNICEC970, true),
                new Device(1, Device.OWNICEC970PLUS, false),
                new Device(2, Device.OWNICEC960, false),
                new Device(3, Device.OWNICEC800, false),
                new Device(4, Device.OWNICECC500S, false),
                new Device(5, Device.ELLIVIEWS4, false),
                new Device(6, Device.ELLIVIEWU4, false),
                new Device(7, Device.ELLIVIEWS3, false),
                new Device(8, Device.ELLIVIEWU3, false),
                new Device(9, Device.ELLIVIEWD4, false),
                new Device(10, Device.ANDROID_BOX, false),
                new Device(11, Device.MOBILEPHONE, false)
        );
    }

    /**
     * Khi thay đổi thiết bị
     * */
    public void onDeviceChanged(int id){
        if(isPermissionGranted()){
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(getFile(), false));
                for(int i = 0; i < devices.size(); i++){
                    devices.get(i).setSelected(i == id);
                    String line = String.format("%s,%s,%s", devices.get(i).getId(), devices.get(i).getName(), devices.get(i).isSelected());
                    buf.append(line);
                    buf.newLine();
                }
                buf.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

    }

    /**
     * Khi thay đổi thiết bị theo tên
     * */
    public void onDeviceChanged(String name){
        if(isPermissionGranted()){
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(getFile(), false));
                for(int i = 0; i < devices.size(); i++){
                    devices.get(i).setSelected(devices.get(i).getName().equals(name));
                    String line = String.format("%s,%s,%s", devices.get(i).getId(), devices.get(i).getName(), devices.get(i).isSelected());
                    buf.append(line);
                    buf.newLine();
                }
                buf.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

    }

    /**
     *Trả ra thiết bị được chọn
     * */
    public Device isDeviceSelected(){
        if (getDevices().size()>0){
            for (int i=0;i<getDevices().size();i++){
                if (getDevices().get(i).isSelected()){
                    return getDevices().get(i);
                }
            }
        }
        return null;
    }
}
