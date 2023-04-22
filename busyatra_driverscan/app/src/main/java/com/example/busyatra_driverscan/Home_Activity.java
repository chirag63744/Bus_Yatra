package com.example.busyatra_driverscan;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;
import android.Manifest;


// implements onClickListener for the onclick behaviour of button
public class Home_Activity extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;
    Button b3;
    TextView messageText, messageFormat;
    double latitude,longitude;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FusedLocationProviderClient mLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);
        b3=findViewById(R.id.button3);
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            getLastKnownLocation();
        }


        messageText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textContent2);
//


        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String customId = messageFormat.getText().toString();
                if (customId.isEmpty()) {
                } else {
                    Log.d("Location","Changed");
                    Map<String, Object> message = new HashMap<>();
                    message.put("text", messageText.getText().toString());
                    message.put("latitude", location.getLatitude());
                    message.put("longitude", location.getLongitude());

                    db.collection("messages")
                            .document(customId)
                            .set(message)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + customId);
                                    Toast.makeText(Home_Activity.this, "Location updating", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
            }
        };

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // Register the location listener to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        // adding listener to the button
        scanBtn.setOnClickListener(this);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customId = messageFormat.getText().toString();

                Map<String, Object> message = new HashMap<>();
                message.put("text", messageText.getText().toString());
                message.put("latitude", latitude);
                message.put("longitude", longitude);

                db.collection("messages")
                        .document(customId)
                        .set(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + customId);
                                Toast.makeText(Home_Activity.this, "value added to the database", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

            }
        });

    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d(TAG, "Latitude: " + latitude + ", Longitude: " + longitude);
//                    Toast.makeText(MainActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude,
//                            Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "Failed to get location.");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            } else {
                Log.d(TAG, "Permission denied");
            }
        }
    }


    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        String scannedText = intentResult.getContents();
        if (scannedText != null) {
            String[] parts = scannedText.split("\\|");
            if (parts.length == 2) {
                String scannedBustext = parts[0];
                String scannedBusno = parts[1];
                messageText.setText(scannedBustext);
                messageFormat.setText(scannedBusno);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
