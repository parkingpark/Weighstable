package com.example.weighstable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weighstable.household.Household;
import com.example.weighstable.util.DeviceReadWrite;

import java.io.IOException;

public class HouseholdActivity extends AppCompatActivity {

    Button create_house;
    Household household;
    ScrollView household_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household);

        checkForHousehold();

        create_house = (Button) findViewById(R.id.create_household);
        ListView household_view = (ListView) findViewById(R.id.household_view);
        if (household != null && household.getPeople() != null) {
            create_house.setVisibility(View.INVISIBLE);
            ArrayAdapter<String> names_adapter = new ArrayAdapter<String>(HouseholdActivity.this, R.layout.listview, household.getPeople());
            household_view.setAdapter(names_adapter);
            household_view.setVisibility(View.VISIBLE);
        }
        household_form = (ScrollView) findViewById(R.id.household_form);

        create_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                household_form.setVisibility(View.VISIBLE);
                create_house.setVisibility(View.GONE);
            }
        });
        Button submit_form = (Button) findViewById(R.id.submit_form);
        submit_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText names = (EditText) findViewById(R.id.edit_names);
                String[] n = names.getText().toString().split(",");
                household.setPeople(n);
                household_form.setVisibility(View.GONE);
                ArrayAdapter<String> names_adapter = new ArrayAdapter<String>(HouseholdActivity.this, R.layout.listview, n);
                household_view.setAdapter(names_adapter);
                household_view.setVisibility(View.VISIBLE);
            }
        });

        ImageView nav = (ImageView) findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView nav_view = (ListView) findViewById(R.id.nav_view);
                String[] pages = {"Home", "Calendar", "Data"};
                ArrayAdapter<String> pages_adapter = new ArrayAdapter<String>(HouseholdActivity.this, R.layout.listview, pages);
                nav_view.setAdapter(pages_adapter);
                nav_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        if (selected.equals("Home")) {
                            startActivity(new Intent(HouseholdActivity.this, MainActivity.class));
                        } else if (selected.equals("Calendar")) {
                            startActivity(new Intent(HouseholdActivity.this, CalendarActivity.class));
                        } else if (selected.equals("Data")) {
                            startActivity(new Intent(HouseholdActivity.this, DataActivity.class));
                        }
                    }
                });
                if (nav_view.getVisibility() == View.INVISIBLE){
                    nav_view.setVisibility(View.VISIBLE);
                } else {
                    nav_view.setVisibility(View.INVISIBLE);
                }
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
}