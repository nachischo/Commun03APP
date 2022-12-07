package com.imsangar.commun03app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.LoginActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.databinding.ArrancarServicioBeaconsBinding;

public class TabBTLE extends Fragment {

    private ArrancarServicioBeaconsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ArrancarServicioBeaconsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.botonBuscarDispositivosBTLE.setOnClickListener(view -> {
            BTLE.buscarTodosLosDispositivosBTLE();
        });

        binding.botonDetenerBusquedaDispositivosBTLE.setOnClickListener(view -> {
            BTLE.detenerBusquedaDispositivosBTLE();
        });

        binding.buscarMicro.setOnClickListener(view -> {

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs",MODE_PRIVATE);

            BTLE.buscarEsteDispositivoBTLE(sharedPreferences.getString("uuid","no hay uuid para buscar"));
        });
        return root;
    }
}
