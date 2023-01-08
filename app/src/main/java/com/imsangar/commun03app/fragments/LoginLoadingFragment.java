package com.imsangar.commun03app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.XmlRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.imsangar.commun03app.LoginActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.accountManagement.Login;
import com.imsangar.commun03app.databinding.LoadingScreenBinding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginLoadingFragment extends Fragment {

    private LoadingScreenBinding binding;
    private int animationDuration = 500; // 0.5 Seconds
    private int intervalBetweenAnimations = 500; // 0.5 Seconds
    private Handler handler = new Handler();
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


        handler.postAtTime(deNadaa0, System.currentTimeMillis()+intervalBetweenAnimations);
        handler.postDelayed(deNadaa0, intervalBetweenAnimations);

        Login LoginObject = new Login(getContext());
        LoginObject.iniciaSesion(((LoginActivity)getActivity()), getContext(), savedInstanceState, requireArguments().getString("email"), requireArguments().getString("password"));

        //bypass login for dev purposes
        //Intent intent = new Intent(getContext(), MainActivity.class );
        //startActivity(intent);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LoadingScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }


    public void errorEnInicioDeSesion(FragmentActivity actividad, Bundle savedInstanceState, int codigo, String cuerpo){
        Log.d( "nuevoPostLogin fallido", "codigo de error = " + codigo );
        Bundle bundleReqRes = new Bundle();
        bundleReqRes.putInt("responseCode" , codigo);
        bundleReqRes.putString("responseBody" , cuerpo);
        FragmentAdapter.volverAFragmentLogin(actividad, savedInstanceState, bundleReqRes);
    }

}