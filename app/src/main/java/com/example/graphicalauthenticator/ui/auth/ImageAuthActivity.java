package com.example.graphicalauthenticator.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.graphicalauthenticator.R;
import com.example.graphicalauthenticator.databinding.ActivityImageAuthBinding;
import com.example.graphicalauthenticator.ui.view.Display;
import com.example.graphicalauthenticator.ui.view.MyDrawView;
import com.google.firebase.installations.Utils;
//import com.example.graphicalauthenticator.ui.view.PaintView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.example.graphicalauthenticator.Constants.CREATE_NEW_SIGNATURE;

public class ImageAuthActivity extends AppCompatActivity {

    private ActivityImageAuthBinding binding;

    public static Path path = new Path();
    public static Paint paintBrush = new Paint();

    //    private PaintView paintView;
    //    private DrawingView drawingView;
    private MyDrawView myDrawView;
    private Display drawingView;

    private static final String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Boolean isNewSignature = false;
    private Boolean firstSignature = true;
    private Uri contentUri;
    private File signature1;
    private File signature2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_auth);
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        isNewSignature = intent.getBooleanExtra(CREATE_NEW_SIGNATURE, false);

        Toast.makeText(this, "" + isNewSignature, Toast.LENGTH_SHORT).show();
        if (isNewSignature) {
            binding.btnVerify.setText("Next");
            firstSignature = true;
            updateUiAccordingly(1);
        } else {
            updateUiAccordingly(3);
        }

//        verifyStoragePermissions(this);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

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
//     ////////////////////           pathList.clear();
////                colorList.clear();
////                path.reset();
////            }
////        });
//
////        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                try {
////                    drawingView.saveImage();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        });
////////////////////////////////////////


        ConstraintLayout parent = findViewById(R.id.constraintLayout);
        myDrawView = new MyDrawView(ImageAuthActivity.this);
        parent.addView(myDrawView);

//        View view = binding.included;
//        paintView = view.findViewById(R.id.paintView);
//        drawingView = view.findViewById(R.id.paintView);

        drawingView = findViewById(R.id.paintView);


        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Bitmap b = loadBitmapFromView(myDrawView);
