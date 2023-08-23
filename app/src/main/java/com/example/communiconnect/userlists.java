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
import java.util.HashMap;

public class userlists extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database,database_groups,database_postalcode,database_gos;
    MyAdapter myAdapter;
    //  MyAdapter myAdapterr;
    ArrayList<Users> list;
    // ArrayList<Users> listt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlists);
        recyclerView = findViewById(R.id.userlists);
        database = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        //  listt = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        // myAdapterr = new MyAdapter(this,listt);
        recyclerView.setAdapter(myAdapter);
        //  recyclerView.setAdapter(myAdapterr);
        Intent intent = getIntent();
        User userr = (User) intent.getSerializableExtra("user");

        Intent intent_ = getIntent();
        User userr_ = (User) intent.getSerializableExtra("userGroups");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users user = dataSnapshot.getValue(Users.class);
                    HashMap<String, Object> userData = (HashMap<String, Object>) dataSnapshot.getValue();
                    ArrayList<HashMap<String, Object>> marksList = (ArrayList<HashMap<String, Object>>) userData.get("marks");
                    String currtitle=null;String currdescription=null;
                    if (marksList.size()>1) {
                        HashMap<String, Object> lastMark = marksList.get(marksList.size() - 1);
                        currtitle = (String) lastMark.get("title");
                        currdescription = (String) lastMark.get("description");
                    }
                    //  String id_ = dataSnapshot.child("id").getValue(String.class);

                    // user.setId(id_);
                    user.setUsername("DESCRIPTION - "+currdescription);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String postalcode=dataSnapshot.child("postalcode").getValue(String.class);
                    user.setEmail("TITLE - "+ currtitle +" - posted by "+email);
                    String emaill="";
                    if(userr!=null) {
                        emaill = userr.getEmail();
                    }

                    int x=0;

                    if (userr != null) {
                        //  Toast.makeText(getApplicationContext(),email, Toast.LENGTH_SHORT).show();

                        // String usernamee = userr.getUsername();

                        if(email.equals(emaill)){
                            //Toast.makeText(getApplicationContext(),"hii", Toast.LENGTH_SHORT).show();
                            user.setEmail("EMAIL - "+emaill);
                            //  userrr.setId(groupSnashot.child("id").getValue(String.class));
                            user.setUsername("USERNAME - "+username);

                            list.add(user);
                            break;
                        }
                        //  Toast.makeText(MakingandSeeingMarker.this, username+", "+email, Toast.LENGTH_SHORT).show();

                    }
                    else if(userr_!=null){
                        emaill = userr_.getEmail();

                        if (userr_ != null) {

                            if(email.equals(emaill)){
                                database_groups = FirebaseDatabase.getInstance().getReference("groups");
                                database_groups
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot groupSnapshot) {
                                                for (DataSnapshot groupDataSnapshot : groupSnapshot.getChildren()) {
                                                    String groupId = groupDataSnapshot.getKey();
                                                    // Toast.makeText(getApplicationContext(),groupId, Toast.LENGTH_SHORT).show();
                                                    // Do something with the groupId, like adding it to a list
                                                    if(groupId.equals(postalcode)){
                                                        //  Toast.makeText(getApplicationContext(),postalcode, Toast.LENGTH_SHORT).show();
                                                        database_postalcode = database_groups.child(postalcode);
                                                        database_postalcode
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot groupSnapshot) {
                                                                        for (DataSnapshot gDataSnapshot : groupSnapshot.getChildren()) {
                                                                            String groupIdd = gDataSnapshot.getKey();
                                                                            //   Toast.makeText(getApplicationContext(),groupIdd, Toast.LENGTH_SHORT).show();

                                                                            database_gos = FirebaseDatabase.getInstance().getReference("Users");

                                                                            database_gos
                                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {

                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot groupSnapshot) {
                                                                                            for (DataSnapshot groupSnashot : groupSnapshot.getChildren()) {
                                                                                                String groupId = groupSnashot.getKey();
                                                                                                if(groupId.equals(groupIdd)){
                                                                                                    Users userrr = groupSnashot.getValue(Users.class);
                                                                                                    userrr.setEmail("EMAIL - "+groupSnashot.child("email").getValue(String.class));
                                                                                                    //  userrr.setId(groupSnashot.child("id").getValue(String.class));
                                                                                                    userrr.setUsername("USERNAME - "+groupSnashot.child("username").getValue(String.class));

                                                                                                    list.add(userrr);
                                                                                                    Toast.makeText(getApplicationContext(),groupSnashot.child("email").getValue(String.class), Toast.LENGTH_SHORT).show();
                                                                                                    //groupSnashot.child("username").getValue(String.class)

                                                                                                }
                                                                                                //

                                                                                            }
                                                                                            myAdapter.notifyDataSetChanged();
                                                                                            //  myAdapterr.notifyDataSetChanged();

                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                                                            // Handle error if needed
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                        // Handle error if needed
                                                                    }
                                                                });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle error if needed
                                            }
                                        });



                                //  Toast.makeText(getApplicationContext(),postalcode, Toast.LENGTH_SHORT).show();

                            }

                        }
                        myAdapter.notifyDataSetChanged();
                        //      myAdapterr.notifyDataSetChanged();

                    }


                    else{
                        if(currdescription!=null && currtitle!=null){     list.add(user);}
                    }


                }
                myAdapter.notifyDataSetChanged();
                //   myAdapterr.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}