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

public class LogActivity extends AppCompatActivity {

    private static final String TAG = "LogActivity";
    private FirebaseFirestore db;
    CollectionReference takeoutRef;
    private DocumentReference reportRef;
    private ArrayList<TakeoutData> dump;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        db = FirebaseFirestore.getInstance();
        button = findViewById(R.id.button);
        takeoutRef = db.collection("takeout");
        dump = new ArrayList<>();


        Query queryTotal = takeoutRef.orderBy("timestamp");
        Map<String, Object> input = new HashMap<>();

        ListView reportListView1 = findViewById(R.id.reportListView);
        ArrayAdapter<TakeoutData> adapter = new ArrayAdapter<TakeoutData>(
                this, android.R.layout.simple_list_item_1, new ArrayList<TakeoutData>());
        TakeoutData test = new TakeoutData("12345", "123", 12.1);
        dump.add(test);
        adapter.addAll(dump);
        reportListView1.setAdapter(adapter); // error line

    }

    public void onRefreshClick(View view) {
        takeoutRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TakeoutData t = document.toObject(TakeoutData.class);
                                dump.add(t);
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }




}
