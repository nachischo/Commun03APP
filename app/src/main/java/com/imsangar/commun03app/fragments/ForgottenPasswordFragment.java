package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.databinding.ForgottenPasswordBinding;
import com.imsangar.commun03app.databinding.HomeBinding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgottenPasswordFragment extends Fragment {

    private ForgottenPasswordBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ForgottenPasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //si la casilla de email no estaba vacía en la ventana de login...
        if(requireArguments().getString("email")!=""){
            //introducir automáticamente el email proporcionado en la casilla de login
            binding.emailInput.setText(requireArguments().getString("email"));
        }

        //al presionar el botón de recuperar contraseña...
        binding.forgottenPsswrdButton.setOnClickListener(view -> {

            //recuperar el email de la casilla de email
            View emailUsuario = ((com.imsangar.commun03app.LoginActivity)getActivity()).findViewById(R.id.emailInput);
            //realizar una petición de tipo PUT para iniciar el proceso de recuperación de contraseña poniendo el email en el cuerpo
            try {
                REST.nuevaPeticion.put("https://communo3-backend.onrender.com/api/usuarios/resetPassword",String.valueOf(new JSONObject().put("email", ((TextView)emailUsuario).getText().toString() )), new PeticionarioREST.RespuestaREST() {
                            @Override
                            public void callback(int codigo, String cuerpo) {
                                //una vez recibida la respuesta del servidor crear un paquete con los datos de la operación
                                Bundle reqRes = new Bundle();
                                reqRes.putInt("codigo", codigo);
                                reqRes.putString("cuerpo", cuerpo);
                                reqRes.putString("email", requireArguments().getString("email"));
                                //inicializar el fragment de resultado para mostrar el resultado de la operación al usuario y darle instrucciones
                                FragmentAdapter.inicializarFragmentForgottenPasswordResult(((com.imsangar.commun03app.LoginActivity)getActivity()), savedInstanceState, reqRes);
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        return root;
    }
}
