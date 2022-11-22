package com.example.graphicalauthenticator.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreRepository {

    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    public FirebaseFirestore getFirestoreDB() {
        return firestoreDB;
    }

    public CollectionReference getUserProfileCollection() {
        return firestoreDB.collection("USERS");
    }



}
