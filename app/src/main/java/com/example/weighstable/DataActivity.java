package com.example.weighstable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

        ImageView menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView menu_view = (ListView) findViewById(R.id.menu_view);
                String[] pages = {"Home", "Household", "Calendar"};
                ArrayAdapter<String> pages_adapter = new ArrayAdapter<String>(DataActivity.this, R.layout.listview, pages);
                menu_view.setAdapter(pages_adapter);
                menu_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                if (menu_view.getVisibility() == View.INVISIBLE){
                    menu_view.setVisibility(View.VISIBLE);
                } else {
                    menu_view.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}