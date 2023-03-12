package com.example.graphicalauthenticator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import com.example.graphicalauthenticator.managers.ActivitySwitchManager;
import com.example.graphicalauthenticator.ui.auth.ImageAuthActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onStart() {
        super.onStart();
//        new ActivitySwitchManager(this, ImageAuthActivity.class).openActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new ActivitySwitchManager(this, ImageAuthActivity.class).openActivity();
    }
}