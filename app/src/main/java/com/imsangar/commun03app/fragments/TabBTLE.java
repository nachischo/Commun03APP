package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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
           BTLE.buscarEsteDispositivoBTLE("43:4f:4d:4d:55:4e:2d:4f:33:2d:47:54:49:2d:33:41:");
        });
        return root;
    }
}
