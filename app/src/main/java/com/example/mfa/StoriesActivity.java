package com.example.mfa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class StoriesActivity extends AppCompatActivity {

    Button upload;
    EditText problemTitle, problemDescription, problemPrice, problemDetail;
    ImageView problemImage;
    Uri imageUri;
    FirebaseStorage storage;
    String downloadUri;
    StorageReference storageReference;
    ProgressDialog pd;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = auth.getCurrentUser();
    String userid = firebaseUser.getUid();
    DocumentReference docRef = db.collection("Users").document(userid);
    final String randomKey = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Sections, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        problemTitle = findViewById(R.id.problemTitle);
        problemDescription = findViewById(R.id.problemDescription);
        problemPrice = findViewById(R.id.problemPrice);
        problemDetail = findViewById(R.id.problemDetail);
        problemImage = findViewById(R.id.problemImage);
        upload = findViewById(R.id.upload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        problemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(StoriesActivity.this);
                pd.setMessage("Uploading...");
                pd.setCanceledOnTouchOutside(false);
                pd.show();

                String imageChecker = downloadUri;
                String problemType = spinner.getSelectedItem().toString();

                String str_pTitle = problemTitle.getText().toString();
                String str_pDesc = problemDescription.getText().toString();
                String str_pPrice = problemPrice.getText().toString();
                String str_pDetail = problemDetail.getText().toString();

                if (TextUtils.isEmpty(str_pTitle) || TextUtils.isEmpty(str_pDetail)
                        || TextUtils.isEmpty(str_pDesc) || TextUtils.isEmpty(str_pPrice)) {
                    pd.dismiss();
                    Toast.makeText(StoriesActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else if (imageChecker == null) {
                    pd.dismiss();
                    Toast.makeText(StoriesActivity.this, "Please upload an image first", Toast.LENGTH_SHORT).show();
                } else {
                    savetoDatabase(str_pTitle, problemType, str_pDesc, str_pPrice, str_pDetail, downloadUri);
                }
            }
        });
    }

    private void savetoDatabase(String str_pTitle, String problemType, String str_pDesc, String str_pPrice, String str_pDetail, String downloadUri) {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String ID = document.getString("User ID");


                        Map<String, Object> problem = new HashMap<>();
                        problem.put("problemTitle", str_pTitle);
                        problem.put("problemDesc", str_pDesc);
                        problem.put("problemPrice", str_pPrice);
                        problem.put("problemDetail", str_pDetail);
                        problem.put("problemImage", downloadUri);

                        if (problemType.equalsIgnoreCase("Education")){

                            db.collection("Education").add(problem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                                    pd.dismiss();
                                    Toast.makeText(StoriesActivity.this, "Successfully uploaded your problem", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(StoriesActivity.this, "Error uploading your problem", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else  if (problemType.equalsIgnoreCase("Business")){

                            db.collection("Business").add(problem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                                    pd.dismiss();
                                    Toast.makeText(StoriesActivity.this, "Successfully uploaded your problem", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(StoriesActivity.this, "Error uploading your problem", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else if (problemType.equalsIgnoreCase("Orphan")){

                            db.collection("Orphan").add(problem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                                    pd.dismiss();
                                    Toast.makeText(StoriesActivity.this, "Successfully uploaded your problem", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(StoriesActivity.this, "Error uploading your problem", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else  if (problemType.equalsIgnoreCase("Medical")){

                            db.collection("Medical").add(problem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                                    pd.dismiss();
                                    Toast.makeText(StoriesActivity.this, "Successfully uploaded your problem", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(StoriesActivity.this, "Error uploading your problem", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    } else {
                        Toast.makeText(StoriesActivity.this, "Cannot find your ID", Toast.LENGTH_SHORT).show();


                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Toast.makeText(StoriesActivity.this, "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            problemImage.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        // Create a reference to "ProductImage.jpg"
        StorageReference problemImage = storageReference.child("Problem images");

        // Create a reference to 'Product Images'
        StorageReference problemImageRef = storageReference.child("Problem images/" + randomKey);

        // While the file names are the same, the references point to different files
        problemImage.getName().equals(problemImageRef.getName());    // true
        problemImage.getPath().equals(problemImageRef.getPath());    // false

        UploadTask uploadTask = problemImageRef.putFile(imageUri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                // Handle unsuccessful uploads
                pd.dismiss();
                Toast.makeText(StoriesActivity.this, "Failed to upload, try again", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                pd.dismiss();
                Snackbar.make(findViewById(R.id.content3), "Image uploaded", Snackbar.LENGTH_LONG).show();

                problemImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downloadUri = task.getResult().toString();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StoriesActivity.this, "Failed to get URL", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });

    }
}