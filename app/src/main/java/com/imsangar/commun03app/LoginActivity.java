package com.imsangar.commun03app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.imsangar.commun03app.uiElements.FragmentAdapter;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentAdapter.inicializarFragmentLogin(LoginActivity.this, savedInstanceState);

    }


} // class