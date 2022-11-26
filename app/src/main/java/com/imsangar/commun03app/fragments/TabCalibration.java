package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.databinding.CalibrarDispositivoBinding;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TabCalibration extends Fragment {

    private CalibrarDispositivoBinding binding;
    static View valorMedicion = null;
    static View valorMedicionOffset = null;
    static View valorTemperatura = null;

    static double Vcalibracion = 42.31;


    public static void cambiaValorMedicion(double nuevoValorMedicion){
        if(valorMedicion!=null){
            ((TextView)valorMedicion).setText(String.valueOf(new BigDecimal(nuevoValorMedicion).setScale(6, RoundingMode.HALF_UP)));
        }
    }

    public static void cambiaValorMedicionConOffset(double nuevoValorMedicionOffset){
        if(valorMedicionOffset!=null){
            ((TextView)valorMedicionOffset).setText(String.valueOf(new BigDecimal(nuevoValorMedicionOffset).setScale(6, RoundingMode.HALF_UP)));
        }
    }

    public static void cambiaValorTemperatura(double nuevoValorTemperatura){
        if(valorTemperatura!=null){
            ((TextView)valorTemperatura).setText(String.valueOf(new BigDecimal(nuevoValorTemperatura).setScale(1, RoundingMode.HALF_UP)));
        }
    }

    public static double calculaOffset(double valorReal){
        double res = (valorReal - Double.parseDouble(((TextView)valorMedicion).getText().toString())) * Vcalibracion;
        return res;
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
        valorMedicionOffset = binding.valorMedicionCalibrado;

        binding.buttonCalibrar.setOnClickListener(view -> {
            View valorReal = ((MainActivity)getActivity()).findViewById(R.id.editTextValorReal);
            View valorOffset = ((MainActivity)getActivity()).findViewById(R.id.valorOffset);

            ((TextView)valorOffset).setText(String.valueOf(calculaOffset(Double.parseDouble(((TextView)valorReal).getText().toString()))));
            BTLE.esteEsElOffset(calculaOffset(Double.parseDouble(((TextView)valorReal).getText().toString())));
        });

        return root;
    }
}