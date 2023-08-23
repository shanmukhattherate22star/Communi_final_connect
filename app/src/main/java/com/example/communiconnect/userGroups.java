package com.example.communiconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userGroups extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Users> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlists);
        recyclerView = findViewById(R.id.userlists);
        database = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);
        Intent intent = getIntent();
        User userr = (User) intent.getSerializableExtra("user");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users user = dataSnapshot.getValue(Users.class);
                    String id_ = dataSnapshot.child("id").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);

                    user.setEmail(email);
                  //  user.setId(id_);
                    user.setUsername(username);
                    String emaill="";
                    if(userr!=null) {
                        emaill = userr.getEmail();
                    }



                    if (userr != null) {
                        //  Toast.makeText(getApplicationContext(),email, Toast.LENGTH_SHORT).show();

                        // String usernamee = userr.getUsername();

                        if(email.equals(emaill)){
                            //Toast.makeText(getApplicationContext(),"hii", Toast.LENGTH_SHORT).show();

                            list.add(user);
                            break;
                        }
                        //  Toast.makeText(MakingandSeeingMarker.this, username+", "+email, Toast.LENGTH_SHORT).show();

                    }


                    else{
                        list.add(user);
                    }


                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}