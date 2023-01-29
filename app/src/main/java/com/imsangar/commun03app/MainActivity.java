package com.imsangar.commun03app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.fragments.HomeFragment;
import com.imsangar.commun03app.uiElements.FragmentAdapter;
import com.imsangar.commun03app.uiElements.TabManager;

import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
            //Cargar la primera capa de tiles con tiles de osm
            final MapTileProviderBasic tileProvider = new MapTileProviderBasic(getApplicationContext());
            final ITileSource tileSource = TileSourceFactory.DEFAULT_TILE_SOURCE;
            tileProvider.setTileSource(tileSource);
            final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, this.getBaseContext());
            tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);

            //añadir la segunda capa con custom tiles de mapa de interpolación a partir de mediciones
            final MapTileProviderBasic anotherTileProvider = new MapTileProviderBasic(getApplicationContext());
            final ITileSource anotherTileSource = new XYTileSource("MyTiles", 1, 18,  256, ".png",
                    new String[]{"https://raw.githubusercontent.com/nachischo/TileServer/master/MedicionesPruebaV1/"}){
                @Override
                public String getTileURLString(long pMapTileIndex) {
                    return getBaseUrl()
                            + MapTileIndex.getZoom(pMapTileIndex)
                            + "/" + MapTileIndex.getX(pMapTileIndex)
                            + "/" + MapTileIndex.getY(pMapTileIndex)
                            + mImageFilenameEnding;
                }
            };
            anotherTileProvider.setTileSource(anotherTileSource);
            final TilesOverlay secondTilesOverlay = new TilesOverlay(anotherTileProvider, this.getBaseContext());
            secondTilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);

// add the first tilesOverlay to the list
            myOpenMapView.getOverlays().add(tilesOverlay);

// add the second tilesOverlay to the list
            myOpenMapView.getOverlays().add(secondTilesOverlay);


            //XYTileSource tileSource = new XYTileSource("MyTiles", 1, 18, 256, ".png", new String[]{"https://github.com/nachischo/TileServer/tree/master/MedicionesPruebaV1"});
            //myOpenMapView.setTileSource(tileSource);

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
            HomeFragment HomeFragmentObject = new HomeFragment();
            HomeFragmentObject.actualizaTarjetaDatos();
        }
    }

} // class´
