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
    private String serverIP = "172.20.10.9";

    //al instanciar la clase se almacena el contexto actual
    public Login(Context context){
        this.context = context;
    }

    //función encargada de realizar una petición al servidor para comprobar las credenciales proporcionadas por el usuario.
    //Si éstas son correctas se guardarán los datos de inicio de sesión para el futuro.
    public void iniciaSesion(FragmentActivity actividad, Context contexto, Bundle savedInstanceState, String emailUsuario, String passwordUsuario){
        Log.d("nuevoPostLogin", "{ 'email': '"+emailUsuario+"', 'password': '"+passwordUsuario+"' }");
        try {
            //realizar una petición de tipo POST donde en el cuerpo aparecen el email y la contraseña del usuario
            REST.nuevaPeticion.post("http://"+serverIP+":3000/api/usuarios/login", String.valueOf(new JSONObject().put("email", emailUsuario).put("password", passwordUsuario)), new PeticionarioREST.RespuestaREST() {
                @Override
                public void callback(int codigo, String cuerpo) {
                    //si el código de respuesta es 200 (OK)...
                    if(codigo == 200){
                        Log.d( "nuevoPostLogin", "codigo = " + codigo+" cuerpo = "+cuerpo );

                        JSONObject cuerpoJSON = null;
                        try {
                            //convertir el cuerpo de la respuesta a un objeto JSON
                            cuerpoJSON = new JSONObject(cuerpo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //obtener la referencia a los datos del usuario
                        SharedPreferences sharedPreferences = contexto.getSharedPreferences("shared_prefs",MODE_PRIVATE);
                        //editar los datos del usuario
                        SharedPreferences.Editor editarPreferencias = sharedPreferences.edit();

                        //guardar email y contraseña para futuros inicios de sesión automáticos al entrar en la app
                        editarPreferencias.putString("email", emailUsuario);
                        editarPreferencias.putString("password", passwordUsuario);
                        try {
                            //guardar apodo, url de foto de perfil, rol y token del usuario
                            editarPreferencias.putString("nickname", cuerpoJSON.getString("nickname"));
                            editarPreferencias.putString("profile_photo_url", cuerpoJSON.getString("profile_photo_url"));
                            editarPreferencias.putString("rol", cuerpoJSON.getString("rol"));
                            editarPreferencias.putString("token", cuerpoJSON.getString("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //confirmar el guardado de los nuevos datos
                        editarPreferencias.commit();

                        //realizar una petición de tipo get para conocer la información del sensor asociado al usuario
                        REST.nuevaPeticion.get("http://"+serverIP+":3000/api/usuarios/"+sharedPreferences.getString("nickname","")+"/sensor", new PeticionarioREST.RespuestaREST() {
                            @Override
                            public void callback(int codigo, String cuerpo) {
                                if(codigo == 200) {

                                    //si el código de respuesta es 200 (OK)...
                                    JSONObject cuerpoJSON = null;
                                    try {
                                        //convertir el cuerpo de la respuesta a un objeto JSON
                                        cuerpoJSON = new JSONObject(cuerpo);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //obtener la referencia a los datos del usuario
                                    SharedPreferences sharedPreferences = contexto.getSharedPreferences("shared_prefs",MODE_PRIVATE);
                                    //editar los datos del usuario
                                    SharedPreferences.Editor editarPreferencias = sharedPreferences.edit();
                                    try {
                                        //guardar uuid, valor de calibración del sensor e id del ayuntamiento asociados al sensor del usuario
                                        editarPreferencias.putString("uuid", cuerpoJSON.getString("uuid"));
                                        editarPreferencias.putFloat("valorCalibracion", (float)cuerpoJSON.getDouble("valorCalibracion"));
                                        editarPreferencias.putInt("ayuntamiento_id", cuerpoJSON.getInt("ayuntamiento_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //confirmar el guardado de los nuevos datos
                                    editarPreferencias.commit();

                                    //iniciar la actividad principal con la sesión iniciada
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);

                                }
                                //si el código no fué 200 (OK), llamar a la función que gestiona los errores de inicio de sesión
                                else{
                                    LoginLoadingFragment LoginLoadingObject = new LoginLoadingFragment();
                                    LoginLoadingObject.errorEnInicioDeSesion(actividad, savedInstanceState, codigo, cuerpo);

                                }
                            }

                        });

                    }
                    //si el código no fué 200 (OK), llamar a la función que gestiona los errores de inicio de sesión
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
