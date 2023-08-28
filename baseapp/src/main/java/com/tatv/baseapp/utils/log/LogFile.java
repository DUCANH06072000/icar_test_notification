package com.tatv.baseapp.utils.log;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.tatv.baseapp.R;
import com.tatv.baseapp.api.ApiService;
import com.tatv.baseapp.api.IBaseDataService;
import com.tatv.baseapp.api.WSConfig;
import com.tatv.baseapp.data.shared.BaseSharedPreference;
import com.tatv.baseapp.utils.json.JsonUtils;
import com.tatv.baseapp.utils.system.StorageUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogFile {
    private static final String TAG = "LogFile";
    
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());

    /**
     * Giới hạn kích thước file
     * */
    private final int FILE_SIZE_LIMIT = 1000;

    /**
     * Giới hạn số ngày lưu log
     * */
    private static int DATE_SIZE = 7;



    /**
     * Singleton
     * */
    private static LogFile instance;

    public static LogFile getInstance() {
        if (instance == null) {
            instance = new LogFile();
        }
        return instance;
    }


    private Context context;
    private BaseSharedPreference preference;
    private String dir = "icar";
    private File file;
    private File path;
    private File reportDir;


    private File jsonFile;

    public void init(Context context) {
        this.context = context;
        this.preference = new BaseSharedPreference(context);
        this.reportDir = new File(String.format("%s/%s/%s", Environment.getExternalStorageDirectory().getAbsolutePath(), dir, "/report"));
        clearFilesPeriodically();
        getPath();
    }

    /**
     * Khởi tạo có đường dẫn
     */
    public void init(Context context, String dir) {
        this.dir = dir;
        init(context);
    }


    /**
     * Thêm log
     * @param message - nội dung log
     */
    public void addLog(String message) {
        if (isLogAvailable()) {
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(getFileDir(), true));
                buf.append(message);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                Log.e(TAG, "addLog: " + e.getMessage());
            }
        }
    }

    /**
     * Kiểm tra sẵn sàng logfile hay chưa
     */
    private boolean isLogAvailable() {
        if (isPermissionGranted()) {
            while (StorageUtil.getStorageAvailable() <= 1024) {
                if (getNumberOfFile() >= 3) {
                    if (!deleteFirstFile()) {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (StorageUtil.getStorageAvailable() > 1024) {
                return true;
            } else {
                return false;
            }
        }
        return false;
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
            path = new File(String.format("%s/%s/", Environment.getExternalStorageDirectory().getAbsolutePath(), dir));
        }
        if (!path.exists()) {
            path.mkdirs();
        }
        return path;
    }

    /**
     * Lấy file theo đường dẫn
     */
    private File getFileDir() {
        File path = getPath();
        if (file == null) {
            file = new File(String.format("%s/%s_logcat.log", path, DATETIME_FORMAT.format(new Date())));
        }else {
            if (!file.exists()) {
                file = new File(String.format("%s/%s_logcat.log", path, DATETIME_FORMAT.format(new Date())));
            }
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        if(StorageUtil.getFileSize(file) >= FILE_SIZE_LIMIT){
            file = new File(String.format("%s/%s_logcat.log", getPath(), DATETIME_FORMAT.format(new Date())));
        }
        return file;
    }


    /**
     * Xóa file log định kỳ
     * Số ngày lưu trữ file log @variable DATE_SIZE
     * Xóa file có đuôi _logcat.log và .json
     */
    private void clearFilesPeriodically() {
        if (isPermissionGranted()) {
            File[] files = getPath().listFiles();
            if (files != null && files.length > 0) {
                sortFileByLastModifiedDESC(files);
                for (File file : files) {
                    String name = file.getName();
                    if (name.endsWith("_logcat.log") || name.endsWith(".json")){
                        if((System.currentTimeMillis() - file.lastModified())/(24 *60 * 60 * 1000) > DATE_SIZE){
                            try {
                                file.delete();
                                if (file.exists()) {
                                    file.getCanonicalFile().delete();
                                    if (file.exists()) {
                                        context.deleteFile(file.getName());
                                    }
                                }
                            } catch (IOException e) {
                                Log.e(TAG,"clearFilesPeriodically: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Xóa file log đầu tiên
     */
    private boolean deleteFirstFile() {
        if (isPermissionGranted()) {
            File[] files = getPath().listFiles();
            if (files != null && files.length > 0) {
                sortFileByLastModifiedDESC(files);
                for (File file : files) {
                    String name = file.getName();
                    if (name.endsWith("_logcat.log")) {
                        file.delete();
                        return true;
                    }

                }
            }
        }
        return false;
    }

    /**
     * return tổng số file log trong thư mục
     */
    private int getNumberOfFile() {
        if (isPermissionGranted()) {
            File[] files = getPath().listFiles();
            if (files != null) {
                return files.length;
            }
        }
        return -1;
    }


    /**
     * Sắp xếp danh sách file theo thời gian tạo file
     */
    private void sortFileByLastModifiedDESC(File[] files) {
        for (int i = 0; i < files.length - 1; i++) {
            for (int j = i + 1; j < files.length; j++) {
                if (files[i].lastModified() < files[j].lastModified()) {
                    File file = files[j];
                    files[j] = files[i];
                    files[i] = file;
                }
            }
        }
    }

    /**
     * Nén file report
     * Chỉ nén những file có đuôi _logcat.log và không bắt đầu bằng dấu .
     * */
    private boolean createZipReport() {
        if(StorageUtil.getStorageAvailable() > 5 * 1024){
            try {
                if(!reportDir.exists()){
                    reportDir.mkdir();
                }
                File[] files = getPath().listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().endsWith("_logcat.log") && !file.getName().startsWith(".")) {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            ZipOutputStream zos = new ZipOutputStream(bos);

                            InputStream is = new FileInputStream(file);
                            int count;
                            byte data[] = new byte[2048];
                            BufferedInputStream entryStream = new BufferedInputStream(is, 2048);
                            zos.putNextEntry(new ZipEntry(file.getName()));
                            while ((count = entryStream.read(data, 0, 2048)) != -1) {
                                zos.write(data, 0, count);
                            }
                            entryStream.close();
                            zos.closeEntry();

                            zos.close();
                            File fileSave = new File(String.format("%s/%s/report/%s.zip", Environment.getExternalStorageDirectory().getAbsolutePath(), dir, System.currentTimeMillis()));
                            FileOutputStream out = new FileOutputStream(fileSave);
                            bos.writeTo(out);
                            out.flush();
                            out.close();
                        }
                    }

                    return true;
                }else {
                    Log.e(TAG, "cache empty");
                }

            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }else {
            Log.e(TAG, "Bộ nhớ không đủ");
        }

        return false;
    }


    /**
     * Gửi dữ liệu log lên server
     * @param account - String: Số điện thoại
     * @param appId - String
     * @param appName - String
     * @param listener - Callback
     */
    int index = 0; // Đánh dấu index gửi report
    @SuppressLint("DefaultLocale")
    public synchronized void sendReport(String account, String appId, String appName, ResponseCallback listener) {
        if (isPermissionGranted()) {
            if(System.currentTimeMillis() - preference.getLastTimeReport() < 60 * 1000){
                listener.onError(String.format(context.getString(R.string.text_duplicate_report), (int)(60 - (System.currentTimeMillis() - preference.getLastTimeReport())/ 1000)));
                return;
            }
            listener.onWaiting(context.getString(R.string.text_compressing_data));
            if(createZipReport()){
                File reportDir = new File(String.format("%s/%s/%s", Environment.getExternalStorageDirectory().getAbsolutePath(), dir, "/report"));
                File[] reportFiles = reportDir.listFiles();
                index = 0;
                uploadReport(account, appId, appName, reportFiles, listener);
            }else {
                LogUtils.e(TAG, "Không tạo được file zip report");
                listener.onError(context.getString(R.string.text_create_report_error));
            }
        }else {
            Log.e(TAG, "Không có quyền truy cập file");
            listener.onError(context.getString(R.string.text_create_report_error));
        }
    }

    /**
     * Upload file report lên server
     * */
    private void uploadReport(String account, String appId, String appName, File[] files, ResponseCallback listener){
        if(files.length > 0){
            IBaseDataService service = ApiService.getService(context, WSConfig.BASE_LOG, IBaseDataService.class);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), files[index]);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", files[index].getName(), requestFile);

            Call<Object> call = service.uploadLog(account, appId, appName, body);
            call.enqueue(new Callback<Object>() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    LogUtils.d(TAG, "onResponse: " + response.code());
                    if(response.code() == 200){
                        if(index == files.length - 1){
                            listener.onSuccess();
                            deleteRecursive(reportDir);
                            markAsSendLog();
                        }else {
                            listener.onWaiting(String.format("Đã gửi thành công: %s", files[index].getName()));
                            index++;
                            uploadReport(account, appId, appName, files, listener);
                        }
                    }else {
                        listener.onError(String.format("%s (%d)" ,context.getString(R.string.text_send_report_failure), response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    LogUtils.e(TAG, "onFailure: " + t.getMessage());
                    listener.onError(context.getString(R.string.text_send_report_failure));
                }
            });
        }else {
            listener.onError(context.getString(R.string.text_report_file_empty));
        }
    }

    /**
     * Xóa đệ quy tất cả file/folder
     * */
    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()){
            for (File child : fileOrDirectory.listFiles()){
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }


    /**
     * Clear hết file log có đuôi _logcat.log
     * */
    private void clearLog(){
        for (File file : getPath().listFiles()){
            if (file.getName().endsWith("_logcat.log")) {
                if(file.exists()){
                    file.delete();
                }
            }
        }
    }

    /**
     * Đánh dấu là file đã gửi report
     * Đổi tên file có dấu . ở đầu
     * */
    private void markAsSendLog(){
        for (File file : getPath().listFiles()){
            if (file.getName().endsWith("_logcat.log") && !file.getName().startsWith(".")) {
                String newFileName = "." + file.getName();
                File newFile = new File(getPath(), newFileName);
                file.renameTo(newFile);
            }
        }
    }


    /**
     * Lưu file json log
     * @param obj - Đối tượng dữ liệu cần log
     * */
    public <T> void addLogJson(T obj){
        if (isPermissionGranted()) {
            if(jsonFile == null){
                jsonFile = new File(String.format("%s/%s_data.json", getPath(), DATETIME_FORMAT.format(new Date())));
            }
            if (!jsonFile.exists()) {
                try {
                    jsonFile.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(jsonFile, true));
                buf.append(JsonUtils.getJsonFromObj(obj));
                buf.append(",");
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Lấy danh sách đối tượng từ json file
     * @param name - Tên file
     * @param type - Class định dạng
     * */
    public <T> List<T> getLogJson(String name, Class<T> type){
        List<T> data = new ArrayList<>();
        if (isPermissionGranted()) {
            File file = new File(String.format("%s/%s", getPath(), name));
            if(file.exists()){
                FileInputStream is;
                BufferedReader reader;
                try {
                    is = new FileInputStream(file);
                    reader = new BufferedReader(new InputStreamReader(is));
                    String line = reader.readLine();
                    while(line != null){
                        if(line.length() > 0){
                            while (line.endsWith(",")){
                                line = line.substring(0, line.length() - 1);
                            }
                        }
                        data.add(JsonUtils.getObjFromJson(line, type));
                        line = reader.readLine();
                    }
                } catch (IOException e) {
                    Log.e(TAG,"getLogJson: " + e.getMessage());
                }
            }
        }

        return data;
    }



    public interface ResponseCallback {
        void onSuccess();
        void onError(String message);
        void onWaiting(String message);
    }
}
