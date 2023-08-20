package com.example.communiconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MakingandSeeingMarker extends AppCompatActivity {

    Button viewMap;
    Button viewMarkerButton;
    public String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makingand_seeing_marker);
        viewMap = findViewById(R.id.viewMap);
        Intent intent = getIntent();
        if (intent != null) {
            User user = (User) intent.getSerializableExtra("user");
            if (user != null) {
                String username = user.getUsername();
                email = user.getEmail();
               //  Toast.makeText(MakingandSeeingMarker.this, username+", "+email, Toast.LENGTH_SHORT).show();

            }
        }
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MakingandSeeingMarker.this, "going to viewmap", Toast.LENGTH_SHORT).show();
                User user = new User("exampleUsername", email);

                Intent intent = new Intent(MakingandSeeingMarker.this, Markinginfo.class);
               intent.putExtra("user", user);

                startActivity(intent);

            }
        });
        Button viewMarkerButton = findViewById(R.id.viewMarker);
        viewMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(MakingandSeeingMarker.this, "going to viewmap", Toast.LENGTH_SHORT).show();
                User user = new User("exampleUsername", email);

                Intent intent = new Intent(MakingandSeeingMarker.this, SeeingMarkers.class);
                intent.putExtra("user", user);

                startActivity(intent);
            }
        });
    }
}