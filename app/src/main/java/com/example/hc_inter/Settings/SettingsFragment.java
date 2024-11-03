package com.example.hc_inter.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.media.AudioManager;
import android.widget.Toast;

import com.example.hc_inter.R;
import com.example.hc_inter.databinding.FragmentSettingsBinding;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {


    private FragmentSettingsBinding binding;

    private ImageButton homebutton;

    private ImageButton infobutton;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private TextView textView;

    private ImageButton plusbutton;
    private ImageButton minusbutton;

    private TextView brightnesscounter;
    private AudioManager audioManager;


    private ImageButton soundplus;
    private ImageButton soundminus;

    private TextView soundtext;

    private static final String PREFS_NAME = "NotesPrefs";
    private static final String KEY_NOTE = "note";
    private static final String KEY_BRIGHTNESS = "brightness_level";

    private ImageButton speakbutton;

    private ImageButton textminus;

    private int brightnessLevel;

    private ImageButton textplus;

    private float currentTextSize = 22;

    private ImageButton back;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();



        homebutton = root.findViewById(R.id.imageButton7);
        infobutton = root.findViewById(R.id.imageButton6);
        textView = root.findViewById(R.id.textView24);
        plusbutton = root.findViewById(R.id.imageButton110);
        minusbutton = root.findViewById(R.id.imageButton12);
        brightnesscounter = root.findViewById(R.id.textViewcounter);
        soundplus = root.findViewById(R.id.imageButton17);
        soundminus = root.findViewById(R.id.imageButton18);
        soundtext = root.findViewById(R.id.textView26);
        speakbutton = root.findViewById(R.id.imageButton23);
        textplus = root.findViewById(R.id.imageButton112);
        textminus = root.findViewById(R.id.imageButton120);
        back = root.findViewById(R.id.imageButton9);


        audioManager = (AudioManager) requireContext().getSystemService(getContext().AUDIO_SERVICE);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_settings_to_nav_home);
            }
        });

        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInfoTextView();
            }
        });


        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseBrightness();
            }
        });

        minusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseBrightness();
            }
        });


        loadBrightnessLevel();


        soundplus.setOnClickListener(new View.OnClickListener() {
            //Increase volume
            @Override
            public void onClick(View v) {
                adjustVolume(AudioManager.ADJUST_RAISE);
            }
        });

        soundminus.setOnClickListener(new View.OnClickListener() {
            //Decrease volume
            @Override
            public void onClick(View v) {
                adjustVolume(AudioManager.ADJUST_LOWER);
            }
        });

        speakbutton.setOnClickListener(new View.OnClickListener() {
            //Voice reco
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        textplus.setOnClickListener(new View.OnClickListener() {
            //Makes text bigger
            @Override
            public void onClick(View v) {
                increaseTextSize();
            }
        });


        textminus.setOnClickListener(new View.OnClickListener() {
            //Makes text smaller
            @Override
            public void onClick(View v) {
                decreaseTextSize();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_settings_to_nav_home);
            }
        });



        updateVolumeLevel();

        return root;
    }

    private void toggleInfoTextView() {
        //Info button
        if (textView.getVisibility() == View.GONE) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void loadBrightnessLevel() {
        //Finds the current state of the brightness
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        brightnessLevel = sharedPreferences.getInt(KEY_BRIGHTNESS, 5);
        brightnesscounter.setText(String.valueOf(brightnessLevel));
        setBrightness(brightnessLevel);
    }

    private void saveBrightnessLevel() {
        //Save the state
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_BRIGHTNESS, brightnessLevel);
        editor.apply();
    }

    private void increaseBrightness() {
        if (brightnessLevel < 10) {
            brightnessLevel++;
            brightnesscounter.setText(String.valueOf(brightnessLevel));
            setBrightness(brightnessLevel);
            saveBrightnessLevel();
        }
    }

    private void decreaseBrightness() {
        if (brightnessLevel > 1) {
            brightnessLevel--;
            brightnesscounter.setText(String.valueOf(brightnessLevel));
            setBrightness(brightnessLevel);
            saveBrightnessLevel();
        }
    }

    private void setBrightness(int level) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.screenBrightness = level / 10.0f;
        getActivity().getWindow().setAttributes(layoutParams);
    }


    //Volume changes
    private void adjustVolume(int direction) {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, AudioManager.FLAG_SHOW_UI);
        updateVolumeLevel();
    }

    private void updateVolumeLevel() {
        //Updates the textview that shows the level of the sound
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        soundtext.setText("Ήχος: " + currentVolume + " / " + maxVolume);
    }

    //Voice recognition
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
        //Handles different voice commands
        if (command.contains("δυνάμωσε τον ήχο") || command.contains("αύξησε τον ήχο") || command.contains("δυνάμωσε την ένταση") || command.contains("αύξησε την ένταση")) {
            adjustVolume(AudioManager.ADJUST_RAISE);
        } else if(command.contains("χαμήλωσε τον ήχο") || command.contains("μείωσε τον ήχο")|| command.contains("μείωσε την ένταση") || command.contains("χαμήλωσε την ένταση")){
            adjustVolume(AudioManager.ADJUST_LOWER);
        }
        else if(command.contains("χαμήλωσε την φωτεινότητα") || command.contains("μείωσε την φωτεινότητα")|| command.contains("πιο σκοτεινό")){
            decreaseBrightness();
        }
        else if(command.contains("αύξησε την φωτεινότητα") || command.contains("δυνάμωσε την φωτεινότητα")|| command.contains("πιο φωτεινό")){
            increaseBrightness();
        }
        else if(command.contains("μεγάλωσε τα γράμματα") || command.contains("μεγάλωσε τις λέξεις")|| command.contains("κάνε τα γράμματα μεγαλύτερα") || command.contains("κάνε μεγαλύτερα τα γράμματα")){
            increaseTextSize();
        }
        else if(command.contains("μίκραινε τα γράμματα") || command.contains("κάνε μικρότερα τα γράμματα")|| command.contains("κάνε τα γράμματα μικρότερα")){
            decreaseTextSize();
        }
        else {
            Toast.makeText(requireContext(), "Με συγχωρείτε δεν κατάλαβα με τι θέλετε να σας βοηθήσω. Παρακαλώ ξανά πατήστε το κουμπί και ξανά μιλήστε μου", Toast.LENGTH_SHORT).show();
        }
    }


    //In the text changes it recognises the current state and the it changes it
    private void increaseTextSize() {
        currentTextSize += 2;
        View rootView = getActivity().findViewById(android.R.id.content);
        updateTextSize(rootView, currentTextSize);
    }

    private void decreaseTextSize() {
        currentTextSize -= 2;
        View rootView = getActivity().findViewById(android.R.id.content);
        updateTextSize(rootView, currentTextSize);
    }


    //Saves the changes
    private void updateTextSize(View view, float textSize) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                updateTextSize(viewGroup.getChildAt(i), textSize);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextSize(textSize);
        }
    }

}
