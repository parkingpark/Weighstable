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

import java.util.HashMap;
import java.util.Map;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

public class MainActivity extends AppCompatActivity {

    private static String ip = "weighstable.csvpwirajhmg.us-east-2.rds.amazonaws.com";
    private static String port = "3306";
    private static String classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "weighstable";
    private static String username = "admin";
    private static String password = "Group14Admin";
    private static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;
    private Connection connection = null;

    private TextView weight;

    private static final String TAG = "MainActivity";
    private static final String KEY_USER = "user";
    private static final String KEY_WEIGHT = "weight";
    private EditText reportName;
    private FirebaseFirestore db;

    private static final String ARG_VALUE = "ARG_VALUE";
    private static final String ARG_DEVICEID = "e00fce6879ba0853f09d4af2";

    Button report;
    FirebaseAuth fAuth;


    private TextView tv;
    private TextView tv2;

    Object weightData = 0;
    Object capacityData = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enabling android cloud apk for particle

        ParticleCloudSDK.init(this);

        setContentView(R.layout.activity_main);//textView = findViewById(R.id.textView);
        report = findViewById(R.id.iTookOut);
        reportName = findViewById(R.id.reportName);

        db = FirebaseFirestore.getInstance();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageView nav = (ImageView) findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView nav_view = (ListView) findViewById(R.id.nav_view);
                String[] pages = {"Household", "Calendar", "Data"};
                ArrayAdapter<String> pages_adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.listview, pages);
                nav_view.setAdapter(pages_adapter);
                nav_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        if (selected.equals("Household")) {
                            startActivity(new Intent(MainActivity.this, HouseholdActivity.class));
                        } else if (selected.equals("Calendar")) {
                            startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                        } else if (selected.equals("Data")) {
                            startActivity(new Intent(MainActivity.this, DataActivity.class));
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

         /*int val =  Sensor.getInstanceActivity().getFinalWeight();
        weight.setText(String.valueOf(val));
*/

        tv = findViewById(R.id.display_weight);
        tv.setText(String.valueOf(getIntent().getIntExtra(ARG_VALUE, 0)));

        tv2 = findViewById(R.id.display_capacity);
        tv2.setText(String.valueOf(getIntent().getIntExtra(ARG_VALUE, 0)));


        //findViewById(R.id.refresh_button).setOnClickListener(v -> {
        //...
        // Do network work on background thread
        Timer sensor_timer = new Timer();
        TimerTask task_process = new TimerTask() {
            @Override
            public void run() {


                Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
                    @Override
                    public Object callApi(@NonNull ParticleCloud ParticleCloud) throws ParticleCloudException, IOException {
                        ParticleCloud.logIn("ccarpenter6000@gmail.com", "4155Password!");

                        ParticleDevice device = ParticleCloud.getDevice("e00fce6879ba0853f09d4af2");
                        //get weight
                        Object weight;
                        try {
                            weight = device.getVariable("weight");
                            weightData = weight;
                        } catch (ParticleDevice.VariableDoesNotExistException e) {
                            Toaster.l(MainActivity.this, e.getMessage());
                            weight = -1;
                        }

                        //get capacity
                        Object capacity;
                        try {
                            capacity = device.getVariable("capacity");
                            capacityData = capacity;
                        } catch (ParticleDevice.VariableDoesNotExistException e) {
                            Toaster.l(MainActivity.this, e.getMessage());
                            capacity = -1;

                        }
                        return weight;
                    }

                    @Override
                    public void onSuccess(@NonNull Object i) { // this goes on the main thread
                        tv.setText(i.toString());
                        tv2.setText(capacityData.toString());

                    }

                    @Override
                    public void onFailure(@NonNull ParticleCloudException e) {
                        e.printStackTrace();
                    }
                });
                // });
            }

            public Intent buildIntent(Context ctx, Integer value, String deviceId) {
                Intent intent = new Intent(ctx, Sensor.class);
                intent.putExtra(ARG_VALUE, value);
                intent.putExtra(ARG_DEVICEID, deviceId);

                return intent;
            }
        };
        sensor_timer.schedule(task_process, 0, 1000);


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameData = reportName.getText().toString();
                String val = weightData.toString();
                double wD = Double.valueOf(val);


                DocumentReference documentReference = db.collection("takeout").document();
                Map<String, Object> input = new HashMap<>();
                input.put(KEY_USER, nameData);
                //input.put(KEY_WEIGHT, wD);
                input.put(KEY_WEIGHT, 12.0);

                documentReference.set(input)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Input Saved", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "reeeeeeeee");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
            }
        });
    }
}