package com.imsangar.commun03app.fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.BuildConfig;
import com.imsangar.commun03app.DevActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.databinding.ActivityMapaBinding;
import com.imsangar.commun03app.databinding.HomeBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


public class HomeFragment extends Fragment {

    private ActivityMapaBinding binding;
    private MapView myOpenMapView;
    private MapController myMapController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = ((MainActivity)getActivity()).getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (((MainActivity)getActivity()).checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(((MainActivity)getActivity()), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityMapaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.floatinButtonOptions.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), DevActivity.class );
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        GeoPoint madrid = new GeoPoint(38.99660440484192, -0.16639447183603295);

        myOpenMapView = (MapView) ((MainActivity)getActivity()).findViewById(R.id.openmapview);
        myOpenMapView.setMultiTouchControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setCenter(madrid);
        myMapController.setZoom(19.0);
    }


}
