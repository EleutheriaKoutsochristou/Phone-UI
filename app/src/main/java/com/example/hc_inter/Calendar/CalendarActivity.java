package com.example.hc_inter.Calendar;

import static com.example.hc_inter.Calendar.CalendarUtils.daysInMonthArray;
import static com.example.hc_inter.Calendar.CalendarUtils.monthYearFromDate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.speech.RecognizerIntent;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hc_inter.R;
import com.example.hc_inter.home.HomeFragment;

import java.time.LocalDate;

import java.util.ArrayList;


public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    //initialization of screen buttons and screen texts
    private TextView monthYearText;
    private RecyclerView calendarRecycling;



    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private ImageButton speakbutton;

    private ImageButton infoButton;



    private TextView infoText;

    private ImageButton homeButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //xml connection and initialization of values
        setContentView(R.layout.activity_calendar);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();

        speakbutton = findViewById(R.id.imageButton22);

        infoButton = findViewById(R.id.imageButton5);
        infoText = findViewById(R.id.textView20);
        homeButton = findViewById(R.id.homeButton);

        setMonth();



        //setting the action that takes place when user clicks on the speak button
        speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        //setting the action that takes place when user clicks on the info button
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInfoTextView();
            }
        });








    }
    private void initWidgets() {
        calendarRecycling = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }


    //method that sets up the month to appear on screen
    private void setMonth() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysOfMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysOfMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecycling.setLayoutManager(layoutManager);
        calendarRecycling.setAdapter(calendarAdapter);
    }



    //method to find the previous month on the calendar
    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonth();
    }


    //method to find the previous month on the calendar when voice recognition is used
    public void previousMonthAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonth();
    }


    //method to find the next month on the calendar
    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonth();
    }


    //method to find the next month on the calendar when voice recognition is used
    public void nextMonthAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonth();
    }


    //method that takes the date when a user clicks on a specific date in order to store it in the utility
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null)
        {
            CalendarUtils.selectedDate = date;
            setMonth();
        }
    }

    //making the info text visible based on if the user clicks the info button
    private void toggleInfoTextView() {
        if (infoText.getVisibility() == View.GONE) {
            infoText.setVisibility(View.VISIBLE);
        } else {
            infoText.setVisibility(View.GONE);
        }
    }


    //method that initiates the daily activity when user selects a specific date
    public void DailyAppointment(View v){
        startActivity(new Intent(this, DailyActivity.class));
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el-GR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Μιλήστε μου");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {
                String command = matches.get(0).toLowerCase();
                handleVoiceCommand(command);
            }
        }
    }

    private void handleVoiceCommand(String command) {
        if (command.contains("επόμενος")) {
            nextMonthAction();

        }
        else if(command.contains("προηγούμενος") ){
           previousMonthAction();
        }
        else {
            Toast.makeText(this, "Με συγχωρείτε δεν κατάλαβα με τι θέλετε να σας βοηθήσω. Παρακαλώ ξανά πατήστε το κουμπί και ξανά μιλήστε μου", Toast.LENGTH_SHORT).show();
        }
    }

}




