package com.example.communiconnect;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Marking extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    FrameLayout map;
    GoogleMap gMap;
    Marker marker;
    SearchView searchView;
    public String email;
    public String titles;
    public String descriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Locationinfo");
        Intent intent = getIntent();
        if (intent != null) {
            User user = (User) intent.getSerializableExtra("user");
            if (user != null) {
                String username = user.getUsername();
                email = user.getEmail();
                titles=user.getTitle();
                descriptions=user.getDescription();
                //  Toast.makeText(MainActivity.this, username+", "+email, Toast.LENGTH_SHORT).show();

            }
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        map = findViewById(R.id.map);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();
        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = query;
                if (loc == null) {
                    Toast.makeText(Marking.this, "Location Not found", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder geocoder = new Geocoder(Marking.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(loc, 1);
                        if (addressList.size() > 0) {
                            LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                            if (marker != null) {
                                marker.remove();
                            }
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(loc);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            gMap.animateCamera(cameraUpdate);
                            marker = gMap.addMarker(markerOptions);


                            double latitude = addressList.get(0).getLatitude();
                            double longitude = addressList.get(0).getLongitude();

                            String toastMessage = "Location: " + loc + "\nLatitude: " + latitude + "\nLongitude: " + longitude+"added by "+email;
                            Toast.makeText(Marking.this, toastMessage, Toast.LENGTH_LONG).show();
                            String userEmail = email;


                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");


                            Query queri = usersRef.orderByChild("email").equalTo(userEmail);

                            queri.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                                        String userUid = userSnapshot.getKey();


                                        HashMap<String, Object> userData = (HashMap<String, Object>) userSnapshot.getValue();


                                        ArrayList<HashMap<String, Object>> marksList = (ArrayList<HashMap<String, Object>>) userData.get("marks");


                                        HashMap<String, Object> newMark = new HashMap<>();
                                        newMark.put("latitude", latitude);
                                        newMark.put("longitude", longitude);
                                        newMark.put("title", titles);
                                        newMark.put("description", descriptions);


                                        marksList.add(newMark);


                                        userData.put("marks", marksList);


                                        usersRef.child(userUid).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(Marking.this, "updated successfullly", Toast.LENGTH_LONG).show();

                                                } else {
                                                    // Handle error
                                                    Toast.makeText(Marking.this, "error with updating", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                                    } else {
                                        // User with the provided email not found
                                        Toast.makeText(Marking.this, "user not found", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle error
                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Marking.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Current Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
            else {

                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}