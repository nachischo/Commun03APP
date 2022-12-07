package com.imsangar.commun03app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.imsangar.commun03app.uiElements.FragmentAdapter;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSharedPreferences("shared_prefs",MODE_PRIVATE).contains("email")&&getSharedPreferences("shared_prefs",MODE_PRIVATE).contains("password")){
            Bundle datosUser = new Bundle();
            datosUser.putString("email", getSharedPreferences("shared_prefs",MODE_PRIVATE).getString("email",""));
            datosUser.putString("password", getSharedPreferences("shared_prefs",MODE_PRIVATE).getString("password",""));
            FragmentAdapter.inicializarFragmentLoginLoading(LoginActivity.this, savedInstanceState, datosUser, 10200);
        }
        else{
            FragmentAdapter.inicializarFragmentLogin(LoginActivity.this, savedInstanceState);
        }

    }


} // class