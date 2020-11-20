package com.example.weighstable;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import static io.particle.android.sdk.cloud.ParticleCloudSDK.*;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

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


    private static final String ARG_VALUE = "ARG_VALUE";
    private static final String ARG_DEVICEID = "e00fce6879ba0853f09d4af2";

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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Spinner spinner = (Spinner) findViewById(R.id.trashDayInput);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.trashDayArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        /*int val =  Sensor.getmInstanceActivity().getFinalWeight();

        weight.setText(String.valueOf(val));
*/






            tv = findViewById(R.id.displayWeight);
            tv.setText(String.valueOf(getIntent().getIntExtra(ARG_VALUE, 0)));

            tv2 = findViewById(R.id.displaycapacity);
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
                                capacityData =  capacity;
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

        }



    }
