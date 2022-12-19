package com.imsangar.commun03app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.imsangar.commun03app.LoginActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.databinding.LoadingScreenBinding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginLoadingFragment extends Fragment {

    private LoadingScreenBinding binding;
    private int animationDuration = 500; // 0.5 Seconds
    private int intervalBetweenAnimations = 500; // 0.5 Seconds
    private Handler handler = new Handler();
    private Runnable runnable = null;
    private Runnable deNadaa0 = null;
    private Runnable de0a1 = null;
    private Runnable de1a2 = null;
    private Runnable de2aNada = null;
    private Runnable vuelveAempezar = null;
    FragmentManager fm = null;
    Fragment f = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vuelveAempezar = new Runnable() {
            @Override
            public void run() {
                handler = new Handler();
                handler.postAtTime(deNadaa0, System.currentTimeMillis()+100);
                handler.postDelayed(deNadaa0, 100);

            }
        };

        deNadaa0 = new Runnable() {
            @Override
            public void run() {
                binding.loading0.setAlpha(0f);
                binding.loading0.setVisibility(View.VISIBLE);
                binding.loading0.bringToFront();
                binding.loading0.animate()
                        .alpha(1f)
                        .setDuration(animationDuration)
                        .setListener(null);
                handler = new Handler();
                handler.postAtTime(de0a1, System.currentTimeMillis()+intervalBetweenAnimations);
                handler.postDelayed(de0a1, intervalBetweenAnimations);
            }
        };

        de0a1 = new Runnable() {
            @Override
            public void run() {
                binding.loading1.setAlpha(0f);
                binding.loading1.setVisibility(View.VISIBLE);
                binding.loading1.bringToFront();
                binding.loading1.animate()
                        .alpha(1f)
                        .setDuration(animationDuration)
                        .setListener(null);
                binding.loading3.setVisibility(View.VISIBLE);
                handler = new Handler();
                handler.postAtTime(de1a2, System.currentTimeMillis()+intervalBetweenAnimations);
                handler.postDelayed(de1a2, intervalBetweenAnimations);

            }
        };

        de1a2 = new Runnable() {
            @Override
            public void run() {
                binding.loading2.setAlpha(0f);
                binding.loading2.setVisibility(View.VISIBLE);
                binding.loading2.bringToFront();
                binding.loading2.animate()
                        .alpha(1f)
                        .setDuration(animationDuration)
                        .setListener(null);
                handler = new Handler();
                handler.postAtTime(de2aNada, System.currentTimeMillis()+intervalBetweenAnimations);
                handler.postDelayed(de2aNada, intervalBetweenAnimations);

            }
        };

        de2aNada = new Runnable() {
            @Override
            public void run() {
                binding.loading2.animate()
                        .alpha(0f)
                        .setDuration(animationDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.loading2.setVisibility(View.GONE);
                            }
                        });
                binding.loading1.animate()
                        .alpha(0f)
                        .setDuration(animationDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.loading1.setVisibility(View.GONE);
                            }
                        });
                binding.loading0.animate()
                        .alpha(0f)
                        .setDuration(animationDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.loading0.setVisibility(View.GONE);
                            }
                        });
                handler = new Handler();
                handler.postAtTime(vuelveAempezar, System.currentTimeMillis()+intervalBetweenAnimations);
                handler.postDelayed(vuelveAempezar, intervalBetweenAnimations);

            }
        };

        fm = getParentFragmentManager();
        f = fm.findFragmentById(R.id.fragmentContainerView);

        Log.d("Loading", "llego a loadingFragment ");

         runnable = new Runnable(){
            public void run() {
                Log.d("Loading", "llego a lm.popBackStack() ");
                fm.popBackStack();
            }
        };

        handler.postAtTime(deNadaa0, System.currentTimeMillis()+intervalBetweenAnimations);
        handler.postDelayed(deNadaa0, intervalBetweenAnimations);


        //bypass login for dev purposes
        //Intent intent = new Intent(getContext(), MainActivity.class );
        //startActivity(intent);

        Log.d("nuevoPostLogin", "{ 'email': '"+requireArguments().getString("email")+"', 'password': '"+requireArguments().getString("password")+"' }");
        try {
            REST.nuevaPeticion.post("http://172.20.10.2:3000/api/usuarios/login", String.valueOf(new JSONObject().put("email", requireArguments().getString("email")).put("password", requireArguments().getString("password"))), new PeticionarioREST.RespuestaREST() {
                @Override
                public void callback(int codigo, String cuerpo) {
                    if(codigo == 200){
                        Log.d( "nuevoPostLogin", "codigo = " + codigo+" cuerpo = "+cuerpo );

                        JSONObject cuerpoJSON = null;
                        try {
                            cuerpoJSON = new JSONObject(cuerpo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs",MODE_PRIVATE);
                        SharedPreferences.Editor editarPreferencias = sharedPreferences.edit();

                        editarPreferencias.putString("email", requireArguments().getString("email"));
                        editarPreferencias.putString("password", requireArguments().getString("password"));
                        try {
                            editarPreferencias.putString("nickname", cuerpoJSON.getString("nickname"));
                            editarPreferencias.putString("profile_photo_url", cuerpoJSON.getString("profile_photo_url"));
                            editarPreferencias.putString("rol", cuerpoJSON.getString("rol"));
                            editarPreferencias.putString("token", cuerpoJSON.getString("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        editarPreferencias.commit();

                        REST.nuevaPeticion.get("http://172.20.10.2:3000/api/usuarios/"+sharedPreferences.getString("nickname","")+"/sensor", new PeticionarioREST.RespuestaREST() {
                            @Override
                            public void callback(int codigo, String cuerpo) {
                                if(codigo == 200) {
                                    JSONObject cuerpoJSON = null;
                                    try {
                                        cuerpoJSON = new JSONObject(cuerpo);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs",MODE_PRIVATE);
                                    SharedPreferences.Editor editarPreferencias = sharedPreferences.edit();
                                    try {
                                        editarPreferencias.putString("uuid", cuerpoJSON.getString("uuid"));
                                        editarPreferencias.putFloat("uuid", (float)cuerpoJSON.getDouble("valorCariblracion"));
                                        editarPreferencias.putInt("ayuntamiento_id", cuerpoJSON.getInt("ayuntamiento_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    editarPreferencias.commit();

                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                    ((LoginActivity) getActivity()).finish();
                                }
                                else{
                                    Log.d( "nuevoPostLogin fallido", "codigo de error = " + codigo );
                                    Bundle bundleReqRes = new Bundle();
                                    bundleReqRes.putInt("responseCode" , codigo);
                                    bundleReqRes.putString("responseBody" , cuerpo);
                                    FragmentAdapter.volverAFragmentLogin(((LoginActivity)getActivity()), savedInstanceState, bundleReqRes);

                                }
                            }

                        });

                    }
                    else{
                        Log.d( "nuevoPostLogin fallido", "codigo de error = " + codigo );
                        Bundle bundleReqRes = new Bundle();
                        bundleReqRes.putInt("responseCode" , codigo);
                        bundleReqRes.putString("responseBody" , cuerpo);
                        FragmentAdapter.volverAFragmentLogin(((LoginActivity)getActivity()), savedInstanceState, bundleReqRes);

                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LoadingScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }


}