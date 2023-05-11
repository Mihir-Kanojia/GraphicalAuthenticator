package com.example.graphicalauthenticator.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.graphicalauthenticator.MainActivity;
import com.example.graphicalauthenticator.R;
import com.example.graphicalauthenticator.databinding.ActivityEmailLoginBinding;
import com.example.graphicalauthenticator.managers.ActivitySwitchManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scottyab.rootbeer.RootBeer;

import java.util.Objects;

import static com.example.graphicalauthenticator.Constants.AUTH_SIGNATURE;
import static com.example.graphicalauthenticator.Constants.CREATE_NEW_SIGNATURE;

public class EmailLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    boolean doubleBackToExitPressedOnce = false;
    private ActivityEmailLoginBinding binding;
    private boolean isAlreadyLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        RootBeer rootBeer = new RootBeer(this);
        if (rootBeer.isRooted()) {
            rootedDeviceDetectedExitApp();
        } else {


            mAuth = FirebaseAuth.getInstance();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
//            new ActivitySwitchManager(this, MainActivity.class).openActivity();
//            Toast.makeText(this, "Already login", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onCreate: I am called");
                Intent intent = new Intent(EmailLoginActivity.this, ImageAuthActivity.class);
                intent.putExtra(AUTH_SIGNATURE, true);
                startActivity(intent);
                finish();
            }

            binding = DataBindingUtil.setContentView(this, R.layout.activity_email_login);
            setContentView(binding.getRoot());

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Log.d("TAG", "onCreate: EmailLoginActivity");
            binding.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = binding.etEmail.getText().toString();
                    String password = binding.etPassword.getText().toString();
                    verifyAndSignInUser(email, password);
                }


            });

            binding.tvRegisterHere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ActivitySwitchManager(EmailLoginActivity.this, EmailRegistrationActivity.class).openActivity();
                }
            });

        }
    }

    private void rootedDeviceDetectedExitApp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Device is Rooted");
        builder.setMessage("This app cannot be run on rooted devices for security reasons. Please unroot your device and try again.");
        builder.setPositiveButton("Exit App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // exit the app
            }
        });
        builder.setCancelable(false); // prevent the user from dismissing the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void verifyAndSignInUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                        }
                    }

                    private void updateUI(FirebaseUser user) {

//                        Toast.makeText(EmailLoginActivity.this, "New login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EmailLoginActivity.this, ImageAuthActivity.class);
                        intent.putExtra(AUTH_SIGNATURE, true);
                        startActivity(intent);
                        finish();

                    }

                });
    }

//    private void updateUI(FirebaseUser user) {
//    }

    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
////            new ActivitySwitchManager(this, MainActivity.class).openActivity();
//            Toast.makeText(this, "Already login", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(EmailLoginActivity.this, ImageAuthActivity.class);
//            intent.putExtra(AUTH_SIGNATURE, true);
//            startActivity(intent);
//        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callFinish();
    }

    private void callFinish() {

        if (doubleBackToExitPressedOnce) {
            super.finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Hit back again to exit", Toast.LENGTH_SHORT).show();
        new Handler(getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

    }
}