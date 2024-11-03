package com.example.hc_inter.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.hc_inter.R;
import com.example.hc_inter.home.HomeFragment;

import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private Button buttonTime;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private ImageButton speakbutton;

    private ImageButton infoButton;

    private TextView infoText;

    private ImageButton homeButton;

    //private ImageButton backButton;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        buttonTime = findViewById(R.id.idBtnPickTime);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        speakbutton = findViewById(R.id.imageButton22);

        infoButton = findViewById(R.id.imageButton5);
        infoText = findViewById(R.id.textView20);

        homeButton = findViewById(R.id.imageButton3);
        //backButton = findViewById(R.id.backButtonEdit);

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventEditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                                eventTimeTV.setText(formattedTime);
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();

            }
        });


        //what happens when the voice command button is clicked
        speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        //what happens when the info button is clicked
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInfoTextView();
            }
        });

        //what happens when the home button is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_event,homeFragment).commit();
            }
        });





    }

    //setting up the view of the name of the event and the date
    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
    }

    //actions that takes place when the user asks to save an event
    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        LocalTime eventTime = LocalTime.parse(eventTimeTV.getText());
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, eventTime);
        Event.eventsList.add(newEvent);
        finish();
    }

    //actions that takes place when the user asks to save an event with a voice command
    public void saveEventAction()
    {
        String eventName = eventNameET.getText().toString();
        LocalTime eventTime = LocalTime.parse(eventTimeTV.getText());
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, eventTime);
        Event.eventsList.add(newEvent);
        finish();
    }


    //toggling text info based on the user clicking the info button
    private void toggleInfoTextView() {
        if (infoText.getVisibility() == View.GONE) {
            infoText.setVisibility(View.VISIBLE);
        } else {
            infoText.setVisibility(View.GONE);
        }
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
        if (command.contains("αποθήκευση")) {
            saveEventAction();
        }

        else {
            Toast.makeText(this, "Με συγχωρείτε δεν κατάλαβα με τι θέλετε να σας βοηθήσω. Παρακαλώ ξανά πατήστε το κουμπί και ξανά μιλήστε μου", Toast.LENGTH_SHORT).show();
        }
    }


}