package com.example.weighstable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.*;

import com.example.weighstable.household.Household;
import com.example.weighstable.util.DeviceReadWrite;

import java.io.IOException;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CalendarView calendarView;
    TextView myData;
    private TextView txtView;
    private Spinner mySpinner;
    private String valueFromSpinner;
    private String trash;
    private Household household;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        checkForHousehold();

        mySpinner = findViewById(R.id.spinner);
        String[] days = getResources().getStringArray(R.array.names);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.spinner, days);
        //myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        Button button = (Button) findViewById(R.id.dayButton);

        mySpinner.setOnItemSelectedListener(this);

        Calendar cal = Calendar.getInstance();
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", true);
                switch (trash) {
                    case "Monday":
                        intent.putExtra("rrule", "FREQ=WEEKLY;BYDAY=Mo");
                    break;
                    case "Sunday":
                        intent.putExtra("rrule", "FREQ=WEEKLY;BYDAY=Su");
                        break;
                    case "Tuesday":
                        intent.putExtra("rrule", "FREQ=WEEKLY;BYDAY=Tu");
                        break;
                    case "Wednesday":
                        intent.putExtra("rrule", "FREQ=WEEKLY;BYDAY=We");
                        break;
                    case "Thursday":
                        intent.putExtra("rrule", "FREQ=WEEKLY;BYDAY=Th");
                        break;
                    case "Friday":
                        intent.putExtra("rrule", "FREQ=WEEKLY;BYDAY=Fr");
                        break;
                    case "Saturday":
                        intent.putExtra("rrule", "FREQ=WEEKLY;BYDAY=Sa");
                        break;
                }

                intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
                intent.putExtra("title", "Trashday");
                startActivity(intent);

            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      switch (position) {
          case 0:
                trash = "Sunday";
              break;
          case 1:
              trash = "Monday";
              break;
          case 2:
              trash = "Tuesday";
              break;
          case 3:
              trash = "Wednesday";
              break;
          case 4:
              trash = "Thursday";
              break;
          case 5:
              trash = "Friday";
              break;
          case 6:
              trash = "Saturday";
              break;


      }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
