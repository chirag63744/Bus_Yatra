package com.example.bus_yatra_users;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button b1,b2;
    TextView t1,t2;
    private static ArrayList<Type> mArrayList = new ArrayList<>();
    Double latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.button);
        b2=findViewById(R.id.button2);
        t1=findViewById(R.id.textView);
        t2=findViewById(R.id.textView2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference messagesRef = db.collection("messages");
                messagesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                 latitude = document.getDouble("latitude");
                                 longitude = document.getDouble("longitude");
                                Log.d(TAG, "Latitude: " + latitude);
                                Log.d(TAG, "Longitude: " + longitude);
                                String text = document.getString("text");
                                  t1.setText(""+latitude);
                                  t2.setText(""+longitude);
                                //Toast.makeText(MainActivity.this, ""+latitude, Toast.LENGTH_SHORT).show();

                                // Use the retrieved data as needed
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,Location.class);
                startActivity(i);
            }
        });
//        db.collection("messages")
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
//                        if (documentSnapshots.isEmpty()) {
//                            Log.d(TAG, "onSuccess: LIST EMPTY");
//                            return;
//                        } else {
//                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
//                                if (documentSnapshot.exists()) {
//                                    Log.d(TAG, "onSuccess: DOCUMENT" + documentSnapshot.getId() + " ; " + documentSnapshot.getData());
//                                    DocumentReference documentReference1 = FirebaseFirestore.getInstance().document("some path");
//                                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                            Type type= documentSnapshot.toObject(Type.class);
//                                            Log.d(TAG, "onSuccess: " + type.toString());
//                                            mArrayList.add(type);
//                                            Log.d(TAG, "onSuccess: " + mArrayList);
//                                        /* these logs here display correct data but when
//                                         I log it in onCreate() method it's empty*/
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
//                    }
//                }


    }
}