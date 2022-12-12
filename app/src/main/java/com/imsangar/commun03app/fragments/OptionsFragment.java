package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.databinding.ForgottenPasswordBinding;
import com.imsangar.commun03app.databinding.OptionsBinding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class OptionsFragment extends Fragment {

    private OptionsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = OptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.CloseOptionsButton.setAlpha(0f);
        binding.CloseOptionsButton.setVisibility(View.VISIBLE);
        binding.CloseOptionsButton.bringToFront();
        binding.CloseOptionsButton.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);

        binding.CloseOptionsButton.setOnClickListener(view -> {
            FragmentAdapter.inicializarFragmentHome(((MainActivity)getActivity()), savedInstanceState);
            MainActivity.mapInitialized = false;
        });

        return root;
    }
}
