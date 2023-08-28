package com.tatv.baseapp.utils.system;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import androidx.annotation.NonNull;
import com.tatv.baseapp.utils.log.LogUtils;

public class AppUtils {
    private static final String TAG = "AppUtils";

    /**
     * Resize drawable
     * */
    public static Drawable resizeDrawable(Context context, Drawable drawable, int size) {

        try {
            if (drawable != null) {
                Bitmap bitmap;
                try {
                    bitmap = ((BitmapDrawable) ((StateListDrawable) drawable).getCurrent()).getBitmap();
                } catch (Exception e) {
                    bitmap = getBitmapFromDrawable(drawable);
                }
                return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
            }
        }catch (Exception e){
            LogUtils.e(TAG, "resizeDrawable: " + e.getMessage());
        }
        return null;
    }


    /**
     * Resize drawable
     * Size 46
     * */
    public static Drawable getSDrawable(Context context, Drawable drawable) {
        return resizeDrawable(context, drawable, 46);
    }


    /**
     * Resize drawable
     * Size 84
     * */
    public static Drawable getBDrawable(Context context, Drawable drawable) {
        return resizeDrawable(context, drawable, 84);
    }

    /**
     * Resize drawable
     * Size 350
     * */
    public static Drawable getHDrawable(Context context, Drawable drawable) {
        return resizeDrawable(context, drawable, 350);
    }

    /**
     * Convert Bitmap from Drawable
     * */
    @NonNull
    public static Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    /**
     * Lấy tên ứng dụng từ package
     * */
    public static String getAppNameFromPackage(Context context, String pkg){
        final PackageManager pm = context.getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(pkg, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    /**
     * Lấy logo ứng dụng từ package
     * */
    public static Drawable getDrawableFromPackageName(Context mContext, String pkg){
        try {
            return mContext.getPackageManager().getApplicationIcon(pkg);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "getDrawableFromPackageName: " + e.getMessage());
        }
        return null;
    }


    /**
     * Kiểm tra ứng dụng đã cài đặt chưa
     * */
    public static boolean isPackageInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Kiểm tra service có đang chạy
     * */
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
