package com.imsangar.commun03app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.imsangar.commun03app.uiElements.FragmentAdapter;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //si el usuario ya había iniciado sesión...
        if(getSharedPreferences("shared_prefs",MODE_PRIVATE).contains("email")&&getSharedPreferences("shared_prefs",MODE_PRIVATE).contains("password")){
            //crear un paquete con los datos de inicio de sesión
            Bundle datosUser = new Bundle();
            datosUser.putString("email", getSharedPreferences("shared_prefs",MODE_PRIVATE).getString("email",""));
            datosUser.putString("password", getSharedPreferences("shared_prefs",MODE_PRIVATE).getString("password",""));
            //inicializar el inicio de sesión con los datos existentes del usuario
            FragmentAdapter.inicializarFragmentLoginLoading(LoginActivity.this, savedInstanceState, datosUser, 10200);
        }
        //si es la primera vez que se usa la app o el usuario había cerrado la sesión...
        else{
            //inicializar el proceso de inicio de sesión
            FragmentAdapter.inicializarFragmentLogin(LoginActivity.this, savedInstanceState);
        }

    }




} // class