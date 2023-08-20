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

public class Markinginfo extends AppCompatActivity {
    private EditText titlee;


    private EditText descriptione;

  private Button buttonformark;
    private DatabaseReference mRootRef;

    private FirebaseAuth auth;

    public String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markinginfo);
        Intent intent = getIntent();
        if (intent != null) {
            User user = (User) intent.getSerializableExtra("user");
            if (user != null) {
                String username = user.getUsername();
                email = user.getEmail();
               // Toast.makeText(Markinginfo.this, username+", "+email, Toast.LENGTH_SHORT).show();

            }
        }
        buttonformark = findViewById(R.id.buttonformark);
//        mRootRef = FirebaseDatabase.getInstance().getReference();
//
//        auth = FirebaseAuth.getInstance();

        buttonformark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titlee = findViewById(R.id.title);
                descriptione = findViewById(R.id.description);
                String titles = titlee.getText().toString();
                String descriptions = descriptione.getText().toString();

                if(TextUtils.isEmpty(titles) || TextUtils.isEmpty(descriptions) ){
                    Toast.makeText(Markinginfo.this, "Empty information ", Toast.LENGTH_SHORT).show();
                }
                else{
                    User user = new User("exampleUsername", email);

                    Intent intent = new Intent(Markinginfo.this, Marking.class);
                    user.addlocationinfo(0.0,0.0,titles,descriptions);
                    intent.putExtra("user", user);
                  //  Toast.makeText(Markinginfo.this, "in the else  "+email, Toast.LENGTH_SHORT).show();

                    startActivity(intent);
                }
            }
        });

    }



    }

