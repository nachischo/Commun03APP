package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.databinding.CalibrarDispositivoBinding;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TabCalibration extends Fragment {

    private CalibrarDispositivoBinding binding;
    static View valorMedicion = null;
    static View valorTemperatura = null;


    public static void cambiaValorMedicion(double nuevoValorMedicion){
        if(valorMedicion!=null){
            ((TextView)valorMedicion).setText(String.valueOf(new BigDecimal(nuevoValorMedicion).setScale(6, RoundingMode.HALF_UP)));
        }
    }

    public static void cambiaValorTemperatura(double nuevoValorTemperatura){
        if(valorTemperatura!=null){
            ((TextView)valorTemperatura).setText(String.valueOf(new BigDecimal(nuevoValorTemperatura).setScale(1, RoundingMode.HALF_UP)));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CalibrarDispositivoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        valorMedicion = binding.valorMedicion;
        valorTemperatura = binding.valorTemperatura;

        return root;
    }
}