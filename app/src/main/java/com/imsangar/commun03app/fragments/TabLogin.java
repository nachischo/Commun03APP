package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.databinding.LoginPruebaBinding;


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
            View emailUsuario = ((MainActivity)getActivity()).findViewById(R.id.emailUsuario);
            View passwordUsuario = ((MainActivity)getActivity()).findViewById(R.id.passwordUsuario);
            Log.d("nuevoPostLogin", "{ 'email': '"+((TextView)emailUsuario).getText().toString()+"', 'password': '"+((TextView)passwordUsuario).getText().toString()+"' }");
            REST.nuevaPeticion.post("http://172.20.10.2:3000/api/login", "{ 'email': '" + ((TextView) emailUsuario).getText().toString() + "', 'password': '" + ((TextView) passwordUsuario).getText().toString() + "' }", new PeticionarioREST.RespuestaREST() {
                @Override
                public void callback(int codigo, String cuerpo) {
                    if(codigo == 200){
                        Log.d( "nuevoPostLogin", "codigo = " + codigo+" cuerpo = "+cuerpo );
                        View estadoPeticion = ((MainActivity)getActivity()).findViewById(R.id.estadoPeticion);
                        ((TextView)estadoPeticion).setText("Sesión iniciada");
                    }
                    else{
                        Log.d( "nuevoPostLogin fallido", "codigo de error = " + codigo );
                        View estadoPeticion = ((MainActivity)getActivity()).findViewById(R.id.estadoPeticion);
                        View emailUsuario = ((MainActivity)getActivity()).findViewById(R.id.emailUsuario);
                        View passwordUsuario = ((MainActivity)getActivity()).findViewById(R.id.passwordUsuario);
                        ((TextView)estadoPeticion).setText("");
                        ((TextView)passwordUsuario).setText("");
                        ((TextView)estadoPeticion).setText("Algo falla. codigo = " + codigo);
                    }
                }
            });
        });

        return root;
    }
}
