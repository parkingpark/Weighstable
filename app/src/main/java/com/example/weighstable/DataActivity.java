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
    private DocumentReference reportRef;
    CollectionReference takeoutRef;
    private double totalWeight = 0;
    private double weight30 = 0;
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

        db = FirebaseFirestore.getInstance();
        button = findViewById(R.id.button);
        takeoutRef = db.collection("takeout");

        adapter = new ArrayAdapter<TakeoutData>(
                this, android.R.layout.simple_list_item_1, new ArrayList<TakeoutData>());

    }


    public void onRefreshClick(View view) {
        takeoutRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<TakeoutData> dump = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            TakeoutData t = document.toObject(TakeoutData.class);
                            dump.add(t);
                        }
                        adapter.clear();
                        adapter.addAll(dump);
                    }

                });

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String timeData = dateTime.format(timestamp);
        String[] split = timeData.split(".");

        for (TakeoutData doota : dump) {
            String currentTimeData = dateTime.format(timestamp);
            String[] currentSplit = currentTimeData.split(".");
            totalWeight += doota.getWeight();
            if(split[0].equals(currentSplit[0])) {
                if(split[1].equals(currentSplit[1])){
                    weight30 += doota.getWeight();
                }
            }
        }

        totalTrashWeight.setText(String.valueOf(totalWeight));
        trashWeight30.setText(String.valueOf(weight30));

    }
}


