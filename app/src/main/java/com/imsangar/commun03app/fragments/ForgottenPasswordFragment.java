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

        if(requireArguments().getString("email")!=""){
            binding.emailInput.setText(requireArguments().getString("email"));
        }

        binding.forgottenPsswrdButton.setOnClickListener(view -> {

            View emailUsuario = ((com.imsangar.commun03app.LoginActivity)getActivity()).findViewById(R.id.emailInput);
            try {
                REST.nuevaPeticion.put("http://172.20.10.2:3000/api/usuarios/resetPassword",String.valueOf(new JSONObject().put("email", ((TextView)emailUsuario).getText().toString() )), new PeticionarioREST.RespuestaREST() {
                            @Override
                            public void callback(int codigo, String cuerpo) {
                                Bundle reqRes = new Bundle();
                                reqRes.putInt("codigo", codigo);
                                reqRes.putString("cuerpo", cuerpo);
                                reqRes.putString("email", requireArguments().getString("email"));
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
