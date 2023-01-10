package com.imsangar.commun03app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.uiElements.TabManager;

public class DevActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inicializar la ui del menú de desarrollador
        TabManager.inicializarTabs(DevActivity.this);
        //inicializar el bluetooth del dispositivo
        BTLE.inicializarBlueTooth(this, DevActivity.this);

    }


} // class