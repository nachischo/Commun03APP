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
import com.imsangar.commun03app.databinding.ForgottrnPasswordResultBinding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgottenPasswordResultFragment extends Fragment {

    private ForgottrnPasswordResultBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ForgottrnPasswordResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(requireArguments().getInt("codigo")==200){
            binding.OkIcon.setVisibility(View.VISIBLE);
            binding.ResultText.setText("Se ha enviado un correo de recuperación a "+requireArguments().getString("email")+". Por favor compueba la bandeja de entrada y sigue los pasos para configurar una nueva contraseña.");
        }
        else{
            binding.ErrorIcon.setVisibility(View.VISIBLE);
            binding.ResultText.setText("Vaya, parece que ha habido un error al recuperar la contraseña... Por favor vuelve a la página de inicio de sesión para intentarlo de nuevo.");
        }

        binding.ReturnButton.setOnClickListener(view -> {
            FragmentAdapter.volverAFragmentLogin(((com.imsangar.commun03app.LoginActivity)getActivity()), savedInstanceState, new Bundle());
        });

        return root;
    }
}
