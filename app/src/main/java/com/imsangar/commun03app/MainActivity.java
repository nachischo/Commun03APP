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
import com.imsangar.commun03app.fragments.HomeFragment;
import com.imsangar.commun03app.uiElements.FragmentAdapter;
import com.imsangar.commun03app.uiElements.TabManager;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;


public class MainActivity extends AppCompatActivity implements LocationListener {

    public static LocationManager locationManager;
    public static MapView myOpenMapView;
    public static MapController myMapController;
    public static Boolean mapInitialized = true;
    public static Boolean userFound = false;
    public static GeoPoint userLocation;
    Marker userLocationMarker = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //asignar un punto geográfico inicial donde centrar el mapa hasta tener la ubicación del usuario
        userLocation = new GeoPoint(40.46326501151092, -3.7142046644713127);

        //inicializar el bluetooth
        BTLE.inicializarBlueTooth(this, MainActivity.this);

        //inicializar el fragment home para cargar ui de la página principal
        FragmentAdapter.inicializarFragmentHome(MainActivity.this, savedInstanceState);

    }


    //cuando la ubicación del usuario cambie...
    @Override
    public void onLocationChanged(@NonNull Location location) {

        //actualizar la variable global que almacena la última ubicación conocida
        userLocation.setLatitude(location.getLatitude());
        userLocation.setLongitude(location.getLongitude());

        //si es la primera vez que se abre el mapa...
        if(!mapInitialized){
            //ampliar y animar el mapa hacia la ubicación del usuario
            myMapController.animateTo(userLocation, 19.0, 1500L);
            //crear un marcador que indique la posición del usuario
            userLocationMarker = new Marker(myOpenMapView);
            userLocationMarker.setPosition(userLocation);
            userLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            userLocationMarker.setIcon(getResources().getDrawable(R.drawable.user_location_marker_drawable));
            myOpenMapView.getOverlays().add(userLocationMarker);
            userFound=true;
            //indicar que el mapa ha sido inicializado y el marcador del usuario creado
            mapInitialized=true;
        }

        //si ya ha sido creado el marcador del usuario...
        if(userLocationMarker!=null){
            //reposicionarlo en la nueva ubicación del usuario
            userLocationMarker.setPosition(userLocation);
            //actualizar los datos de la tarjeta
            HomeFragment.actualizaTarjetaDatos();
        }
    }

} // class´
