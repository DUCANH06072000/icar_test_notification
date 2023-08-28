package com.tatv.baseapp.utils.gzip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import okhttp3.Headers;

/**
 * Created by tatv on 19/05/2023.
 */
public class GZIPUtils {
    public static final String ENCODE_UTF_8 = "UTF-8";
    public static final String ENCODE_ISO_8859_1 = "ISO-8859-1";

    /**
     * String được nén thành mảng byte gzip
     */
    public static byte[] compress(String str) {
        return compress(str, ENCODE_UTF_8);
    }

    /**
     * String được nén thành mảng byte gzip, cấu hình mã hóa tùy chọn
     */
    public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzipInputStream;
        try {
            gzipInputStream = new GZIPOutputStream(out);
            gzipInputStream.write(str.getBytes(encoding));
            gzipInputStream.close();
        } catch (IOException e) {
        }
        System.out.println("gzip compress error");
        return out.toByteArray();
    }

    /**
     * Nén byte[] thành String
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gzipInputStream.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            System.out.println("gzip uncompress error.");
        }
        return out.toByteArray();
    }

    /**
     * Giải nén byte[] thành String
     */
    public static String uncompressToString(byte[] bytes) {
        return uncompressToString(bytes, ENCODE_UTF_8);
    }

    /**
     * Giải nén byte[] thành String, cấu hình mã hóa tùy chọn
     */
    public static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            System.out.println("gzip uncompress to string error");
            return null;
        }
    }

    /**
     * Xác định xem gzip có tồn tại trong headers không
     */
    public static boolean isGzip(Headers headers) {
        boolean gzip = false;
        for (String key : headers.names()) {
            if (key.equalsIgnoreCase("Accept-Encoding") && headers.get(key).contains("gzip")
                    || key.equalsIgnoreCase("Content-Encoding") && headers.get(key).contains("gzip")) {
                gzip = true;
                break;
            }
        }
        return gzip;
    }
}
