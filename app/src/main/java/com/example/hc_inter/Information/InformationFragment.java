package com.example.hc_inter.Information;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.hc_inter.R;
import com.example.hc_inter.databinding.FragmentHomeBinding;
import com.example.hc_inter.databinding.FragmentInfoBinding;

import java.util.ArrayList;
import java.util.Locale;

public class InformationFragment extends Fragment {

    private EditText editTextafm;

    private EditText editTextamka;

    private EditText editTextat;

    private EditText editTextsetyour;

    private EditText getEditTextsetyourvalue;

    private static final String PREFS_NAME = "NotesPrefs";
    private static final String KEY_NOTE = "note";

    private static final String KEY_NOTE_AFM = "note_afm";
    private static final String KEY_NOTE_AMKA = "note_amka";
    private static final String KEY_NOTE_AT = "note_at";
    private static final String KEY_NOTE_SET_YOUR = "note_set_your";
    private static final String KEY_NOTE_SET_YOUR_VALUE = "note_set_your_value";
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private FragmentInfoBinding binding;

    private ImageButton homebutton;

    private ImageButton backbutton;

    private ImageButton infobutton;
    private TextView textinfo;

    private ImageButton speakbutton;

    private TextToSpeech textToSpeech;

    private TextView text;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = FragmentInfoBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        editTextafm = root.findViewById(R.id.editTextAFM);
        editTextamka = root.findViewById(R.id.editTextTextamka);
        editTextat = root.findViewById(R.id.editTextTextat);
        editTextsetyour = root.findViewById(R.id.editTextText4);
        getEditTextsetyourvalue = root.findViewById(R.id.editTextText5);
        infobutton = root.findViewById(R.id.imageButton5);
        textinfo = root.findViewById(R.id.textView20);
        speakbutton = root.findViewById(R.id.imageButton22);
        text = root.findViewById(R.id.textView17);
        backbutton = root.findViewById(R.id.imageButton4);

        homebutton = root.findViewById(R.id.imageButton);

        loadNote(editTextafm, KEY_NOTE_AFM);
        loadNote(editTextamka, KEY_NOTE_AMKA);
        loadNote(editTextat, KEY_NOTE_AT);
        loadNote(editTextsetyour, KEY_NOTE_SET_YOUR);
        loadNote(getEditTextsetyourvalue, KEY_NOTE_SET_YOUR_VALUE);




        //We make sure its info gets stores in the correct form by its key_note
        editTextafm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveNote(editTextafm,KEY_NOTE_AFM);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextamka.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveNote(editTextamka,KEY_NOTE_AMKA);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveNote(editTextat,KEY_NOTE_AT);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextsetyour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveNote(editTextsetyour, KEY_NOTE_SET_YOUR);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getEditTextsetyourvalue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveNote(getEditTextsetyourvalue,KEY_NOTE_SET_YOUR_VALUE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_info_to_nav_home);
            }
        });

        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInfoTextView();
            }
        });


        //TexttoSpeech in order to tell the numbers that have been stored. IT DOESNT WORK IN GREEK!!!!
        textToSpeech = new TextToSpeech(requireContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(new Locale("el", "GR"));
                }
            }
        });


        speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_info_to_nav_home);
            }
        });


        return root;

    }

    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void saveNote(EditText editText, String key) {
        String note = editText.getText().toString();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, note);
        editor.apply();
    }

    private void loadNote(EditText editText, String key) {
        //To make sure its time we open it, it is the same
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        String note = sharedPreferences.getString(key, "");
        editText.setText(note);
    }


    private void toggleInfoTextView() {
        //Info button
        if (textinfo.getVisibility() == View.GONE) {
            textinfo.setVisibility(View.VISIBLE);
        } else {
            textinfo.setVisibility(View.GONE);
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
        //Actions for different commands
        if (command.contains("πες μου το αφμ μου") || command.contains("αφμ")) {
            String afm = editTextafm.getText().toString();
            if (!afm.isEmpty()) {
                textToSpeech.speak("Το ΑΦΜ σας είναι: " + afm, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak("Το ΑΦΜ σας δεν είναι διαθέσιμο", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }
        else {
            Toast.makeText(requireContext(), "Με συγχωρείτε δεν κατάλαβα με τι θέλετε να σας βοηθήσω. Παρακαλώ ξανά πατήστε το κουμπί και ξανά μιλήστε μου", Toast.LENGTH_SHORT).show();
        }
    }
}
