package com.imsangar.commun03app.accountManagement;


import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;


public class Login {

    public static void iniciaSesion(View emailUsuario, View passwordUsuario){
        Log.d("nuevoPostLogin", "{ 'email': '"+((TextView)emailUsuario).getText().toString()+"', 'password': '"+((TextView)passwordUsuario).getText().toString()+"' }");
        REST.nuevaPeticion.post("http://172.20.10.2:3000/api/usuarios/login", "{ 'email': '" + ((TextView) emailUsuario).getText().toString() + "', 'password': '" + ((TextView) passwordUsuario).getText().toString() + "' }", new PeticionarioREST.RespuestaREST() {
            @Override
            public void callback(int codigo, String cuerpo) {
                if(codigo == 200){
                    Log.d( "nuevoPostLogin", "codigo = " + codigo+" cuerpo = "+cuerpo );
                }
                else{
                    Log.d( "nuevoPostLogin fallido", "codigo de error = " + codigo );

                }
            }
        });
    }


}
