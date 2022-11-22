package com.example.graphicalauthenticator.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graphicalauthenticator.Constants;
import com.example.graphicalauthenticator.MainActivity;
import com.example.graphicalauthenticator.R;
import com.example.graphicalauthenticator.databinding.ActivityEmailRegistrationBinding;
import com.example.graphicalauthenticator.managers.ActivitySwitchManager;
import com.example.graphicalauthenticator.model.User;
import com.example.graphicalauthenticator.repository.FirestoreRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EmailRegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean doubleBackToExitPressedOnce = false;
    private ActivityEmailRegistrationBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirestoreRepository repository = new FirestoreRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_registration);
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAndCreateUser();
            }
        });


        binding.tvLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EmailRegistrationActivity.this, "Here", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void verifyAndCreateUser() {

        String userName;
        String email = "";
        String password = "";

        userName = Objects.requireNonNull(binding.etUserName.getText()).toString();
        email = Objects.requireNonNull(binding.etEmail.getText()).toString();
        password = Objects.requireNonNull(binding.etPassword.getText()).toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailRegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }

                    private void updateUI(FirebaseUser user) {

//                        FirebaseFirestore db;
//                        Map<String,Object> userObject = new HashMap<>();
//                        userObject.put("uerName",userName);
//                        userObject.put("createDateTime",new Date());
                        User userObj = new User(userName, "", new Date());
                        repository.getFirestoreDB().runTransaction(new Transaction.Function<Void>() {
                            @Nullable
                            @Override
                            public Void apply(@NonNull Transaction transaction) {
                                User userObj = new User(userName, "", new Date());
                                transaction.set(repository.getUserProfileCollection().document(Constants.UserAuthID), userObj);
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EmailRegistrationActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EmailRegistrationActivity.this, "Failure, Try again later", Toast.LENGTH_SHORT).show();

                            }
                        });

                        Toast.makeText(EmailRegistrationActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        new ActivitySwitchManager(EmailRegistrationActivity.this, MainActivity.class).openActivity();

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