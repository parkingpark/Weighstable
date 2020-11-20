package com.example.weighstable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weighstable.household.Household;

public class HouseholdActivity extends AppCompatActivity {

    Button create_house;
    Household household;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household);
        create_house = (Button) findViewById(R.id.create_house);

        create_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Household form
            }
        });
    }
}