package com.imsangar.commun03app.accountManagement;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.imsangar.commun03app.LoginActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.fragments.LoginLoadingFragment;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;


public class Login {

    public Context context;

    public Login(Context context){
        this.context = context;
    }

    public void iniciaSesion(FragmentActivity actividad, Context contexto, Bundle savedInstanceState, String emailUsuario, String passwordUsuario){
        Log.d("nuevoPostLogin", "{ 'email': '"+emailUsuario+"', 'password': '"+passwordUsuario+"' }");
        try {
            REST.nuevaPeticion.post("http://172.20.10.5:3000/api/usuarios/login", String.valueOf(new JSONObject().put("email", emailUsuario).put("password", passwordUsuario)), new PeticionarioREST.RespuestaREST() {
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

                        SharedPreferences sharedPreferences = contexto.getSharedPreferences("shared_prefs",MODE_PRIVATE);
                        SharedPreferences.Editor editarPreferencias = sharedPreferences.edit();

                        editarPreferencias.putString("email", emailUsuario);
                        editarPreferencias.putString("password", passwordUsuario);
                        try {
                            editarPreferencias.putString("nickname", cuerpoJSON.getString("nickname"));
                            editarPreferencias.putString("profile_photo_url", cuerpoJSON.getString("profile_photo_url"));
                            editarPreferencias.putString("rol", cuerpoJSON.getString("rol"));
                            editarPreferencias.putString("token", cuerpoJSON.getString("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        editarPreferencias.commit();

                        REST.nuevaPeticion.get("http://172.20.10.5:3000/api/usuarios/"+sharedPreferences.getString("nickname","")+"/sensor", new PeticionarioREST.RespuestaREST() {
                            @Override
                            public void callback(int codigo, String cuerpo) {
                                if(codigo == 200) {
                                    JSONObject cuerpoJSON = null;
                                    try {
                                        cuerpoJSON = new JSONObject(cuerpo);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    SharedPreferences sharedPreferences = contexto.getSharedPreferences("shared_prefs",MODE_PRIVATE);
                                    SharedPreferences.Editor editarPreferencias = sharedPreferences.edit();
                                    try {
                                        editarPreferencias.putString("uuid", cuerpoJSON.getString("uuid"));
                                        editarPreferencias.putFloat("uuid", (float)cuerpoJSON.getDouble("valorCariblracion"));
                                        editarPreferencias.putInt("ayuntamiento_id", cuerpoJSON.getInt("ayuntamiento_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    editarPreferencias.commit();

                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);

                                }
                                else{
                                    LoginLoadingFragment LoginLoadingObject = new LoginLoadingFragment();
                                    LoginLoadingObject.errorEnInicioDeSesion(actividad, savedInstanceState, codigo, cuerpo);

                                }
                            }

                        });

                    }
                    else{
                        LoginLoadingFragment LoginLoadingObject = new LoginLoadingFragment();
                        LoginLoadingObject.errorEnInicioDeSesion(actividad, savedInstanceState, codigo, cuerpo);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
