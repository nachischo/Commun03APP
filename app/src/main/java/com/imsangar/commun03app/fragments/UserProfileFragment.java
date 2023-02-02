package com.imsangar.commun03app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.DevActivity;
import com.imsangar.commun03app.LoginActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.databinding.OptionsBinding;
import com.imsangar.commun03app.databinding.UserProfileBinding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

public class UserProfileFragment extends Fragment {

    private UserProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.CloseProfileButton.setOnClickListener(view -> {
            FragmentAdapter.inicializarFragmentHome(((MainActivity)getActivity()), savedInstanceState);
            MainActivity.mapInitialized = false;
        });

        binding.nombreField.setText(requireArguments().getString("nombre"));
        binding.emailField.setText(requireArguments().getString("email"));
        binding.AytoField.setText(requireArguments().getString("ayto"));
        binding.SensorField.setText(requireArguments().getString("uuid"));

        return root;
    }
}

