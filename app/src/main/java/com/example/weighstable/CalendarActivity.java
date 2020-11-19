package com.example.weighstable;

import androidx.appcompat.app.AppCompatActivity;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

<<<<<<< HEAD
        mySpinner = (Spinner) findViewById(R.id.spinner);
=======
        mySpinner = findViewById(R.id.spinner1);
>>>>>>> 2cbf754077c963a546d092df9c881fa08ad301e9
        String[] days = getResources().getStringArray(R.array.names);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, days);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        String trashDay = (String) mySpinner.getSelectedItem();

         //txtView = (TextView)findViewById(R.id.textView3);
         mySpinner.setOnItemSelectedListener(this);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner1) {
            String valueFromSpinner = parent.getItemAtPosition(position).toString();
            txtView.setText(valueFromSpinner);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
