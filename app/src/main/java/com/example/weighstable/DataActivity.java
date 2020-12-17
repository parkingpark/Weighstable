package com.example.weighstable;

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
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        ImageView nav = (ImageView) findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView nav_view = (ListView) findViewById(R.id.nav_view);
                String[] pages = {"Home", "Household", "Calendar"};
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
                        }
                    }
                });
                if (nav_view.getVisibility() == View.INVISIBLE){
                    nav_view.setVisibility(View.VISIBLE);
                    nav_view.setVisibility(View.VISIBLE);
                } else {
                    nav_view.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}