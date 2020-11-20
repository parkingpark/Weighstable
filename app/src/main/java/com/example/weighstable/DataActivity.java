package com.example.weighstable;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class DataActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_data);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button check_server = (Button) findViewById(R.id.check_server);
        TextView textview = (TextView) findViewById(R.id.textView);

        try {
            Class.forName(classes);
            connection = DriverManager.getConnection(url, username, password);
            textview.setText("SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            textview.setText("ERROR");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            textview.setText("FAILURE");
        }

        check_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connection!=null) {
                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("Select * from TEST_TABLE;");
                        while (resultSet.next()) {
                            textview.setText(resultSet.getString(1));
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else {
                    textview.setText("Connection is null");
                }
            }
        });
    }
}