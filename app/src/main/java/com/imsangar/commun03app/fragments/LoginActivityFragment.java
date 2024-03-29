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

        //comprobar que se han concedido los premisos necesarios para el funcionamiento de la app
        if (ContextCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.USE_FULL_SCREEN_INTENT) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(((LoginActivity)getActivity()), Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //en caso de faltar algún permiso, pedirlo al usuario
            ActivityCompat.requestPermissions(((LoginActivity)getActivity()),
                    new String[] {Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WAKE_LOCK,Manifest.permission.USE_FULL_SCREEN_INTENT,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.VIBRATE} ,
                    101);

            return;
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LoginCommuno3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //si el paquete con los datos de usuario proporcionado al inicializar el fragment no está vacío (contiene información sobre la petición de inicio de sesión)...
        if(!requireArguments().isEmpty()){
            //comprobar si qué código de error devolvió el servidor e informar al usuario del tipo de error que ha ocurrido
            if(requireArguments().getInt("responseCode") == 400){
                binding.errorLogin.setText("El correo o la contraseña son incorrectos.");
            }

            if(requireArguments().getInt("responseCode") == 404){
                binding.errorLogin.setText("No se ha podido encontrar el sensor asociado.");
            }

            else if(requireArguments().getInt("responseCode") == 0){
                binding.errorLogin.setText("Parece que hay un error de conexión.");
            }
        }

        //al presionar el botón de contraseña olvidada...
        binding.forgottenPasswordButton.setOnClickListener(view->{
            //crear un paquete que contenga el email introducido en el login
            Bundle datosUser = new Bundle();
            View emailUsuario = ((com.imsangar.commun03app.LoginActivity)getActivity()).findViewById(R.id.usernameInput);
            datosUser.putString("email", ((TextView)emailUsuario).getText().toString());
            //inicializar el fragment de recuperación de contraseña
            FragmentAdapter.inicializarFragmentForgottenPassword(((com.imsangar.commun03app.LoginActivity)getActivity()), savedInstanceState, datosUser);
        });

        //al presionar el botón de iniciar sesión...
        binding.loginButtonBien.setOnClickListener(view -> {
            //crear un paquete con la información de inicio de sesión introducida por el usuario
            Bundle datosUser = new Bundle();
            View emailUsuario = ((com.imsangar.commun03app.LoginActivity)getActivity()).findViewById(R.id.usernameInput);
            datosUser.putString("email", ((TextView)emailUsuario).getText().toString());
            View passwordUsuario = ((com.imsangar.commun03app.LoginActivity)getActivity()).findViewById(R.id.passwordInput);
            datosUser.putString("password", ((TextView)passwordUsuario).getText().toString());
            //inicializar el fragment de carga que se encarga de verificar las credenciales del usuario
            FragmentAdapter.inicializarFragmentLoginLoading(((com.imsangar.commun03app.LoginActivity)getActivity()), savedInstanceState, datosUser, 10200);
        });

        return root;
    }
}
