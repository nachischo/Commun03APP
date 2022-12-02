package com.imsangar.commun03app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.DevActivity;
import com.imsangar.commun03app.LoginActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.databinding.LoginPruebaBinding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;


public class TabLogin extends Fragment {

    private LoginPruebaBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LoginPruebaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.loginButton.setOnClickListener(view -> {
            View emailUsuario = ((DevActivity)getActivity()).findViewById(R.id.emailUsuario);
            View passwordUsuario = ((DevActivity)getActivity()).findViewById(R.id.passwordUsuario);
            Log.d("nuevoPostLogin", "{ 'email': '"+((TextView)emailUsuario).getText().toString()+"', 'password': '"+((TextView)passwordUsuario).getText().toString()+"' }");

            try {
                REST.nuevaPeticion.post("http://172.20.10.9:3000/api/usuarios/login", String.valueOf(new JSONObject().put("email", ((TextView) emailUsuario).getText().toString()).put("password", ((TextView) passwordUsuario).getText().toString())), new PeticionarioREST.RespuestaREST() {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        if(codigo == 200){
                            Log.d( "nuevoPostLogin", "codigo = " + codigo+" cuerpo = "+cuerpo );
                            View estadoPeticion = ((DevActivity)getActivity()).findViewById(R.id.estadoPeticion);
                            ((TextView)estadoPeticion).setText("Sesión iniciada");

                        }
                        else{
                            Log.d( "nuevoPostLogin fallido", "codigo de error = " + codigo );
                            View estadoPeticion = ((DevActivity)getActivity()).findViewById(R.id.estadoPeticion);
                            View emailUsuario = ((DevActivity)getActivity()).findViewById(R.id.emailUsuario);
                            View passwordUsuario = ((DevActivity)getActivity()).findViewById(R.id.passwordUsuario);
                            ((TextView)estadoPeticion).setText("");
                            ((TextView)passwordUsuario).setText("");
                            ((TextView)estadoPeticion).setText("Algo falla. codigo = " + codigo);

                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        binding.cerrarSesionDev.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = ((DevActivity)getActivity()).getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editarPreferencias = sharedPreferences.edit();
            editarPreferencias.clear();
            editarPreferencias.commit();

            Intent intent = new Intent(getContext(), LoginActivity.class );
            startActivity(intent);
        });

        return root;
    }
}
