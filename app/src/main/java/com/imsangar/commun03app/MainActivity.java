package com.imsangar.commun03app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.uiElements.FragmentAdapter;
import com.imsangar.commun03app.uiElements.TabManager;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;


public class MainActivity extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    public static MapView myOpenMapView;
    public static MapController myMapController;
    public static Boolean mapInitialized = false;
    public static GeoPoint userLocation = new GeoPoint(40.46326501151092, -3.7142046644713127);
    Marker userLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 0.0F, this);

        FragmentAdapter.inicializarFragmentHome(MainActivity.this, savedInstanceState);

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        userLocation.setLatitude(location.getLatitude());
        userLocation.setLongitude(location.getLongitude());
        if(!mapInitialized){
            myMapController.animateTo(userLocation, 19.0, 1500L);
            userLocationMarker = new Marker(myOpenMapView);
            userLocationMarker.setPosition(userLocation);
            userLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            userLocationMarker.setIcon(getResources().getDrawable(R.drawable.user_location_marker_drawable));
            myOpenMapView.getOverlays().add(userLocationMarker);
            mapInitialized=true;
        }

        userLocationMarker.setPosition(userLocation);
    }

} // class´
