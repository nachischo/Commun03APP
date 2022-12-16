package com.imsangar.commun03app.fragments;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.BuildConfig;
import com.imsangar.commun03app.DevActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.beaconManagement.BTLE;
import com.imsangar.commun03app.databinding.ActivityMapaBinding;
import com.imsangar.commun03app.databinding.HomeBinding;
import com.imsangar.commun03app.services.NotificationEventReceiver;
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

    public static void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CommunO3";
            String description = "Comeme los huevos";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ns", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = getContext().getSystemService(NotificationManager.class);
        }

        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(((MainActivity)getActivity()), "ns")
                .setSmallIcon(R.drawable.nuevo_logo_sin_nombre)
                .setContentTitle("CommunO3")
                .setContentText("Cómeme los huevos")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(((MainActivity)getActivity()));

        notificationManager.notify(1, builder.build());*/

        NotificationEventReceiver.setupAlarm(((MainActivity) getActivity()).getApplicationContext());
        ServicioNotificaciones.ultimoDatoEnviado = System.currentTimeMillis() / 1000;

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


        runnableMapCentered = new Runnable() {

            public void run() {
                GeoPoint mapCenter = (GeoPoint) MainActivity.myOpenMapView.getMapCenter();
                GeoPoint userLocation = MainActivity.userLocation;

                if (mapCenter.getLatitude() < userLocation.getLatitude() - 0.002 || mapCenter.getLatitude() > userLocation.getLatitude() + 0.002 || mapCenter.getLongitude() < userLocation.getLongitude() - 0.002 || mapCenter.getLongitude() > userLocation.getLongitude() + 0.002) {
                    if (!yaAnimeFabUserLocVisible) {
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
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis() + 2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    } else {
                        handler = new Handler();
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis() + 2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    }
                }
                if (mapCenter.getLatitude() > userLocation.getLatitude() - 0.002 && mapCenter.getLatitude() < userLocation.getLatitude() + 0.002 && mapCenter.getLongitude() > userLocation.getLongitude() - 0.002 && mapCenter.getLongitude() < userLocation.getLongitude() + 0.002) {
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
                    } else {
                        handler = new Handler();
                        handler.postAtTime(vuelveAempezar, System.currentTimeMillis() + 2000);
                        handler.postDelayed(vuelveAempezar, 2000);
                    }
                }
            }
        };

        handler.postAtTime(runnableMapCentered, System.currentTimeMillis() + 4000);
        handler.postDelayed(runnableMapCentered, 4000);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityMapaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE);

        BTLE.buscarEsteDispositivoBTLE(sharedPreferences.getString("uuid", "no hay uuid para buscar"));

        MainActivity.locationManager = (LocationManager) ((MainActivity) getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(((MainActivity) getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(((MainActivity) getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return new View(getContext());
        }
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

        ValorActual = binding.NivelConcentracion;
        MediaHoy = binding.MediaDeHoy;
        MaxHoy = binding.MaximaDeHoy;
        MinHoy = binding.MinimaDeHoy;
        TempActual = binding.Temp;


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
            MainActivity.myMapController.setZoom(19.0);
        }

    }

    public static void actualizaTarjetaDatos() {


        REST.nuevaPeticion.get("https://dmesmun.upv.edu.es/ServidorProyecto3a/serv/api.php?queQuieres=Ultimo&tipo=1", new PeticionarioREST.RespuestaREST() {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        JSONObject cuerpoJSON = null;
                        JSONArray arrayCuerpo = null;

                        try {
                            arrayCuerpo = new JSONArray(cuerpo);
                            cuerpoJSON = arrayCuerpo.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            ((TextView) ValorActual).setText(String.valueOf(cuerpoJSON.getDouble("Valor")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        REST.nuevaPeticion.get("https://dmesmun.upv.edu.es/ServidorProyecto3a/serv/api.php?queQuieres=Mayor&tipo=1", new PeticionarioREST.RespuestaREST() {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        JSONObject cuerpoJSON = null;
                        JSONArray arrayCuerpo = null;

                        try {
                            arrayCuerpo = new JSONArray(cuerpo);
                            cuerpoJSON = arrayCuerpo.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            ((TextView) MaxHoy).setText(String.valueOf(cuerpoJSON.getDouble("Valor")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        REST.nuevaPeticion.get("https://dmesmun.upv.edu.es/ServidorProyecto3a/serv/api.php?queQuieres=Menor&tipo=1", new PeticionarioREST.RespuestaREST() {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        JSONObject cuerpoJSON = null;
                        JSONArray arrayCuerpo = null;

                        try {
                            arrayCuerpo = new JSONArray(cuerpo);
                            cuerpoJSON = arrayCuerpo.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            ((TextView) MinHoy).setText(String.valueOf(cuerpoJSON.getDouble("Valor")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        REST.nuevaPeticion.get("https://dmesmun.upv.edu.es/ServidorProyecto3a/serv/api.php?queQuieres=Media&tipo=1", new PeticionarioREST.RespuestaREST() {
                    @Override
                    public void callback(int codigo, String cuerpo) {


                            ((TextView) MediaHoy).setText(String.valueOf(cuerpo));

                    }
                }
        );
        REST.nuevaPeticion.get("https://dmesmun.upv.edu.es/ServidorProyecto3a/serv/api.php?queQuieres=Ultimo&tipo=2", new PeticionarioREST.RespuestaREST() {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        JSONObject cuerpoJSON = null;
                        JSONArray arrayCuerpo = null;

                        try {
                            arrayCuerpo = new JSONArray(cuerpo);
                            cuerpoJSON = arrayCuerpo.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            ((TextView) TempActual).setText(String.valueOf(cuerpoJSON.getDouble("Valor")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
    }


}
