package com.example.weighstable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.*;

import com.example.weighstable.household.Household;

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

        ImageView menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView menu_view = (ListView) findViewById(R.id.menu_view);
                String[] pages = {"Home", "Household", "Data"};
                ArrayAdapter<String> pages_adapter = new ArrayAdapter<String>(CalendarActivity.this, R.layout.listview, pages);
                menu_view.setAdapter(pages_adapter);
                menu_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                if (menu_view.getVisibility() == View.INVISIBLE){
                    menu_view.setVisibility(View.VISIBLE);
                } else {
                    menu_view.setVisibility(View.INVISIBLE);
                }
            }
        });


        Button yourButton = (Button) findViewById(R.id.homeNav);

        yourButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(CalendarActivity.this, MainActivity.class));
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
