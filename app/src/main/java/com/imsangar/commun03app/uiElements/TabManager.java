package com.imsangar.commun03app.uiElements;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.imsangar.commun03app.R;

public class TabManager {

    public static void inicializarTabs(Activity actividad){
        actividad.setContentView(R.layout.activity_main);

        // Nombres de las pestañas
        String[] nombres = new String[]{"Servicio Beacons","BBDD", "Login"};

        //Pestañas
        ViewPager2 viewPager = actividad.findViewById(R.id.viewpager);
        viewPager.setAdapter(new MiPagerAdapter(((FragmentActivity)actividad)));
        TabLayout tabs = actividad.findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position){
                        tab.setText(nombres[position]);
                    }
                }
        ).attach();
    }
}
