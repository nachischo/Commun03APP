package com.imsangar.commun03app.RESTrequest;

import android.util.Log;

import com.imsangar.commun03app.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class REST {
    //-------------------------------------------------------------
    //---------------------------REST------------------------------
    //-------------------------------------------------------------
    //función para probar que es posible realizar peticiones de tipo POST
    public static void probarEnviarPOST() {
        PeticionarioREST elPeticionario = new PeticionarioREST();

        elPeticionario.hacerPeticionREST("POST",  "http://172.20.10.4:8080/altaSensor",

                "{ 'idSensor': '80', 'tipo': 'Ozono', 'nombre': 'Sensor de prueba'}",
                new PeticionarioREST.RespuestaREST () {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        Log.d( "pruebasPeticionario", "TENGO RESPUESTA:\ncodigo = " + codigo + "\ncuerpo: \n" + cuerpo);

                    }
                }
        );

        //elPeticionario.hacerPeticionREST("POST",  "https://jsonplaceholder.typicode.com/posts",
        //elPeticionario.hacerPeticionREST("POST",  "https://reqbin.com/echo/post/json",

    } // ()

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    //función para probar que es posible realizar peticiones de tipo GET
    public static void probarEnviarGET() {
        PeticionarioREST elPeticionario = new PeticionarioREST();

        //elPeticionario.hacerPeticionREST("GET",  "https://jsonplaceholder.typicode.com/posts/101",
        elPeticionario.hacerPeticionREST("GET",  "http://172.20.10.4:8080/todosLosSensores",
                null,
                new PeticionarioREST.RespuestaREST () {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        Log.d( "pruebasPeticionario", "codigo = " + codigo + "\n" + cuerpo);
                    }
                }
        );

    } // ()

    //dar de alta una nueva medicion en la base de datos con metodo POST
    //idMedicion: Texto, idSensor: Texto, valorMedicion: N --> altaNuevaMedicion() -->
    public static void altaNuevaMedicion(int idMedicion, String idSensor, double valorMedicion){
        //si la ubicación actual del usuario ha sido encontrada...
        if(MainActivity.userFound){
            PeticionarioREST elPeticionario = new PeticionarioREST();

            //realizar una petición de tipo POST que incluya en el cuerpo el id del sensor, el valor de la medición y la ubicación en la que se ha tomado
            try {
                elPeticionario.hacerPeticionREST("POST",  "https://communo3-backend.onrender.com/api/mediciones",

                        String.valueOf(new JSONObject().put("value", valorMedicion).put("lat", MainActivity.userLocation.getLatitude()).put("lng", MainActivity.userLocation.getLongitude()).put("instante", System.currentTimeMillis())),

                        new PeticionarioREST.RespuestaREST () {
                            @Override
                            public void callback(int codigo, String cuerpo) {
                                Log.d( "pruebasPeticionario", "TENGO RESPUESTA:\ncodigo = " + codigo + "\ncuerpo: \n" + cuerpo);

                            }
                        }
                );
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //clase genérica para realizar peticiones http get o post
    //, 'instante': '"+System.currentTimeMillis()+"'

    public static void postSensorActivo(boolean estado, String nickname){
        //si la ubicación actual del usuario ha sido encontrada...
        if(MainActivity.userFound){
            PeticionarioREST elPeticionario = new PeticionarioREST();

            //realizar una petición de tipo POST que incluya en el cuerpo el id del sensor, el valor de la medición y la ubicación en la que se ha tomado
            try {
                elPeticionario.hacerPeticionREST("POST",  "https://communo3-backend.onrender.com/api/usuarios/"+nickname+"/sensor/activo",

                        String.valueOf(new JSONObject().put("activo", estado)),

                        new PeticionarioREST.RespuestaREST () {
                            @Override
                            public void callback(int codigo, String cuerpo) {
                                Log.d( "pruebasPeticionario", "TENGO RESPUESTA:\ncodigo = " + codigo + "\ncuerpo: \n" + cuerpo);

                            }
                        }
                );
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //clase para simplificación de peticiones
    public static class nuevaPeticion{

        //urlDestino:String --> nuevaPeticion.get() -->
        static String cuerpoRespuesta = "";

        public static void get(String urlDestino, PeticionarioREST.RespuestaREST callback){

            PeticionarioREST elPeticionario = new PeticionarioREST();

            elPeticionario.hacerPeticionREST("GET",  urlDestino,
                    null, callback
            );

        }

        //urlDestino:String, cuerpo:String --> nuevaPeticion.post() -->
        public static void post(String urlDestino, String cuerpo, PeticionarioREST.RespuestaREST callback){

            PeticionarioREST elPeticionario = new PeticionarioREST();

            elPeticionario.hacerPeticionREST("POST",  urlDestino,
                    cuerpo, callback
            );
        }

        //urlDestino:String, cuerpo:String --> nuevaPeticion.put() -->
        public static void put(String urlDestino, String cuerpo, PeticionarioREST.RespuestaREST callback){

            PeticionarioREST elPeticionario = new PeticionarioREST();

            elPeticionario.hacerPeticionREST("PUT",  urlDestino,
                    cuerpo, callback
            );
        }
    }

    //-------------------------------------------------------------
    //-----------------------FIN REST------------------------------
    //-------------------------------------------------------------
}
