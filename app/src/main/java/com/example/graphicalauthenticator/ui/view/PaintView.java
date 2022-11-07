package com.example.graphicalauthenticator.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class PaintView extends View {

    public int brushSize = 10;
    public static final int colorPen = Color.RED;
    public static final int colorEraser = Color.WHITE;
    public static final int defaultBgColor = Color.WHITE;
    private static final float touchTolerance = 4;

    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private int currentColor;
    private ArrayList<FingerPath> paths = new ArrayList<>();

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBigmapPaint = new Paint(Paint.DITHER_FLAG);


    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setDither(true);
        mPaint.setColor(colorPen);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setXfermode(null);
//        mPaint.setAlpha(0xff);

    }

    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = colorPen;
    }

    public void clear() {
        paths.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(defaultBgColor);

        for (FingerPath fp : paths) {
            mPaint.setColor(fp.getColor());
            mPaint.setStrokeWidth(fp.getStrokeWidth());
            mPaint.setMaskFilter(null);
            mCanvas.drawPath(fp.getPath(), mPaint);

        }

        canvas.drawBitmap(mBitmap, 0, 0, mBigmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {

        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, brushSize, mPath);
        paths.add(fp);

        mPaint.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= touchTolerance || dy >= touchTolerance) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    //    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }


}
