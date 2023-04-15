package com.example.graphicalauthenticator.ui.filemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.graphicalauthenticator.Constants;
import com.example.graphicalauthenticator.Modal.FileModal;
import com.example.graphicalauthenticator.R;
import com.example.graphicalauthenticator.databinding.ActivityFileDashbaordBinding;
import com.example.graphicalauthenticator.helpers.FilesAdapter;
import com.example.graphicalauthenticator.repository.FirestoreRepository;
import com.example.graphicalauthenticator.ui.auth.EmailLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileDashbaordActivity extends AppCompatActivity {

    private ActivityFileDashbaordBinding binding;
    private List<FileModal> fileModalList = new ArrayList<>();
    private FilesAdapter filesAdapter;
    private final FirestoreRepository repository = new FirestoreRepository();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_file_dashbaord);
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        filesAdapter = new FilesAdapter(fileModalList, FileDashbaordActivity.this);
        binding.rvFiles.setHasFixedSize(false);
        binding.rvFiles.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fetchFilesAndUpdateRvAdapter();


// Define an ActivityResultLauncher to launch the file picker and receive the result
        ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (true) {
                        showConfirmationDialog(uri);
                    }
                }
        );


// Launch the file picker when the user clicks the button
        binding.btnUpload.setOnClickListener(view -> {
            filePickerLauncher.launch("*/*"); // Allow any type of file to be selected
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(FileDashbaordActivity.this, EmailLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        FileModal model = new FileModal("Filename", 24, "pdf", "fileurll.com");
//        fileModalList.add(model);
//        fileModalList.add(model);
//        fileModalList.add(model);
//        model = new FileModal("Filename", 24, "pdaf", "fileurll.com"); fileModalList.add(model);
//        fileModalList.add(model);
//        fileModalList.add(model);
//        fileModalList.add(model);

        binding.rvFiles.setAdapter(filesAdapter);

        filesAdapter.setOnItemClickListener(position -> {
            if (position >= 0) {
                String fileUrl = fileModalList.get(position).url;
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);
                storageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        String fileName = storageMetadata.getName();
                        String mimeType = storageMetadata.getContentType();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(fileUrl), mimeType);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, "Open " + fileName + " with"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }

        });
    } //  onCreate() closed




    private void fetchFilesAndUpdateRvAdapter() {

        repository.getUserFileCollection().orderBy("uploadDateTime", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Check if QuerySnapshot is empty
                            if (task.getResult().isEmpty()) {
                                // Call updateUi() function if collection is empty
                                updateUiForNoFilesFound();
                            } else {
                                // Iterate over QuerySnapshot and add each document to userList
                                fileModalList.clear();
                                for (DocumentSnapshot document : task.getResult()) {
                                    String name = document.getString("name");
                                    long size = document.getLong("size");
                                    String type = document.getString("type");
                                    String url = document.getString("url");
                                    Date upDate = document.getDate("uploadDateTime");


                                    FileModal modal = new FileModal(name, size, type, url);
                                    modal.setUploadDateTime(upDate);
                                    fileModalList.add(modal);

                                }
                                // Do something with userList here, such as display it in a RecyclerView
                                binding.rvFiles.setAdapter(filesAdapter);

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void updateUiForNoFilesFound() {
        //ToDO: Karege
    }


    private void showConfirmationDialog(Uri fileUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Upload");
        builder.setMessage("Are you sure you want to upload this file?");
        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadFile(fileUri);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Cancel, so do nothing
            }
        });
        builder.show();
    }

    private void uploadFile(Uri uri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(Constants.UserFilesPath);

        String fileName = getFileName(uri);
        String fileExtension = getFileExtension(uri);
        long fileSize = getFileSize(uri);
//        StorageReference fileRef = storageRef.child(fileName + "." + fileExtension);
        StorageReference fileRef = storageRef.child(fileName);

        UploadTask uploadTask = fileRef.putFile(uri);

        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                fileRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    FirebaseFirestore.getInstance().collection(repository.getUserFileCollection().getPath())
                            .add(new FileModal(fileName, fileSize, fileExtension, downloadUri.toString()));
                    fetchFilesAndUpdateRvAdapter();
                });
            } else {
                Toast.makeText(this, "Unable to upload file\nTry again", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "uploadFile: Failed | Exception: " + task.getException());
            }
        });
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @SuppressLint("Range")
    private long getFileSize(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        long size = 0;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return size;
    }
}