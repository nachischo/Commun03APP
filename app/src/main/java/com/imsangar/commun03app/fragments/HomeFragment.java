package com.imsangar.commun03app.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.DevActivity;
import com.imsangar.commun03app.databinding.HomeBinding;


public class HomeFragment extends Fragment {

    private HomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.devButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), DevActivity.class );
            startActivity(intent);
        });

        return root;
    }
}
