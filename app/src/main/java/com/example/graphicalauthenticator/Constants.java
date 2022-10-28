package com.example.graphicalauthenticator;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Constants {

    public static String UserAuthID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

}
