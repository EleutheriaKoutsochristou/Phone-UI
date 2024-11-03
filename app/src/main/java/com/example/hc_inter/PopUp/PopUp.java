package com.example.hc_inter.PopUp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hc_inter.R;
import com.example.hc_inter.databinding.FragmentEmergencyBinding;
import com.example.hc_inter.databinding.FragmentHomeBinding;

public class PopUp extends DialogFragment {

    private Button popupButtonyes;
    private Button popupButtonNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pop_up, container, false);

        popupButtonyes = root.findViewById(R.id.popupButtonyes);
        popupButtonNo = root.findViewById(R.id.popupButtonNo);



        popupButtonNo.setOnClickListener(new View.OnClickListener() {

            // if the answer is no then the pop up closes
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        popupButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(PopUp.this);
                navController.navigate(R.id.action_nav_home_to_nav_emergency);
                dismiss();
            }
        });

        return root;
    }
}
