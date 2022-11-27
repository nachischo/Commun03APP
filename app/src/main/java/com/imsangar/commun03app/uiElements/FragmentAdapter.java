package com.imsangar.commun03app.uiElements;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.fragments.HomeFragment;
import com.imsangar.commun03app.fragments.TabLogin;

public class FragmentAdapter {
    public static void inicializarFragmentLogin(FragmentActivity actividad, Bundle savedInstanceState) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, TabLogin.class, null)
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
