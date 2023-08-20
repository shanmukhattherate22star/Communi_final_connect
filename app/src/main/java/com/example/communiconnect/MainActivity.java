package com.example.communiconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
   // private Button logout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    public String email;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent != null) {
            User user = (User) intent.getSerializableExtra("user");
            if (user != null) {
                String username = user.getUsername();
                email = user.getEmail();
              //  Toast.makeText(MainActivity.this, username+", "+email, Toast.LENGTH_SHORT).show();

            }
        }


//        logout = findViewById(R.id.logout);
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(MainActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, logreg.class));
//            }
//        });

        drawerLayout=findViewById(R.id.drawer_layout);


        navigationView=findViewById(R.id.nav_view);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.map) {
                    Toast.makeText(MainActivity.this, "Map Selected", Toast.LENGTH_SHORT).show();

                    User user = new User("exampleUsername", email);

                    Intent intent = new Intent(MainActivity.this, MakingandSeeingMarker.class);
                   intent.putExtra("user", user);

                    startActivity(intent);
                } else if (itemId == R.id.alert) {
                    Toast.makeText(MainActivity.this, "alerts Selected", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.groups) {
                    Toast.makeText(MainActivity.this, "groups Selected", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.logoutof) {
                    Toast.makeText(MainActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(MainActivity.this, logreg.class));

                }
                else if (itemId == R.id.posts) {
                    Toast.makeText(MainActivity.this, "Opening Posts", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, userlists.class));
                }
                else if (itemId == R.id.about) {
                    Toast.makeText(MainActivity.this, "about selected", Toast.LENGTH_SHORT).show();
                    User user = new User("exampleUsername", email);
                    Intent intentt = new Intent(MainActivity.this, userlists.class);
                    intentt.putExtra("user", user);
                    startActivity(intentt);


                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
        super.onBackPressed();
    }



}