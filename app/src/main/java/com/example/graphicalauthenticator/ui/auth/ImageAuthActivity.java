package com.example.graphicalauthenticator.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.graphicalauthenticator.R;
import com.example.graphicalauthenticator.databinding.ActivityImageAuthBinding;
import com.example.graphicalauthenticator.ui.view.Display;
import com.example.graphicalauthenticator.ui.view.DrawingView;
import com.example.graphicalauthenticator.ui.view.MyDrawView;
import com.example.graphicalauthenticator.ui.view.PaintView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;
import static com.example.graphicalauthenticator.ui.view.Display.colorList;
import static com.example.graphicalauthenticator.ui.view.Display.currentBrush;
import static com.example.graphicalauthenticator.ui.view.Display.pathList;

public class ImageAuthActivity extends AppCompatActivity {

    private ActivityImageAuthBinding binding;

    public static Path path = new Path();
    public static Paint paintBrush = new Paint();

    private PaintView paintView;
    private DrawingView drawingView;
    private MyDrawView myDrawView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_auth);
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

//        View view = binding.included;
//        paintView = view.findViewById(R.id.paintView);
//        drawingView = view.findViewById(R.id.paintView);

//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        paintView.init(metrics);
//
//        binding.btnClearScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawingView.clear();
//            }
//        });

//////////////////////////////////////////////////////////
//        paintBrush.setColor(Color.BLACK);

//        binding.btnClearScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pathList.clear();
//                colorList.clear();
//                path.reset();
//            }
//        });

//        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    drawingView.saveImage();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//////////////////////////////////////////////////////////


        ConstraintLayout parent = (ConstraintLayout) findViewById(R.id.constraintLayout);
        myDrawView = new MyDrawView(ImageAuthActivity.this);
        parent.addView(myDrawView);


        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap b = loadBitmapFromView(myDrawView);

//                parent.setDrawingCacheEnabled(true);
//                Bitmap b = parent.getDrawingCache();
//
//                Bitmap b;
//                parent.setDrawingCacheEnabled(true);
//
//                view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//
//                view.buildDrawingCache(true);
//                b = Bitmap.createBitmap(view.getDrawingCache());

//                b = Bitmap.createBitmap(parent.getDrawingCache());
//                parent.setDrawingCacheEnabled(false);

//                b = BitmapFactory.decodeResource(getResources(), R.id.constraintLayout);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root);
                myDir.mkdirs();
                String fname = "Image-" + "image_name" + ".jpg";
                File file = new File(myDir, fname);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                b.compress(Bitmap.CompressFormat.PNG, 95, fos);


            }
        });


        binding.btnClearScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.clear();
            }
        });

//        if (!Python.isStarted())
//            Python.start(new AndroidPlatform(this));
//
//        Python py = Python.getInstance();
//        PyObject pyObj = py.getModule("script");
//        PyObject obj = null;
//
//        obj = pyObj.callAttr("main", "2", "2");
//        Log.d(TAG, "onCreate: obj"+ obj.toString());

    }

    public Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }


}