package com.example.mfa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText firstName, lastName, email, password, cpassword;
    Button register;
    TextView txt_login;

    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setTitle("Saving your details");
                pd.setMessage("Please wait...");
                pd.show();

                String str_firstName = firstName.getText().toString();
                String str_lastName = lastName.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();
                String str_cpassword = cpassword.getText().toString();


                if (TextUtils.isEmpty(str_firstName) || TextUtils.isEmpty(str_lastName) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)
                        || TextUtils.isEmpty(str_cpassword))
                {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else if (!str_password.equals(str_cpassword))
                {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
                else if (str_password.length() < 6) {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Your password must have more than 6 characters.", Toast.LENGTH_SHORT).show();
                }
                else {
                    register(str_lastName, str_firstName, str_email, str_password);
                }
            }
        });

    }

    private void register(final String lastName, final String firstName, final String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            Map<String, Object> User = new HashMap<>();
                            User.put("User ID", userid);
                            User.put("email", email);
                            User.put("Last name", lastName);
                            User.put("First name", firstName);
                            User.put("Password", password);


                            //Adding a new document with the userid as the document id
                            db.collection("Users").document(userid)
                                    .set(User)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this, "Save successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Error saving your details", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "You can't be registered with this email or password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
