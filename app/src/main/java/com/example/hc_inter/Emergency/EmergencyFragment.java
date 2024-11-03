package com.example.hc_inter.Emergency;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.hc_inter.R;
import com.example.hc_inter.databinding.FragmentEmergencyBinding;
import com.example.hc_inter.databinding.FragmentHomeBinding;
import com.example.hc_inter.home.HomeViewModel;

public class EmergencyFragment extends Fragment {

    private FragmentEmergencyBinding binding;
    private ImageButton home;
    private ImageButton back;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EmergencyViewModel emergencyViewModel = new ViewModelProvider(this).get(EmergencyViewModel.class);

        binding = FragmentEmergencyBinding.inflate(inflater, container, false);

        View root = binding.getRoot();


        home = binding.imageButton3;

        back = binding.imageButton2;


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_emergency_to_nav_home);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_emergency_to_nav_home);
            }
        });



        return root;
    }
}
