package com.example.hc_inter.DialPhone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hc_inter.Contact.ContactActivity;
import com.example.hc_inter.R;

import java.util.ArrayList;

public class DialPhoneActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    //Buttons and text for the info feature for each page
    private ImageButton infoButton;
    private TextView infoText;
    // Constant for voice recognition request code
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial_phone);

        //Initialiaze elements
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        infoButton = findViewById(R.id.imageButton5);
        infoText = findViewById(R.id.textView20);
        ImageButton buttonToContacts = findViewById(R.id.buttonToContacts);
        // Setting up click listener for the info button
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInfoTextView();
            }
        });

        // Array of button IDs for number buttons
        int[] buttonIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6,
                R.id.button7, R.id.button8, R.id.button9,
                R.id.buttonStar, R.id.buttonHash
        };

        // Setting up click listener for buttons above
        View.OnClickListener numberButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                phoneNumberEditText.append(button.getText().toString());
            }
        };

        // Setting the click listener for each number button
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(numberButtonClickListener);
        }

        // Setting up click listener for the call button that will open the call function of the phone
        findViewById(R.id.callButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });

        // Setting up click listener for the delete button
        findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = phoneNumberEditText.getText().toString();
                if (!currentText.isEmpty()) {
                    phoneNumberEditText.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });

        // Setting up click listener for the contacts button
        buttonToContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DialPhoneActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

    }
    //making the info text visible based on if the user clicks the info button
    private void toggleInfoTextView() {
        if (infoText.getVisibility() == View.GONE) {
            infoText.setVisibility(View.VISIBLE);
        } else {
            infoText.setVisibility(View.GONE);
        }
    }

}