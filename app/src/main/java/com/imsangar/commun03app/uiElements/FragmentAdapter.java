package com.imsangar.commun03app.uiElements;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.imsangar.commun03app.R;
import com.imsangar.commun03app.fragments.ForgottenPasswordFragment;
import com.imsangar.commun03app.fragments.ForgottenPasswordResultFragment;
import com.imsangar.commun03app.fragments.FragmentPruebaHome;
import com.imsangar.commun03app.fragments.HomeFragment;
import com.imsangar.commun03app.fragments.LoginActivityFragment;
import com.imsangar.commun03app.fragments.LoginLoadingFragment;
import com.imsangar.commun03app.fragments.OptionsFragment;

public class FragmentAdapter {

    //función encargada de inicializar la ui de inicio de sesión
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

    //función encargada de reinicializar la ui de inicio de sesión cuando ocurre algún problema
    public static void volverAFragmentLogin(FragmentActivity actividad, Bundle savedInstanceState, Bundle respuestaPeticion) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, LoginActivityFragment.class, respuestaPeticion)
                    .commit();
        }
    }

    //función encargada de inicializar la ui de carga cuando se está realizando el inicio de sesión
    public static void inicializarFragmentLoginLoading(FragmentActivity actividad, Bundle savedInstanceState, Bundle datosUser, int loadingTime) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, LoginLoadingFragment.class, datosUser)
                    .commit();
        }
    }

    //función encargada de inicializar la ui de la página principal
    public static void inicializarFragmentHome(FragmentActivity actividad, Bundle savedInstanceState) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, HomeFragment.class, null)
                    .commit();
        }
    }

    //función encargada de inicializar la ui de recuperación de contraseña olvidada
    public static void inicializarFragmentForgottenPassword(FragmentActivity actividad, Bundle savedInstanceState, Bundle datosUser) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, ForgottenPasswordFragment.class, datosUser)
                    .commit();
        }
    }

    //función encargada de inicializar la ui del resultado de la recuperación de contraseña olvidada
    public static void inicializarFragmentForgottenPasswordResult(FragmentActivity actividad, Bundle savedInstanceState, Bundle respuestaPeticion) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, ForgottenPasswordResultFragment.class, respuestaPeticion)
                    .commit();
        }
    }

    //función encargada de inicializar la ui de las opciones en la página principal
    public static void inicializarFragmentOptions(FragmentActivity actividad, Bundle savedInstanceState) {
        actividad.setContentView(R.layout.activity_principal);

        if (savedInstanceState == null) {
            actividad.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, OptionsFragment.class, null)
                    .commit();
        }
    }
}
