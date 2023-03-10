package com.example.graphicalauthenticator.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

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

import java.util.Objects;

import static com.example.graphicalauthenticator.Constants.CREATE_NEW_SIGNATURE;

public class EmailLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    boolean doubleBackToExitPressedOnce = false;
    private ActivityEmailLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_login);
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        String email = Objects.requireNonNull(binding.etEmail.getText()).toString();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString();

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
                        new ActivitySwitchManager(EmailLoginActivity.this, MainActivity.class).openActivity();
                    }

                });
    }

//    private void updateUI(FirebaseUser user) {
//    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
//            new ActivitySwitchManager(this, MainActivity.class).openActivity();
            Intent intent = new Intent(EmailLoginActivity.this, ImageAuthActivity.class);
            intent.putExtra(CREATE_NEW_SIGNATURE, true);
            startActivity(intent);
        }
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