package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.databinding.ForgottenPasswordBinding;
import com.imsangar.commun03app.databinding.HomeBinding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

public class ForgottenPasswordFragment extends Fragment {

    private ForgottenPasswordBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ForgottenPasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(requireArguments().getString("email")!=""){
            binding.emailInput.setText(requireArguments().getString("email"));
        }

        binding.forgottenPsswrdButton.setOnClickListener(view -> {
            FragmentAdapter.inicializarFragmentLogin(((com.imsangar.commun03app.LoginActivity)getActivity()), savedInstanceState);
        });

        return root;
    }
}
