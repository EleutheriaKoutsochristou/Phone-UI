package com.example.hc_inter.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.hc_inter.PopUp.PopUp;
import com.example.hc_inter.R;
import com.example.hc_inter.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ImageButton button;

    private ImageButton infobutton;
    private ImageButton imageButtonSettings;
    private ImageButton weatherButton;

    private ImageButton cameraButton;

    private ImageButton calendarButton;

    private ImageButton ContactButton;
    private ImageButton GalleryButton;
    private ImageButton GpsButton;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private ImageButton speakbutton;

    Dialog dialog;

    private TextView timeText;
    private TextView dateText;

    private TextView textView;
    private ImageButton info;

    //Time and Date
    private final Handler handler = new Handler();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", new Locale("el", "GR"));
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());


    //Athens only -------------------------------------------------------
    //private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", new Locale("el", "GR"));

    private final Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            updateDateTime();
            handler.postDelayed(this, 100);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();


        //button = binding.emergButton;

        button = root.findViewById(R.id.emergButton);

        cameraButton = root.findViewById(R.id.cameraButton);

        infobutton = root.findViewById(R.id.imageButtonInfo);
        imageButtonSettings = root.findViewById(R.id.imageButtonSettings);


        dateText = root.findViewById(R.id.textView2);

        timeText = root.findViewById(R.id.textView6);
        weatherButton = root.findViewById(R.id.imageButton14);
        speakbutton = root.findViewById(R.id.imageButton21);
        textView = root.findViewById(R.id.textView34);
        info = root.findViewById(R.id.imageButton11);

        calendarButton = root.findViewById(R.id.imageButton15);

        ContactButton = root.findViewById(R.id.imageButton16);

        GpsButton = root.findViewById(R.id.imageButton13);
        GpsButton.setOnClickListener(v -> gps_pop_up_window());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpDialog();
            }
        });

        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_info);
            }
        });

        imageButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_settings);
            }
        });

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_weather);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION);
                } else {
                    openCamera();
                }
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_calendar);
            }
        });


        ContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_contact);
            }
        });


        GalleryButton = root.findViewById(R.id.imageButton8);
        GalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivity(intent);
            }
        });

        speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInfoTextView();
            }
        });


        return root;
    }

    private void gps_pop_up_window() {//LOCATION POP-UP
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.map_pop_up, null);

        // Create the popup window - for the location
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Show the popup window
        popupWindow.showAtLocation(GpsButton, Gravity.CENTER, 0, 0);

        // Set up the search button click handler
        EditText locationEditText = popupView.findViewById(R.id.locationtextView);
        Button searchButton = popupView.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            String location = locationEditText.getText().toString();
            openGoogleMaps(location);
            popupWindow.dismiss();
        });
    }
    private void openGoogleMaps(String location) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(getActivity(), "Google Maps is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(updateTime);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTime);
    }

    private void updateDateTime() {
        //Time updater
        Date now = new Date();
        dateText.setText(dateFormat.format(now));
        timeText.setText(timeFormat.format(now));
    }


    @Nullable


    private void showPopUpDialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        PopUp popUp = new PopUp();
        popUp.show(fragmentManager, "PopUpDialog");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {

            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(requireContext(), "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startVoiceRecognition() {
        //Voice rec
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

        // Method that handles the different voice commands
        if (command.contains("άνοιξε τον καιρό")) {
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_home_to_nav_weather);
        } else if (command.contains("άνοιξε την κάμερα")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Δεν βρέθηκε κάμερα στην συσκευή σας", Toast.LENGTH_SHORT).show();
            }
        }else if(command.contains("άνοιξε τις ρυθμίσεις") || command.contains("ρυθμίσεις")){
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_home_to_nav_settings);
        }
        else if(command.contains("άνοιξε τα στοιχεία") || command.contains("στοιχεία")){
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_home_to_nav_info);
        }
        else if(command.contains("άνοιξε το τηλέφωνο") || command.contains("τηλέφωνο") || command.contains("επαφές")){
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_home_to_nav_contact);
        }
        else if(command.contains("χάθηκα") || command.contains("δεν γνωρίζω που είμαι") || command.contains("δεν ξέρω που είμαι" )|| command.contains("είμαι")){
            gps_pop_up_window();
        }
        else if(command.contains("βοήθεια") || command.contains("έκτακτη ανάγκη") ||command.contains("έπεσα")|| command.contains("χτύπησα")){
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_home_to_nav_emergency);
        }
        else {
            Toast.makeText(requireContext(), "Με συγχωρείτε δεν κατάλαβα με τι θέλετε να σας βοηθήσω. Παρακαλώ ξανά πατήστε το κουμπί και ξανά μιλήστε μου", Toast.LENGTH_SHORT).show();
        }
    }


    private void toggleInfoTextView() {
        //Info button
        if (textView.getVisibility() == View.GONE) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

}


