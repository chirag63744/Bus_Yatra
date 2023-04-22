package com.example.bus_yatra_users;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.bus_yatra_users.databinding.ActivityLocationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityLocationBinding binding;
    Double latitude=78.0,longitude=34.9;
    double pla,prelong;
    List<Marker> markers = new ArrayList<>();
    BitmapDescriptor bitmapDescriptor;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//

        // Add a listener to the "messages" collection in Firestore
        CollectionReference messagesRef = db.collection("messages");

        // Listen for changes to the documents in Firebase
        messagesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }

                // Loop through the documents and add/update markers on the map
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            addMarker(dc.getDocument());
                            break;
                        case MODIFIED:
                            updateMarker(dc.getDocument());
                            break;

                    }
                }
            }
        });
    }

    private void addMarker(DocumentSnapshot document) {
        Double latitude = document.getDouble("latitude");
        Double longitude = document.getDouble("longitude");
        String text = document.getString("text");

        // Create a LatLng object from the latitude and longitude values
        LatLng location = new LatLng(latitude, longitude);

        // Create a new marker and add it to the list of markers
        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(""+latitude+""+longitude).icon(bitmapDescriptor));
        markers.add(marker);
        LatLng markerLocation = marker.getPosition();
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(markerLocation, 15);
        mMap.animateCamera(zoom);


    }

    private void updateMarker(DocumentSnapshot document) {
        mMap.clear();
        Double latitude1 = document.getDouble("latitude");
        Double longitude1 = document.getDouble("longitude");
        String text = document.getString("text");
        LatLng location = new LatLng(latitude1, longitude1);


        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(""+latitude1+""+longitude1).icon(bitmapDescriptor));
        markers.add(marker);
        LatLng markerLocation = marker.getPosition();
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(markerLocation, 15);
        mMap.animateCamera(zoom);

        // Find the marker in the list of markers
//        for (Marker marker : markers) {
//            LatLng position = marker.getPosition();
//            if (position.latitude == latitude && position.longitude == longitude) {
//                // Update the marker's title
//                marker.setTitle(text);
//
//                break;
//            }
//        }


    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        String latitude=getIntent().getStringExtra("lat");
//        String longitude=getIntent().getStringExtra("long");
//        double latitud=Double.valueOf(latitude);
//        double longi=Double.valueOf(longitude);
        mMap = googleMap;

        // Convert the vector drawable to a bitmap
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.baseline_directions_bus_24);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

// Create a BitmapDescriptor from the bitmap
         bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_directions_bus_24);

        // Add a marker in Sydney and move the camera

    }
}