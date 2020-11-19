package com.example.weighstable;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import static io.particle.android.sdk.cloud.ParticleCloudSDK.*;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private static String ip = "weighstable.csvpwirajhmg.us-east-2.rds.amazonaws.com";
    private static String port = "3306";
    private static String classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "weighstable";
    private static String username = "admin";
    private static String password = "Group14Admin";
    private static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;
    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enabling android cloud apk for particle

        //ParticleCloudSDK.init(this);

        setContentView(R.layout.activity_main);//textView = findViewById(R.id.textView);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



    }
}