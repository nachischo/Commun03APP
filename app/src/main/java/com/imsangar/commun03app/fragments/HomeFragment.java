package com.imsangar.commun03app.fragments;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import static androidx.core.view.ViewCompat.setBackgroundTintList;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.BuildConfig;
import com.imsangar.commun03app.DevActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.beaconManagement.counters;
import com.imsangar.commun03app.databinding.ActivityMapaBinding;
import com.imsangar.commun03app.databinding.HomeBinding;
import com.imsangar.commun03app.services.ServicioNotificaciones;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.math.BigDecimal;
import java.math.RoundingMode;


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
    private static NotificationManager notificationManager = null;
    static View ValorActual = null;
    static View MediaHoy = null;
    static View MaxHoy = null;
    static View MinHoy = null;
    static View TempActual = null;

    static View FabSensor = null;
    static View tarjetaInfoSuperiorLayout = null;
    static TextView CalidadDelAireEstim = null;
    static TextView MediaEnLaZonaText = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = getContext().getSystemService(NotificationManager.class);
        }


        //---------------------------------------------------------------------------------------------------------------
        //-------------------------------arrancar servicio notificaciones------------------------------------------------
        //---------------------------------------------------------------------------------------------------------------
        Intent intent = new Intent(getActivity().getApplicationContext(), ServicioNotificaciones.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis()+0, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60000, pendingIntent);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 360000, 360000, pendingIntent);
        }
        //Log.d("servicionotificaciones", String.valueOf(alarmManager.getNextAlarmClock().getTriggerTime()));
        //---------------------------------------------------------------------------------------------------------------
        //-----------------------------fin arrancar servicio notificaciones----------------------------------------------
        //---------------------------------------------------------------------------------------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = ((MainActivity) getActivity()).getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (((MainActivity) getActivity()).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(((MainActivity) getActivity()), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


        vuelveAempezar = new Runnable() {
            @Override
            public void run() {
                handler = new Handler();
                handler.postAtTime(runnableMapCentered, System.currentTimeMillis() + 100);
                handler.postDelayed(runnableMapCentered, 100);

            }
        };


        //fragmento de código que comprueba si el marcador del usuario se encuentra visible en pantalla.
        //En caso de no ser así, hacer visible el botón para centrar el mapa en la ubicaciín del usuario.
        runnableMapCentered = new Runnable() {

            public void run() {
                //obtener el centro del mapa
                GeoPoint mapCenter = (GeoPoint) MainActivity.myOpenMapView.getMapCenter();
                //obtener la última posición conocida del usuario
                GeoPoint userLocation = MainActivity.userLocation;

                //si la latitud o longitud del centro del mapa disciernen en MÁS de 0.002 grados de la del usuario...
                if (mapCenter.getLatitude() < userLocation.getLatitude() - 0.002 || mapCenter.getLatitude() > userLocation.getLatitude() + 0.002 || mapCenter.getLongitude() < userLocation.getLongitude() - 0.002 || mapCenter.getLongitude() > userLocation.getLongitude() + 0.002) {
                    if (!yaAnimeFabUserLocVisible) {
                        //indicar que el botón es visible
                        yaAnimeFabUserLocVisible = true;
                        //resetear la variable que indica que se desactivó el botón
                        yaAnimeFabUserLocInvisible = false;
                        binding.fabUserLocation.setTag(true);
                        binding.fabUserLocation.bringToFront();
                        //hacer el botón visible con una animación
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
                        //volver a comprobar si el mapa está descentrado tras 2s
                        handler = new Handler();
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis() + 2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    } else {
                        handler = new Handler();
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis() + 2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    }
                }
                //si la latitud o longitud del centro del mapa disciernen en MENOS de 0.002 grados de la del usuario...
                if (mapCenter.getLatitude() > userLocation.getLatitude() - 0.002 && mapCenter.getLatitude() < userLocation.getLatitude() + 0.002 && mapCenter.getLongitude() > userLocation.getLongitude() - 0.002 && mapCenter.getLongitude() < userLocation.getLongitude() + 0.002) {
                    //si el botón de centrado del mapa sigue siendo visible...
                    if (!yaAnimeFabUserLocInvisible) {
                        //indicar que el botón ya no es visible
                        yaAnimeFabUserLocInvisible = true;
                        //resetear la variable que indica que se activó el botón
                        yaAnimeFabUserLocVisible = false;
                        binding.fabUserLocation.setTag(false);
                        //hacer el botón invisible con una animación
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
                        //volver a comprobar si el mapa está descentrado tras 2s
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis() + 2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    } else {
                        handler = new Handler();
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis() + 2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    }
                }
            }
        };

        //correr el fragmento de código para comprobar si el mapa está descentrado tras 4s
        handler.postAtTime(runnableMapCentered, System.currentTimeMillis() + 4000);
        handler.postDelayed(runnableMapCentered, 4000);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityMapaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //obtener la información guardada del usuario con sesión iniciada
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE);

        //iniciar el servicio de búsqueda de beacons procedentes del sensor asociado al usuario
        BTLE BTLEObject = new BTLE(getContext());
        BTLEObject.buscarEsteDispositivoBTLE(sharedPreferences.getString("uuid", "no hay uuid para buscar"));

        MainActivity.locationManager = (LocationManager) ((MainActivity) getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(((MainActivity) getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(((MainActivity) getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return new View(getContext());
        }
        //inicializar el servicio de ubicación del usuario si se han concedido los permisos pertinentes
        ((MainActivity) getActivity()).locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 0.0F, ((MainActivity) getActivity()));


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = new CountDownTimer(500, 100) {
                        @Override
                        public void onTick(long l) {
                            ViewGroup.LayoutParams params = binding.fabOptions.getLayoutParams();
                            originalConfigParams = params;
                            if (!isConfigExpanded) {
                                params.width = originalConfigParams.width;
                                params.height = originalConfigParams.height;
                            } else {
                                if (view.getId() == binding.fabOptions.getId()) {
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

        Animation animation = AnimationUtils.loadAnimation(((MainActivity) getActivity()), R.anim.circle_explosion_anim);
        animation.setDuration(500);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.fabSensor.setVisibility(View.INVISIBLE);
                binding.fabUserLocation.setVisibility(View.GONE);
                binding.bottomSheet.setVisibility(View.INVISIBLE);
                binding.circleOptions.setVisibility(View.VISIBLE);
                binding.fabOptions.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.fabOptions.setVisibility(View.INVISIBLE);
                            }
                        });
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FragmentAdapter.inicializarFragmentOptions(((MainActivity) getActivity()), savedInstanceState);
                binding.fabSensor.setVisibility(View.VISIBLE);
                binding.bottomSheet.setVisibility(View.VISIBLE);
                binding.circleOptions.setVisibility(View.INVISIBLE);
                binding.fabOptions.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        binding.fabOptions.setOnClickListener(view -> {
                    binding.circleOptions.startAnimation(animation);

                    //Intent intent = new Intent(getContext(), DevActivity.class );
                    // startActivity(intent);
                }
        );

        binding.fabUserLocation.setOnClickListener(view -> {
            MainActivity.myMapController.animateTo(MainActivity.userLocation, 19.0, 1500L);
        });

        FabSensor = binding.fabSensor;
        tarjetaInfoSuperiorLayout = binding.tarjetaInfoSuperiorLayout;
        CalidadDelAireEstim = binding.CalidadDelAireEstim;
        MediaEnLaZonaText = binding.MediaEnLaZonaText;


        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        MainActivity.myOpenMapView = (MapView) ((MainActivity) getActivity()).findViewById(R.id.openmapview);
        MainActivity.myOpenMapView.setMultiTouchControls(true);
        MainActivity.myOpenMapView.getMapCenter().getLatitude();
        MainActivity.myOpenMapView.getMapCenter().getLongitude();
        MainActivity.myMapController = (MapController) MainActivity.myOpenMapView.getController();
        MainActivity.myMapController.setCenter(MainActivity.userLocation);
        if (MainActivity.userLocation.getLatitude() == 40.46326501151092 && MainActivity.userLocation.getLongitude() == -3.7142046644713127) {
            MainActivity.myMapController.setZoom(7.0);
            MainActivity.mapInitialized = false;
        } else {
            MainActivity.myMapController.setZoom(18.0);
        }


    }


    //función para actualizar los datos de la tarjeta
    public void actualizaTarjetaDatos(Context context) {

        SharedPreferences userPrefs = context.getSharedPreferences("shared_prefs",MODE_PRIVATE);

        if(!userPrefs.getBoolean("SensorActivo",true)){
            ColorStateList colorStateList1 = ColorStateList.valueOf(Color.parseColor("#ffff4444"));
            FabSensor.setBackgroundTintList(colorStateList1);


            ColorStateList colorStateList6 = ColorStateList.valueOf(Color.parseColor("#808080"));
            tarjetaInfoSuperiorLayout.setBackgroundTintList(colorStateList6);
            CalidadDelAireEstim.setText("BUSCANDO DISPOSITIVO...");
            MediaEnLaZonaText.setText("Estableciendo conexión con tu dispositivo CommunO3. Asegurate de que está encendido y cerca de tu smartphone. Esto puede tardar unos minutos.");

        }
        else{
            ColorStateList colorStateList2 = ColorStateList.valueOf(Color.parseColor("#FF018786"));
            FabSensor.setBackgroundTintList(colorStateList2);

            if (counters.anteriorValorMedicion < 1) {
                ColorStateList colorStateList3 = ColorStateList.valueOf(Color.parseColor("#1A659E"));
                tarjetaInfoSuperiorLayout.setBackgroundTintList(colorStateList3);
                CalidadDelAireEstim.setText("BAJA CONCENTRACIÓN DE O3 EN LA ZONA");
                MediaEnLaZonaText.setText("Las lecturas de tu dispositivo muestran una concentración de ozono dentro de los parámetros saludables.");
            } else if (counters.anteriorValorMedicion >= 1 && counters.anteriorValorMedicion < 2.5) {
                ColorStateList colorStateList4 = ColorStateList.valueOf(Color.parseColor("#B76C34"));
                tarjetaInfoSuperiorLayout.setBackgroundTintList(colorStateList4);
                CalidadDelAireEstim.setText("CONCENTRACIÓN MODERADA DE O3 EN LA ZONA");
                MediaEnLaZonaText.setText("Las lecturas de tu dispositivo muestran una concentración de ozono un poco más alta de lo habitual. Es normal en entornos algo más contaminados como la ciudad.");
            } else if (counters.anteriorValorMedicion >= 2.5) {
                ColorStateList colorStateList5 = ColorStateList.valueOf(Color.parseColor("#94232E"));
                tarjetaInfoSuperiorLayout.setBackgroundTintList(colorStateList5);
                CalidadDelAireEstim.setText("ALTA CONCENTRACIÓN DE O3 EN LA ZONA");
                MediaEnLaZonaText.setText("Las lecturas de tu dispositivo muestran una concentración de ozono muy alta. Tu salud podría estar en riesgo.");
            }
        }

    }


}
