package com.imsangar.commun03app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.DevActivity;
import com.imsangar.commun03app.LoginActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.beaconManagement.BTLE;
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

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);


        binding.CloseOptionsButton.setAlpha(0f);
        binding.CloseOptionsButton.setVisibility(View.VISIBLE);
        binding.CloseOptionsButton.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);

        binding.CloseOptionsButton.setOnClickListener(view -> {
            FragmentAdapter.inicializarFragmentHome(((MainActivity)getActivity()), savedInstanceState);
            MainActivity.mapInitialized = false;
        });

        binding.UserProfileButton.setOnClickListener(view -> {
            Bundle datosUser = new Bundle();
            datosUser.putString("nombre", (sharedPreferences.getString("nickname", "Se ha producido un error...")));
            datosUser.putString("email", (sharedPreferences.getString("email", "Se ha producido un error...")));
            datosUser.putString("ayto", (String.valueOf(sharedPreferences.getInt("ayuntamiento_id", 0))));
            datosUser.putString("uuid", (sharedPreferences.getString("uuid", "Se ha producido un error...")));
            FragmentAdapter.inicializarFragmentUserProfile(((MainActivity)getActivity()), savedInstanceState, datosUser);
        });

        /*
        binding.DevMenuButton.setVisibility(View.INVISIBLE);

        if(!sharedPreferences.getString("rol", "USER").equals("DEV")){
            binding.DevMenuButton.setVisibility(View.VISIBLE);
            binding.DevMenuButton.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), DevActivity.class );
                startActivity(intent);
            });
        }
        */

        binding.CerrarSesionButton.setOnClickListener(view -> {
            SharedPreferences userPrefs = getContext().getSharedPreferences("shared_prefs",MODE_PRIVATE);
            REST.postSensorActivo(false, userPrefs.getString("nickname",""));
            SharedPreferences.Editor editarPreferencias = sharedPreferences.edit();
            editarPreferencias.clear();
            editarPreferencias.commit();

            Intent intent = new Intent(getContext(), LoginActivity.class );
            startActivity(intent);
            ((MainActivity)getActivity()).finish();
            BTLE.detenerBusquedaDispositivosBTLE();
        });

        return root;
    }
}
