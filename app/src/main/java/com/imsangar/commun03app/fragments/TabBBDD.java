package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.databinding.MostrarDatosBbddBinding;

public class TabBBDD extends Fragment {

    private MostrarDatosBbddBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = MostrarDatosBbddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.pruebaREST.setOnClickListener(view -> {
            REST.probarEnviarPOST();
            REST.probarEnviarGET();
        });

        binding.pruebaPOST.setOnClickListener(view -> {
           REST.nuevaPeticion.post("http://172.20.10.4:8080/altaSensor", "{ 'idSensor': '1', 'tipo': 'ozono', 'nombre': 'prueba Android' }", new PeticionarioREST.RespuestaREST() {
               @Override
               public void callback(int codigo, String cuerpo) {
                   if(codigo == 200){
                       Log.d( "pruebaREST", "codigo = " + codigo+" cuerpo = "+cuerpo );
                   }
                   else{
                       Log.d( "pruebaREST fallida", "codigo de error = " + codigo );
                   }
               }
           });

           REST.nuevaPeticion.post("http://172.20.10.4:8080/altaMedicion", "{ 'idMedicion': 846, 'idSensor': '1', 'valorMedicion': 98, 'tiempo': '"+System.currentTimeMillis()/1000+"', 'lat': '38.9964288', 'long': '-0.1661116'}", new PeticionarioREST.RespuestaREST() {
               @Override
               public void callback(int codigo, String cuerpo) {
                   if(codigo == 200){
                       Log.d( "pruebaREST", "codigo = " + codigo+" cuerpo = "+cuerpo );
                   }
                   else{
                       Log.d( "pruebaREST fallida", "codigo de error = " + codigo );
                   }
               }
           });
        });

        binding.pruebaGET.setOnClickListener(view -> {
          REST.nuevaPeticion.get("http://172.20.10.4:8080/todosLosSensores", new PeticionarioREST.RespuestaREST() {
              @Override
              public void callback(int codigo, String cuerpo) {
                  if(codigo == 200){
                      Log.d( "pruebaREST", "codigo = " + codigo+" cuerpo = "+cuerpo );
                  }
                  else{
                      Log.d( "pruebaREST fallida", "codigo de error = " + codigo );
                  }
              }
          });


          REST.nuevaPeticion.get("http://172.20.10.4:8080/medicionesDelSensor/1", new PeticionarioREST.RespuestaREST() {
              @Override
              public void callback(int codigo, String cuerpo) {
                  if(codigo == 200){
                      Log.d( "pruebaREST", "codigo = " + codigo+" cuerpo = "+cuerpo );
                  }
                  else{
                      Log.d( "pruebaREST fallida", "codigo de error = " + codigo );
                  }
              }
          });
        });

        return root;
    }
}

