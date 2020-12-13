package com.example.weighstable;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

public class Sensor_test extends AppCompatActivity {

    private static final String ARG_VALUE = "ARG_VALUE";
    private static final String ARG_DEVICEID = "e00fce6879ba0853f09d4af2";

    private TextView tv;
    private TextView tv2;

    Object weightData = 0;
    Object capacityData = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParticleCloudSDK.init(this);

        setContentView(R.layout.activity_value);
        tv = findViewById(R.id.value);
        tv.setText(String.valueOf(getIntent().getIntExtra(ARG_VALUE, 0)));

        tv2 = findViewById(R.id.value2);
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
                            Toaster.l(Sensor_test.this, e.getMessage());
                            weight = -1;
                        }

                        //get capacity
                        Object capacity;
                        try {
                            capacity = device.getVariable("capacity");
                            capacityData =  capacity;
                        } catch (ParticleDevice.VariableDoesNotExistException e) {
                            Toaster.l(Sensor_test.this, e.getMessage());
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
