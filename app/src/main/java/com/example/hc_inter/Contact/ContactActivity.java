package com.example.hc_inter.Contact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hc_inter.DialPhone.DialPhoneActivity;
import com.example.hc_inter.R;
import com.example.hc_inter.home.HomeFragment;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private static final int REQUEST_CODE_READ_CALL_LOG = 100;
    private EditText searchEditText;
    private ListView contactsListView;
    private ContactAdapter adapter;
    private ArrayList<Contact> contactsList;
    private ArrayList<Contact> filteredContactsList;
    private Button addContactButton;
    private ImageButton infoButton;
    private ImageButton voicebutton;
    private TextView infoText;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //Initialiaze/set the ids
        ImageButton openCallLogButton = findViewById(R.id.buttonToCallHistory);
        infoButton = findViewById(R.id.imageButton5);
        infoText = findViewById(R.id.textView20);
        ImageButton buttonToDialPhone = findViewById(R.id.buttonToDialPhone);
        ImageButton navigateButton = findViewById(R.id.imageButton4);
        searchEditText = findViewById(R.id.searchEditText);
        contactsListView = findViewById(R.id.contactsListView);
        addContactButton = findViewById(R.id.addContactButton);
        voicebutton = findViewById(R.id.imageButton22);

        // Initialize and set onClickListener for opening call log
        openCallLogButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_CODE_READ_CALL_LOG);
            } else {
                openCallHistory();
            }
        });

        // Initialize and set onClickListener for info button
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInfoTextView();
            }
        });

        // Initialize and set onClickListener for navigating to dial phone activity
        buttonToDialPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, DialPhoneActivity.class);
                startActivity(intent);
            }
        });

        // Initialize and set onClickListener for navigating to home fragment
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
            }
        });


        // Our contact List, intialized with some examples for showcasing
        contactsList = new ArrayList<>();
        contactsList.add(new Contact("Person 1", "69987652417"));
        contactsList.add(new Contact("Person 2", "69987652417"));
        contactsList.add(new Contact("Person 3", "69987652418"));
        contactsList.add(new Contact("Person 4", "69987652419"));
        contactsList.add(new Contact("Person 5", "69987652410"));
        contactsList.add(new Contact("Person 6", "69987642419"));
        contactsList.add(new Contact("Person 7", "69987632410"));
        contactsList.add(new Contact("Person 8", "69987654410"));
        filteredContactsList = new ArrayList<>(contactsList);

        // Set up the ContactAdapter
        adapter = new ContactAdapter(this, filteredContactsList);
        contactsListView.setAdapter(adapter);


        // Add TextWatcher to filter the contacts list based on user input
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterContacts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        // Set OnClickListener for the add contact button
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog();
            }
        });

        // Set OnItemClickListener for the contacts list to show options dialog for call and delete
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showContactOptionsDialog(position);
            }
        });

        // Set OnClickListener for the voice button to start voice recognition
        voicebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });
    }

    // Method to open call history
    private void openCallHistory() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("content://call_log/calls"));
        startActivity(intent);
    }

    // Method to filter contacts based on search query
    private void filterContacts(String query) {
        filteredContactsList.clear();
        if (query.isEmpty()) {
            filteredContactsList.addAll(contactsList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Contact contact : contactsList) {
                if (contact.getName().toLowerCase().contains(lowerCaseQuery) ||
                        contact.getPhoneNumber().toLowerCase().contains(lowerCaseQuery)) {
                    filteredContactsList.add(contact);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Method to show a dialog for adding a new contact
    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Προσθήκη Επαφής");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.add_contact, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText inputName = viewInflated.findViewById(R.id.inputContactName);
        final EditText inputPhoneNumber = viewInflated.findViewById(R.id.inputContactPhoneNumber);

        builder.setView(viewInflated);

        builder.setPositiveButton("Προσθήκη", (dialog, which) -> {
            String contactName = inputName.getText().toString().trim();
            String contactPhoneNumber = inputPhoneNumber.getText().toString().trim();
            if (!contactName.isEmpty() && !contactPhoneNumber.isEmpty()) {
                contactsList.add(new Contact(contactName, contactPhoneNumber));
                filterContacts(searchEditText.getText().toString());
            }
        });
        builder.setNegativeButton("Ακύρωση", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Method to show options dialog for a contact call or delete
    private void showContactOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Επιλογές Επαφής");

        String[] options = {"Κλήση", "Διαγραφή"};
        builder.setItems(options, (dialog, which) -> {
            Contact selectedContact = filteredContactsList.get(position);

            // Using if statement instead of switch
            if (which == 0) {
                // Call the contact
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + selectedContact.getPhoneNumber()));
                startActivity(callIntent);
            } else if (which == 1) {
                // Delete the contact
                contactsList.remove(selectedContact);
                filterContacts(searchEditText.getText().toString());
            }
        });
        builder.show();
    }
    // Load fragment to navigate to main fragment from activity
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }

    //Voice recognision commands
    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el-GR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Μιλήστε μου");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    //Voice recognision commands
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

    //making the info text visible based on if the user clicks the info button
    private void toggleInfoTextView() {
        if (infoText.getVisibility() == View.GONE) {
            infoText.setVisibility(View.VISIBLE);
        } else {
            infoText.setVisibility(View.GONE);
        }
    }
    //Voice recognision commands
    private void handleVoiceCommand(String command) {
        if (command.contains("κλήση") || command.contains("κάλεσε")) {
            Intent intent = new Intent(ContactActivity.this, DialPhoneActivity.class);
            startActivity(intent);
        }
        else if(command.contains("ιστορικό") ||  command.contains("προηγούμενες")){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_CODE_READ_CALL_LOG);
            } else {
                openCallHistory();
            }
        }
        else {
            Toast.makeText(this, "Με συγχωρείτε δεν κατάλαβα με τι θέλετε να σας βοηθήσω. Παρακαλώ ξανά πατήστε το κουμπί και ξανά μιλήστε μου", Toast.LENGTH_SHORT).show();
        }
    }
}