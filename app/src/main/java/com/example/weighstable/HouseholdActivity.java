package com.example.weighstable;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.weighstable.household.Household;

public class HouseholdActivity extends AppCompatActivity {

    Button create_house;
    Household household;
    ScrollView household_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household);
        create_house = (Button) findViewById(R.id.create_household);
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
                EditText names = (EditText) findViewById(R.id.editNames);
                String[] n = names.getText().toString().split(",");
                household_form.setVisibility(View.GONE);
                ListView household_view = (ListView) findViewById(R.id.household_view);
                ArrayAdapter<String> names_adapter = new ArrayAdapter<String>(HouseholdActivity.this, R.layout.listview, n);
                household_view.setAdapter(names_adapter);
                household_view.setVisibility(View.VISIBLE);
            }
        });

        ImageView menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView menu_view = (ListView) findViewById(R.id.menu_view);
                String[] pages = {"Home", "Calendar", "Data"};
                ArrayAdapter<String> pages_adapter = new ArrayAdapter<String>(HouseholdActivity.this, R.layout.listview, pages);
                menu_view.setAdapter(pages_adapter);
                menu_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                if (menu_view.getVisibility() == View.INVISIBLE){
                    menu_view.setVisibility(View.VISIBLE);
                } else {
                    menu_view.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}