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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    private int check;
    private DocumentReference reportRef;
    CollectionReference takeoutRef;
    private ArrayList<TakeoutData> dump = new ArrayList<>();
    Button button;
    TextView totalTrashWeight;
    TextView trashWeight30;
    ArrayAdapter<TakeoutData> adapter;
    private static final SimpleDateFormat dateTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        // weight taken out of all time
        totalTrashWeight = findViewById(R.id.totalTrashWeight);
        // weight from last 30 days
        trashWeight30 = findViewById(R.id.trashWeight30);

        check = 0;

        db = FirebaseFirestore.getInstance();
        button = findViewById(R.id.button);
        takeoutRef = db.collection("takeout");

        adapter = new ArrayAdapter<TakeoutData>(
                this, android.R.layout.simple_list_item_1, new ArrayList<TakeoutData>());

        ImageView nav = (ImageView) findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView nav_view = (ListView) findViewById(R.id.nav_view);
                String[] pages = {"Home", "Household", "Calendar", "Log Activity", "Logout"};
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
                        } else if (selected.equals("Log Activity")) {
                            startActivity(new Intent(DataActivity.this, LogActivity.class));
                        } else if (selected.equals("Logout")) {
                            FirebaseAuth.getInstance().signOut();// logout
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                            startActivity(new Intent(DataActivity.this, Login.class));
                        }
                    }
                });
                if (nav_view.getVisibility() == View.INVISIBLE) {
                    nav_view.setVisibility(View.VISIBLE);
                } else {
                    nav_view.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    public void onRefreshClick(View view) {
        if (check == 0) {
            takeoutRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TakeoutData t = document.toObject(TakeoutData.class);
                                dump.add(t);
                            }
                            adapter.clear();
                            adapter.addAll(dump);
                        }

                    });
            check = 1;
        }

        double totalWeight = 0.0;
        double weight30 = 0.0;

        if (totalWeight == 0.0) {

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String timeData = dateTime.format(timestamp);
            String[] split = timeData.split("\\.");
            Log.d(TAG, "??????????????????????" + split[0] + "!!!!!!!!!!!!!!!" + split[1]);

            for (TakeoutData doota : dump) {
                String[] currentSplit = doota.getTimestamp().split("\\.");
                Log.d(TAG, "??????????????????????" + currentSplit[0] + "!!!!!!!!!!!!!!!" + currentSplit[1]);
                totalWeight += doota.getWeight();
                if (Integer.parseInt(split[0]) == Integer.parseInt(currentSplit[0])) {
                    if (split[1].equals(currentSplit[1])) {
                        weight30 += doota.getWeight();
                    }
                }
            }


            totalTrashWeight.setText(String.valueOf(Double.toString(totalWeight) + "lbs"));
            trashWeight30.setText(String.valueOf(Double.toString(weight30) + "lbs"));
            weight30 = 0.0;
            totalWeight = 0.0;
        }

    }
}


