package com.example.graphicalauthenticator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.graphicalauthenticator.databinding.ActivityMainBinding;
import com.example.graphicalauthenticator.Modal.User;
import com.example.graphicalauthenticator.repository.FirestoreRepository;
import com.example.graphicalauthenticator.ui.auth.EmailLoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean doubleBackToExitPressedOnce = false;
    private final FirestoreRepository repository = new FirestoreRepository();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();

        repository.getUserProfileDocument().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    binding.userName.setText(user.getName());
                    binding.userEmail.setText(email);
                } else {
                    Toast.makeText(MainActivity.this, "Unable to fetch data.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Unable to fetch data.", Toast.LENGTH_SHORT).show();

            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, EmailLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


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