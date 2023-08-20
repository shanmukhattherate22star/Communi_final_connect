package com.example.communiconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public class SeeingMarkers extends AppCompatActivity {

    private GoogleMap googleMap;
    private double[][] locations;
    private String email;
    private String postalcode;
    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeing_markers);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        Intent intent = getIntent();
        if (intent != null) {
            User user = (User) intent.getSerializableExtra("user");
            if (user != null) {
                String username = user.getUsername();
                email = user.getEmail();
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.maptosee);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.map, mapFragment);
            fragmentTransaction.commit();
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                markLocationsOnMap();
            }
        });
    }

    private void markLocationsOnMap() {
        if (googleMap == null) {
            Toast.makeText(this, "Google Map is not ready yet", Toast.LENGTH_SHORT).show();
            return;
        }

        googleMap.clear();

        Query queryemail = usersRef.orderByChild("email").equalTo(email);
       // Toast.makeText(this, "querying for " + email, Toast.LENGTH_SHORT).show();

        queryemail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                    HashMap<String, Object> userData = (HashMap<String, Object>) userSnapshot.getValue();
                    postalcode = (String) userData.get("postalcode");
                //    Toast.makeText(SeeingMarkers.this, "found postal code" + postalcode, Toast.LENGTH_LONG).show();
                    dataSnapshot.getRef().getParent().removeEventListener(this);
                    Query queryForPostal = usersRef.orderByChild("postalcode").equalTo(postalcode);
                   // Toast.makeText(SeeingMarkers.this, "found postal code "+postalcode, Toast.LENGTH_LONG).show();

                    queryForPostal.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    HashMap<String, Object> userData = (HashMap<String, Object>) userSnapshot.getValue();
                                    ArrayList<HashMap<String, Object>> marksList = (ArrayList<HashMap<String, Object>>) userData.get("marks");
//
                                    for (HashMap<String, Object> mark : marksList) {

                                        Object latitudes = mark.get("latitude");
                                        Object longitudes = mark.get("longitude");
                                        if (latitudes instanceof Double && longitudes instanceof Double) {
                                            Double latitude = (Double) latitudes;
                                            Double longitude = (Double) longitudes;


                                            String title = (String) mark.get("title");
                                            String description = (String) mark.get("description");

                                            LatLng latLng = new LatLng(latitude, longitude);


                                            MarkerOptions markerOptions = new MarkerOptions()
                                                    .position(latLng)
                                                    .title(title)
                                                    .snippet(description);

                                            Marker marker = googleMap.addMarker(markerOptions);
                                        } else {
                                            //Toast.makeText(SeeingMarkers.this, "laatitudes and longitudes are not of double type", Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                }

                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker clickedMarker) {
                                        String markerTitle = clickedMarker.getTitle();
                                        String markerSnippet = clickedMarker.getSnippet();


                                        LatLng markerPosition = clickedMarker.getPosition();
                                        double clickedLat = markerPosition.latitude;
                                        double clickedLng = markerPosition.longitude;

                                        String message = "Title: " + markerTitle + "\n" +
                                                "Description: " + markerSnippet + "\n" +
                                                "Latitude: " + clickedLat + "\n" +
                                                "Longitude: " + clickedLng;

                                        Toast.makeText(SeeingMarkers.this, message, Toast.LENGTH_SHORT).show();
                                        return true;

                                    }
                                });
                            } else {
                                Toast.makeText(SeeingMarkers.this, "Users not found while querying for postalcode", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SeeingMarkers.this, "Database error", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(SeeingMarkers.this, "user not found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




    }




}


