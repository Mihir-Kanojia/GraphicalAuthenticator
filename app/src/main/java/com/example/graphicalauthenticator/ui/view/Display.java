package com.example.graphicalauthenticator.ui.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.Path;
import java.util.ArrayList;

import androidx.annotation.Nullable;

import static com.example.graphicalauthenticator.ui.auth.ImageAuthActivity.paintBrush;
import static com.example.graphicalauthenticator.ui.auth.ImageAuthActivity.path;

public class Display extends View {

    public static ArrayList<Path> pathList = new ArrayList<>();
    public static ArrayList<Integer> colorList = new ArrayList<>();
    public ViewGroup.LayoutParams params;
    public static int currentBrush = Color.BLACK;


    public Display(Context context) {
        super(context);
        init(context);
    }

    public Display(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Display(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Display(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
        paintBrush.setAntiAlias(true);
        paintBrush.setColor(Color.BLACK);
        paintBrush.setStyle(Paint.Style.STROKE);
        paintBrush.setStrokeCap(Paint.Cap.ROUND);
        paintBrush.setStrokeJoin(Paint.Join.ROUND);
        paintBrush.setStrokeWidth(20f);

        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                pathList.add(path);
                colorList.add(currentBrush);
                invalidate();
                return true;
            default:
                return false;
        }
    }

   @Override
   protected void onDraw(Canvas canvas) {
      for(int i=0; i< pathList.size();i++){
         paintBrush.setColor((colorList.get(i)));
         canvas.drawPath(pathList.get(i),paintBrush);
         invalidate();

      }
   }


}
