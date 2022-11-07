package com.example.graphicalauthenticator.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.graphicalauthenticator.R;
import com.example.graphicalauthenticator.ui.auth.EmailRegistrationActivity;
import com.example.graphicalauthenticator.ui.auth.ImageAuthActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidx.annotation.Nullable;

public class DrawingView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = Color.BLACK;
    //canvas
    private Canvas drawCanvas;
    private Canvas canvasPro;
    //canvas bitmap
    private Bitmap canvasBitmap;
    private float brushSize, lastBrushSize;
    private boolean erase = false;
    int i = 0;
    private Context context;

    public DrawingView(Context context) {
        super(context);
        this.context = context;
        setupDrawing();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupDrawing();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setupDrawing();
    }

    public void clear() {
//        drawPath.reset();
        setupDrawing();
        Log.d("TAG", "clear: method called");
//        Toast.makeText(, "", Toast.LENGTH_SHORT).show();
//        drawPath = new Path();
//        invalidate();
    }

    private void setupDrawing() {
        // TODO Auto-generated method stub


        brushSize = 10f;
        lastBrushSize = brushSize;


        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);


        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
        canvasPro = canvas;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }


    public void saveImage() throws IOException {
//        drawPath.reset();
        getDrawnMessage(canvasPro);
    }

    public void getDrawnMessage(Canvas canvas) throws IOException {
        Bitmap bitmap;
        setDrawingCacheEnabled(true);
        Toast.makeText(getContext(), "Toasting", Toast.LENGTH_SHORT).show();
//        String root = Environment.getExternalStorageDirectory().toString();
//        File imgDir = new File(root + "/ChitBak/");
//        String imgName;
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
//        imgDir.mkdirs();
//        imgName = "img" + i + ".jpg";
//        i++;
//        File file = new File(imgDir, imgName);
//        if (file.exists()) {
//            file.delete();
//        }
//        FileOutputStream outImg = new FileOutputStream(file);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outImg);
//
//        saveBitmapToJPG(bitmap, imgDir);
//        setDrawingCacheEnabled(false);
///////////////////////////////////////////////////////////
        File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(newBitmap);
        canvas1.drawColor(Color.WHITE);
        canvas1.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        scanMediaFile(photo);
        stream.close();
    }

    private void scanMediaFile(File photo) {
        Toast.makeText(context, "scanMediaFile() called", Toast.LENGTH_SHORT).show();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) {

    }
}

