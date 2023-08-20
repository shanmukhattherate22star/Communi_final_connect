package com.example.communiconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;

    private EditText Postal_code;
    private EditText email;
    private EditText password;
    private Button register;
    private DatabaseReference mRootRef;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        Postal_code=findViewById(R.id.postalcode);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String postal_code=Postal_code.getText().toString();
                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Empty credentials ", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length()<6) {
                    Toast.makeText(RegisterActivity.this, "password is short ", Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser(txt_username, txt_email, txt_password,postal_code);
                }
            }
        });

    }

    private void registerUser(String username, String email, String password,String postal_code) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("username", username);
                map.put("email", email);
                map.put("postalcode", postal_code);



                ArrayList<HashMap<String, Object>> emptyMarksList = new ArrayList<>();
                HashMap<String, Object> emptyMark = new HashMap<>();
                emptyMark.put("latitude",0.0);
                emptyMark.put("longitude",0.0);
                emptyMark.put("title","random title");
                emptyMark.put("description","random des");
                emptyMarksList.add(emptyMark);

                map.put("marks", emptyMarksList);

                map.put("id", auth.getCurrentUser().getUid());

                mRootRef.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_SHORT).show();


                            User user = new User("exampleUsername", email);

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}