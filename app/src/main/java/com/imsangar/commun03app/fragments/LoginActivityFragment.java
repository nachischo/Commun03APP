package com.imsangar.commun03app.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

        if (ContextCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(((LoginActivity)getActivity()),
                    new String[] {Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE} ,
                    101);

            return;
        }


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

            if(requireArguments().getInt("codigo") == 404){
                binding.errorLogin.setText("No se ha podido encontrar el sensor asociado.");
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