//                Bitmap b;
                drawingView.post(new Runnable() {
                    @Override
                    public void run() {

                        Bitmap signatureBitmap = loadBitmapFromView(myDrawView);
//                        Bitmap b = loadBitmapFromView(findViewById(R.id.constraintLayout));
//                        ImageView imageView = findViewById(R.id.imageView);
//                        imageView.buildDrawingCache();
//                        Bitmap b = imageView.getDrawingCache();
//                        findViewById(R.id.imageView2).setBackground(new BitmapDrawable(getResources(), b));

                        File cacheDir = ImageAuthActivity.this.getCacheDir();
                        File signatureFile1 = new File(cacheDir, "signature1.png");
                        File signatureFile2 = new File(cacheDir, "signature2.png");
                        try {

                            FileOutputStream outputStream1, outputStream2;
                            if (isNewSignature) {
                                if (firstSignature) {
                                    outputStream1 = new FileOutputStream(signatureFile1);
                                    signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream1);
                                    updateUiAccordingly(2);
                                    firstSignature = false;
                                } else {
                                    outputStream2 = new FileOutputStream(signatureFile2);
                                    signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream2);
                                    checkPythonModule(signatureFile1, signatureFile2);
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


//                        try (FileOutputStream outputStream = new FileOutputStream(signatureFile1)) {
//                            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                            checkPythonModule(signatureFile1, signatureFile2);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }


                        if (addJpgSignatureToGallery(signatureBitmap)) {
                            Toast.makeText(ImageAuthActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ImageAuthActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


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
//        Log.d("TAG", "onCreate: obj" + obj.toString());
    }

    private void updateUiAccordingly(int uiCase) {

        String step, title, desc, btnTxt;

        if (uiCase == 1) {
            step = "1/2";
            title = getString(R.string.sign1Title);
            desc = getString(R.string.sign1Desc);
            btnTxt = "Next";
        } else if (uiCase == 2) {
            myDrawView.clear();
            step = "2/2";
            title = getString(R.string.sign2Title);
            desc = getString(R.string.sign2Desc);
            btnTxt = "Save Signature";
        } else {
            step = "";
            title = getString(R.string.loginTitle);
            desc = getString(R.string.loginDesc);
            btnTxt = "Verify";
        }

        binding.tvStep.setText(step);
        binding.tvHead.setText(title);
        binding.tvDesc.setText(desc);
        binding.btnVerify.setText(btnTxt);

    }

    private void updateSignature1(File signature) {
        signature1 = signature;
    }

    private void updateSignature2(File signature) {
        signature2 = signature;
    }

    private void checkPythonModule(File signatureFile1, File signatureFile2) {

        if (!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        Python py = Python.getInstance();
        PyObject pyObj = py.getModule("script");
        PyObject obj = null;
//        String myPath = contentUri.getPath();
        obj = pyObj.callAttr("main", signatureFile1, signatureFile2);
        Log.d("TAG", "onCreate: obj" + obj.toString());
        Toast.makeText(this, "Similarity Score: " + obj.toString(), Toast.LENGTH_SHORT).show();
    }


    public Bitmap loadBitmapFromView(View view) {
//        if (v.getMeasuredHeight() <= 0) {
//            //        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//            Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
//            Canvas c = new Canvas(b);
////        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
//            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//            v.draw(c);
//            return b;
//        }
//        Toast.makeText(this, "Returning nulll", Toast.LENGTH_SHORT).show();
//    return null;

        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;


    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        ImageAuthActivity.this.sendBroadcast(mediaScanIntent);
        Log.d("TAG", "scanMediaFile: URI: " + contentUri);

//        if (!Python.isStarted())
//            Python.start(new AndroidPlatform(this));

//        Python py = Python.getInstance();
//        PyObject pyObj = py.getModule("script");
//        PyObject obj = null;
//        String myPath = contentUri.getPath();
//        obj = pyObj.callAttr("main", myPath, myPath);
//        Log.d("TAG", "onCreate: obj" + obj.toString());
    }

//    private void compareBitmap() {
//        // Convert the bitmaps to grayscale images
//        Bitmap signature1 = getSignatureBitmap1();
//        Bitmap signature2 = getSignatureBitmap2();
//        Mat matSignature1 = new Mat(signature1.getHeight(), signature1.getWidth(), CvType.CV_8UC1);
//        Mat matSignature2 = new Mat(signature2.getHeight(), signature2.getWidth(), CvType.CV_8UC1);
//        Utils.bitmapToMat(signature1, matSignature1);
//        Utils.bitmapToMat(signature2, matSignature2);
//        Imgproc.cvtColor(matSignature1, matSignature1, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.cvtColor(matSignature2, matSignature2, Imgproc.COLOR_BGR2GRAY);
//
//        // Resize the images
//        Size size = new Size(200, 100);
//        Imgproc.resize(matSignature1, matSignature1, size);
//        Imgproc.resize(matSignature2, matSignature2, size);
//
//        // Calculate the difference between the images
//        Mat diff = new Mat();
//        Core.absdiff(matSignature1, matSignature2, diff);
//
//        // Calculate the similarity score
//        double mse = Core.mean(diff.mul(diff)).val[0];
//        double ssim = calculateSSIM(matSignature1, matSignature2);
//        double ncc = calculateNCC(matSignature1, matSignature2);
//
//        // Determine the threshold
//        double threshold = 100;
//
//        // Determine if the two signatures are similar
//        if (mse < threshold && ssim > threshold && ncc > threshold) {
//            // The signatures are similar
//            Log.d(TAG, "compareBitmap: similar"+threshold);
//        } else {
//            // The signatures are not similar
//            Log.d(TAG, "compareBitmap: not similar"+threshold);
//
//        }
//    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}