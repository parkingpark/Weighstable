package com.example.weighstable;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

import com.example.weighstable.household.Household;
import com.example.weighstable.util.DeviceReadWrite;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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

    private static final SimpleDateFormat dateTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    Button report;
    FirebaseAuth fAuth;

    private TextView tv;
    private TextView tv2;

    boolean login;
    Object weightData = 0;
    Object capacityData = 0;
    int maxCapacity = 53;
    int minCapacity = 0;
    ParticleDevice device;

    private Household household;
    private Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enabling android cloud apk for particle

        ParticleCloudSDK.init(this);
        final ParticleDevice[] device = new ParticleDevice[1];

        setContentView(R.layout.activity_main);//textView = findViewById(R.id.textView);
        report = findViewById(R.id.iTookOut);
        reportName = findViewById(R.id.reportName);

        login = false;

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        checkForHousehold();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        ImageView nav = (ImageView) findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView nav_view = (ListView) findViewById(R.id.nav_view);
                String[] pages = {"Household", "Calendar", "Data", "Log Activity", "Logout"};
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
                        } else if (selected.equals("Log Activity")) {
                            startActivity(new Intent(MainActivity.this, LogActivity.class));
                        } else if (selected.equals("Logout")) {
                            FirebaseAuth.getInstance().signOut();// logout
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                            startActivity(new Intent(MainActivity.this, Login.class));
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

        EditText weightLimit = (EditText) findViewById(R.id.weight_input);
        EditText capLimit = (EditText) findViewById(R.id.capacity_input);

        TextWatcher capWatch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int cLimit = Integer.parseInt(capLimit.getText().toString());
                    household.setcLimit(cLimit);
                } catch (NumberFormatException e) {

                }
            }
        };

        TextWatcher weightWatch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int wLimit = Integer.parseInt(weightLimit.getText().toString());
                    household.setwLimit(wLimit);
                } catch (NumberFormatException e) {

                }
            }
        };

        if (household != null) {
            weightLimit.setText(String.valueOf(household.getwLimit()));
            capLimit.setText(String.valueOf(household.getcLimit()));
        }

        capLimit.addTextChangedListener(capWatch);
        weightLimit.addTextChangedListener(weightWatch);


        //findViewById(R.id.refresh_button).setOnClickListener(v -> {
        //...
        // Do network work on background thread
//        Timer login_timer = new Timer();
//        TimerTask login_pro = new TimerTask() {
//            @Override
//            public void run() {
//                Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
//                    @Override
//                    public Object callApi(@NonNull ParticleCloud ParticleCloud) throws ParticleCloudException, IOException {
////                        if( login = false ){
//                        ParticleCloud.logIn("ccarpenter6000@gmail.com", "4155Password!");
//
//                        device[0] = ParticleCloud.getDevice("e00fce6879ba0853f09d4af2");
////                        }
////                        login_timer.cancel();
////                        login_timer.purge();
//                        return null;
//                    }
//
//                    @Override
//                    public void onSuccess(Object o) {
//
//                    }
//
//                    @Override
//                    public void onFailure(ParticleCloudException exception) {
//
//                    }
//                });
//
//            }


//        };login_timer.schedule(login_pro, 0, 1000);


        Timer sensor_timer = new Timer();
        TimerTask task_process = new TimerTask() {
            @Override
            public void run() {
                final ParticleDevice[] device = new ParticleDevice[1];

                Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
                    @Override
                    public Object callApi(@NonNull ParticleCloud ParticleCloud) throws ParticleCloudException, IOException {
////                        if( login = false ){
//                            ParticleCloud.logIn("ccarpenter6000@gmail.com", "4155Password!");
//
                        device[0] = ParticleCloud.getDevice("e00fce6879ba0853f09d4af2");
////                        }
                        //get weight
                        Object weight;
                        try {
                            weight = device[0].getVariable("weight");
                            weightData = weight;
                        } catch (ParticleDevice.VariableDoesNotExistException e) {
                            Toaster.l(MainActivity.this, e.getMessage());
                            weight = -1;
                        }

                        //get capacity
                        Object capacity;
                        try {
                            capacity = device[0].getVariable("capacity");
                            capacityData = capacity;
                        } catch (ParticleDevice.VariableDoesNotExistException e) {
                            Toaster.l(MainActivity.this, e.getMessage());
                            capacity = -1;

                        }
                        return weight;
                    }

                    @Override
                    public void onSuccess(@NonNull Object i) { // this goes on the main thread
                        double capPer = ((maxCapacity - (Double) capacityData) / maxCapacity) * 100;
                        capPer = (int) capPer;
                        String cap = String.valueOf(capPer);
                        tv.setText(i.toString());
                        tv2.setText(cap + "%");

//                        tv2.setText(capacityData.toString());

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
        Timer limitTimer = new Timer();
        TimerTask limtTask = new TimerTask() {
            @Override
            public void run() {

                if (Double.parseDouble(weightData.toString()) >= household.getwLimit() || Double.parseDouble(capacityData.toString()) >= household.getcLimit()) {
                    addNotification();
                }
            }
        };
        limtTask.run();
        limitTimer.schedule(limtTask, 0, 300000);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameData = reportName.getText().toString();
                String val = weightData.toString();
                double wD = Double.valueOf(val);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String timeData = dateTime.format(timestamp);

                DocumentReference documentReference = db.collection("takeout").document();
                Map<String, Object> input = new HashMap<>();

                input.put(KEY_USER, nameData);
                input.put(KEY_WEIGHT, wD);
                //input.put(KEY_WEIGHT, 12.0);
                input.put("timestamp", timeData);

                household.rotate();

                documentReference.set(input)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Input Saved", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Success");
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
        TextView trashDayView = (TextView) findViewById(R.id.day);
        if (household.getTrashDay() != null) {
            trashDayView.setText(household.getTrashDay());
        }
    }

    private void MySyncTasks() {


        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud ParticleCloud) throws ParticleCloudException, IOException {
                ParticleCloudSDK.getCloud().logIn("ccarpenter6000@gmail.com", "4155Password!");

                ParticleDevice device = ParticleCloud.getDevice("e00fce6879ba0853f09d4af2");
                return -1;

            }

            @Override
            public void onSuccess(Object value) {
                Toaster.s(MainActivity.this, "Logged in");
                login = true;
            }

            @Override
            public void onFailure(ParticleCloudException e) {
                Toaster.l(MainActivity.this, "Wrong credentials or no internet connectivity, please try again");
                login = true;
            }
        });


    }

    protected void checkForHousehold() {
        try {
            household = DeviceReadWrite.readHousehold(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (household != null) {
            try {
                DeviceReadWrite.writeHousehold(household, getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notification");
        builder.setContentTitle("Weighstable");
        if (household.getPeople() != null) {
            builder.setContentText("Your trash is full! Its " + household.getPeople()[0] + "'s turn.");

        } else {
            builder.setContentText("Your trash is full!");
        }
        builder.setSmallIcon(R.drawable.w);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}