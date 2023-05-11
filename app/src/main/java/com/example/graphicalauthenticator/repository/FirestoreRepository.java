package com.example.graphicalauthenticator.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

//import static com.example.graphicalauthenticator.Constants.UserAuthID;

import java.util.Objects;

public class FirestoreRepository {

    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    public FirebaseFirestore getFirestoreDB() {
        return firestoreDB;
    }

    public CollectionReference getUserProfileCollection() {
        return firestoreDB.collection("USERS");
    }

    public DocumentReference getUserProfileDocument(){
        return firestoreDB.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
    }

    public CollectionReference getUserFileCollection(){
        return getUserProfileDocument().collection("FILES");
    }


}
