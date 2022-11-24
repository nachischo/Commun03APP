package com.imsangar.commun03app.RESTrequest;

import android.util.Log;


public class REST {
    //-------------------------------------------------------------
    //---------------------------REST------------------------------
    //-------------------------------------------------------------
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

    //doy de alta una nueva medicion en la base de datos con metodo POST
    //idMedicion: Texto, idSensor: Texto, valorMedicion: N --> altaNuevaMedicion() -->
    public static void altaNuevaMedicion(String idMedicion, String idSensor, int valorMedicion){
        PeticionarioREST elPeticionario = new PeticionarioREST();


        elPeticionario.hacerPeticionREST("POST",  "http://172.20.10.2:3000/api/mediciones",

                "{ 'sensor': '"+idSensor+"', 'valor': "+valorMedicion+", 'lat': '38.9964288', 'lon': '-0.1661116'}",
                new PeticionarioREST.RespuestaREST () {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        Log.d( "pruebasPeticionario", "TENGO RESPUESTA:\ncodigo = " + codigo + "\ncuerpo: \n" + cuerpo);

                    }
                }
        );
    }

    //clase genérica para realizar peticiones http get o post
    //, 'tiempo': '"+System.currentTimeMillis()/1000+"'

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
    }

    //-------------------------------------------------------------
    //-----------------------FIN REST------------------------------
    //-------------------------------------------------------------
}
