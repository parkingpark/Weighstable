package com.example.weighstable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CalendarView calendarView;
    TextView myData;
    private TextView txtView;
    private Spinner mySpinner;
    private String valueFromSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mySpinner = findViewById(R.id.spinner);
        String[] days = getResources().getStringArray(R.array.names);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.spinner, days);
        //myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        //String trashDay = (String) mySpinner.getSelectedItem();

        //txtView = (TextView)findViewById(R.id.textView3);
        mySpinner.setOnItemSelectedListener(this);

        ImageView nav = (ImageView) findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView nav_view = (ListView) findViewById(R.id.nav_view);
                String[] pages = {"Home", "Household", "Data"};
                ArrayAdapter<String> pages_adapter = new ArrayAdapter<String>(CalendarActivity.this, R.layout.listview, pages);
                nav_view.setAdapter(pages_adapter);
                nav_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        if (selected.equals("Home")) {
                            startActivity(new Intent(CalendarActivity.this, MainActivity.class));
                        } else if (selected.equals("Household")) {
                            startActivity(new Intent(CalendarActivity.this, HouseholdActivity.class));
                        } else if (selected.equals("Data")) {
                            startActivity(new Intent(CalendarActivity.this, DataActivity.class));
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner) {
            valueFromSpinner = parent.getItemAtPosition(position).toString();
            //txtView.setText(valueFromSpinner);
            TextView day = (TextView) findViewById(R.id.day);
            //day.setText(valueFromSpinner);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
