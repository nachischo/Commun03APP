package com.imsangar.commun03app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.uiElements.FragmentAdapter;
import com.imsangar.commun03app.uiElements.TabManager;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentAdapter.inicializarFragmentHome(MainActivity.this, savedInstanceState);

    }


} // class