package com.imsangar.commun03app.fragments;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
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
import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.databinding.ActivityMapaBinding;
import com.imsangar.commun03app.databinding.HomeBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


public class HomeFragment extends Fragment {

    private ActivityMapaBinding binding;
    private CountDownTimer countDownTimer;
    private Boolean isConfigExpanded = false;
    private Boolean isSensorExpanded = false;
    private ViewGroup.LayoutParams originalConfigParams = null;
    private ViewGroup.LayoutParams originalSensorParams = null;
    private Handler handler = new Handler();
    private Runnable runnableMapCentered = null;
    private Runnable vuelveAempezar = null;
    private Boolean yaAnimeFabUserLocVisible = false;
    private Boolean yaAnimeFabUserLocInvisible = false;


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

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs",MODE_PRIVATE);

        BTLE.buscarEsteDispositivoBTLE(sharedPreferences.getString("uuid","no hay uuid para buscar"));

        vuelveAempezar = new Runnable() {
            @Override
            public void run() {
                handler = new Handler();
                handler.postAtTime(runnableMapCentered, System.currentTimeMillis()+100);
                handler.postDelayed(runnableMapCentered, 100);

            }
        };


        runnableMapCentered = new Runnable(){

            public void run() {
                GeoPoint mapCenter = (GeoPoint) MainActivity.myOpenMapView.getMapCenter();
                GeoPoint userLocation = MainActivity.userLocation;

                if(mapCenter.getLatitude()<userLocation.getLatitude()-0.002||mapCenter.getLatitude()>userLocation.getLatitude()+0.002||mapCenter.getLongitude()<userLocation.getLongitude()-0.002||mapCenter.getLongitude()>userLocation.getLongitude()+0.002){
                    if(!yaAnimeFabUserLocVisible){
                        yaAnimeFabUserLocVisible = true;
                        yaAnimeFabUserLocInvisible = false;
                        binding.fabUserLocation.setTag(true);
                        binding.fabUserLocation.bringToFront();
                        binding.fabUserLocation.setVisibility(View.VISIBLE);
                        binding.fabUserLocation.animate()
                                .alpha(1f)
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        binding.fabUserLocation.setVisibility(View.VISIBLE);
                                    }
                                });
                        handler = new Handler();
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis()+2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    }
                    else{
                        handler = new Handler();
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis()+2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    }
                }
                if(mapCenter.getLatitude()>userLocation.getLatitude()-0.002&&mapCenter.getLatitude()<userLocation.getLatitude()+0.002&&mapCenter.getLongitude()>userLocation.getLongitude()-0.002&&mapCenter.getLongitude()<userLocation.getLongitude()+0.002) {
                    if (!yaAnimeFabUserLocInvisible) {
                        yaAnimeFabUserLocInvisible = true;
                        yaAnimeFabUserLocVisible = false;
                        binding.fabUserLocation.setTag(false);
                        binding.fabUserLocation.animate()
                                .alpha(0f)
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        binding.fabUserLocation.setVisibility(View.GONE);
                                    }
                                });
                        handler = new Handler();
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis() + 2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    }
                    else{
                        handler = new Handler();
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis()+2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    }
                }
            }
        };

        handler.postAtTime(runnableMapCentered, System.currentTimeMillis()+4000);
        handler.postDelayed(runnableMapCentered, 4000);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityMapaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                    countDownTimer = new CountDownTimer(500, 100) {
                        @Override
                        public void onTick(long l) {
                            ViewGroup.LayoutParams params = binding.fabOptions.getLayoutParams();
                            originalConfigParams = params;
                            if(!isConfigExpanded){
                                params.width = originalConfigParams.width;
                                params.height = originalConfigParams.height;
                            } else{
                                if(view.getId()==binding.fabOptions.getId()){
                                    params.width *= 1.1;
                                    params.height *= 1.1;
                                    isConfigExpanded = true;
                                }
                            }
                        }

                        @Override
                        public void onFinish() {

                        }
                    }.start();

                }
            }
        };

        binding.fabOptions.setOnClickListener(listener
                /*view -> {
            Intent intent = new Intent(getContext(), DevActivity.class );
            startActivity(intent);
        }*/
        );

        binding.fabUserLocation.setOnClickListener(view -> {
            MainActivity.myMapController.animateTo(MainActivity.userLocation, 19.0, 1500L);
        });


        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        MainActivity.myOpenMapView = (MapView) ((MainActivity)getActivity()).findViewById(R.id.openmapview);
        MainActivity.myOpenMapView.setMultiTouchControls(true);
        MainActivity.myOpenMapView.getMapCenter().getLatitude();
        MainActivity.myOpenMapView.getMapCenter().getLongitude();
        MainActivity.myMapController = (MapController) MainActivity.myOpenMapView.getController();
        MainActivity.myMapController.setCenter(MainActivity.userLocation);
        MainActivity.myMapController.setZoom(7.0);

    }


}
