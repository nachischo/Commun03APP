package com.imsangar.commun03app.uiElements;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.imsangar.commun03app.R;
import com.imsangar.commun03app.fragments.HomeFragment;
import com.imsangar.commun03app.fragments.LoginActivityFragment;
import com.imsangar.commun03app.fragments.LoginLoadingFragment;

public class FragmentAdapter {
    public static void inicializarFragmentLogin(FragmentActivity actividad, Bundle savedInstanceState) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, LoginActivityFragment.class, new Bundle())
                    .addToBackStack("LoginActivityFragment")
                    .commit();
        }
    }

    public static void volverAFragmentLogin(FragmentActivity actividad, Bundle savedInstanceState, Bundle respuestaPeticion) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, LoginActivityFragment.class, respuestaPeticion)
                    .commit();
        }
    }

    public static void inicializarFragmentLoginLoading(FragmentActivity actividad, Bundle savedInstanceState, Bundle datosUser, int loadingTime) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, LoginLoadingFragment.class, datosUser)
                    .commit();
        }
    }

    public static void inicializarFragmentHome(FragmentActivity actividad, Bundle savedInstanceState) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, HomeFragment.class, null)
                    .commit();
        }
    }
}
