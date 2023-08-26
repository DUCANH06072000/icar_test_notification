package com.tatv.baseapp.utils.qr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by tatv on 07/10/2022.
 */
public class QRUtils {
    private final String TAG = "QRUtil";

    /**
     * Set QR code to ImageView
     * */
    public void setQRView(String code, ImageView view, int size){
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, size, size);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            view.setImageBitmap(bmp);
        } catch (WriterException e) {
            Log.e(TAG, e.getMessage());
        }

    }
}
