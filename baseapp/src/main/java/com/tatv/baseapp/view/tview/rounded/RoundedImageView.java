package com.tatv.baseapp.view.tview.rounded;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.tatv.baseapp.R;
import com.tatv.baseapp.utils.system.DimensionUtils;


public class RoundedImageView extends androidx.appcompat.widget.AppCompatImageView {
    private RectF rectF;
    private Path path = new Path();
    public float cornerRadius = 0;

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.RoundedView);
        CharSequence cornerRadius = arr.getString(R.styleable.RoundedView_cornerRadiusValue);
        this.cornerRadius = cornerRadius == null ? 15f : DimensionUtils.stringToDimension(cornerRadius.toString(), getResources().getDisplayMetrics());
        arr.recycle();
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF = new RectF(0, 0, w, h);
        resetPath();
    }

    @Override
    public void draw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(path);
        super.draw(canvas);
        canvas.restoreToCount(save);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

    private void resetPath() {
        path.reset();
        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);
        path.close();
    }
}