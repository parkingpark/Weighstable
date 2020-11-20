package com.example.weighstable;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
        create_house = (Button) findViewById(R.id.create_house);
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
    }
}