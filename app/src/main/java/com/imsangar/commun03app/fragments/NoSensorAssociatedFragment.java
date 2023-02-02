package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.databinding.HomeBinding;
import com.imsangar.commun03app.databinding.NoSensorAssociatedBinding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

public class NoSensorAssociatedFragment extends Fragment {

    private NoSensorAssociatedBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = NoSensorAssociatedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.VolverALoginButton.setOnClickListener(view -> {
            FragmentAdapter.inicializarFragmentLogin(getActivity(),savedInstanceState);
        });

        return root;
    }
}