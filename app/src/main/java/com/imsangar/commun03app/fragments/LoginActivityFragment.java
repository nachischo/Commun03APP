package com.imsangar.commun03app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.imsangar.commun03app.LoginActivity;
import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.databinding.LoginCommuno3Binding;
import com.imsangar.commun03app.uiElements.FragmentAdapter;

public class LoginActivityFragment extends Fragment {

    private LoginCommuno3Binding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LoginCommuno3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(!requireArguments().isEmpty()){
            if(requireArguments().getInt("codigo") == 400){
                binding.errorLogin.setText("El correo o la contraseña son incorrectos.");
            }

            else if(requireArguments().getInt("codigo") == 0){
                binding.errorLogin.setText("Parece que hay un error de conexión.");
            }
        }

        binding.forgottenPasswordButton.setOnClickListener(view->{
            Bundle datosUser = new Bundle();
            View emailUsuario = ((com.imsangar.commun03app.LoginActivity)getActivity()).findViewById(R.id.usernameInput);
            datosUser.putString("email", ((TextView)emailUsuario).getText().toString());
            FragmentAdapter.inicializarFragmentForgottenPassword(((com.imsangar.commun03app.LoginActivity)getActivity()), savedInstanceState, datosUser);
        });


        binding.loginButtonBien.setOnClickListener(view -> {
            Bundle datosUser = new Bundle();
            View emailUsuario = ((com.imsangar.commun03app.LoginActivity)getActivity()).findViewById(R.id.usernameInput);
            datosUser.putString("email", ((TextView)emailUsuario).getText().toString());
            View passwordUsuario = ((com.imsangar.commun03app.LoginActivity)getActivity()).findViewById(R.id.passwordInput);
            datosUser.putString("password", ((TextView)passwordUsuario).getText().toString());
            FragmentAdapter.inicializarFragmentLoginLoading(((com.imsangar.commun03app.LoginActivity)getActivity()), savedInstanceState, datosUser, 10200);
        });

        return root;
    }
}
