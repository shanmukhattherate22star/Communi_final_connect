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

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText postalCodeEditText;
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
        postalCodeEditText = findViewById(R.id.postalcode);
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
                String postalCode = postalCodeEditText.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_username, txt_email, txt_password, postalCode);
                }
            }
        });
    }

    private void registerUser(String username, String email, String password, String postalCode) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("username", username);
                userMap.put("email", email);
                userMap.put("postalcode", postalCode);

                ArrayList<HashMap<String, Object>> emptyMarksList = new ArrayList<>();
                HashMap<String, Object> emptyMark = new HashMap<>();
                emptyMark.put("latitude", 0.0);
                emptyMark.put("longitude", 0.0);
                emptyMark.put("title", "random title");
                emptyMark.put("description", "random des");
                emptyMarksList.add(emptyMark);

                userMap.put("marks", emptyMarksList);

                userMap.put("id", auth.getCurrentUser().getUid());

                DatabaseReference userRef = mRootRef.child("Users").child(auth.getCurrentUser().getUid());
                userRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addUserToPostalCodeGroup(postalCode, auth.getCurrentUser().getUid());

                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                            User user = new User(username, email);

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

    private void addUserToPostalCodeGroup(String postalCode, String userId) {
        DatabaseReference groupsRef = mRootRef.child("groups").child(postalCode);
        groupsRef.child(userId).setValue(true);
    }
}
