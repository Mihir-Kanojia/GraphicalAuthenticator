package com.example.graphicalauthenticator;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Constants {

    public static String UserAuthID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    public static String CREATE_NEW_SIGNATURE = "CREATE_NEW_SIGNATURE";
    public static String AUTH_SIGNATURE = "AUTH_SIGNATURE";
}
