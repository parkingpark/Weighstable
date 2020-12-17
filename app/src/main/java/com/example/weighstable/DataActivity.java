package com.example.weighstable;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataActivity extends AppCompatActivity {

    private static final String TAG = "DataActivity";
    private EditText reportName;
    private FirebaseFirestore db;
    TextView totalTrashWeight;
    TextView trashWeight30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        // weight taken out of all time
        totalTrashWeight = findViewById(R.id.totalTrashWeight);
        // weight from last 30 days
        trashWeight30 = findViewById(R.id.trashWeight30);
        // whos turn is it next?

        db = FirebaseFirestore.getInstance();

        db.collection("takeout")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        ImageView nav = (ImageView) findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView nav_view = (ListView) findViewById(R.id.nav_view);
                String[] pages = {"Home", "Household", "Calendar"};
                ArrayAdapter<String> pages_adapter = new ArrayAdapter<String>(DataActivity.this, R.layout.listview, pages);
                nav_view.setAdapter(pages_adapter);
                nav_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        if (selected.equals("Home")) {
                            startActivity(new Intent(DataActivity.this, MainActivity.class));
                        } else if (selected.equals("Household")) {
                            startActivity(new Intent(DataActivity.this, HouseholdActivity.class));
                        } else if (selected.equals("Calendar")) {
                            startActivity(new Intent(DataActivity.this, CalendarActivity.class));
                        }
                    }
                });
                if (nav_view.getVisibility() == View.INVISIBLE) {
                    nav_view.setVisibility(View.VISIBLE);
                    nav_view.setVisibility(View.VISIBLE);
                } else {
                    nav_view.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}