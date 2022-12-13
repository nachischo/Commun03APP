package com.imsangar.commun03app.beaconManagement;

import static java.lang.Math.pow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.fragments.TabCalibration;

import java.util.List;

class counters {
    static int BeaconCounter = 1;
    static int AnteriorValorBruto = 0;
    static double anteriorValorMedicion = - 1;
    static double anteriorValorMedicionOffset = - 1;
    public static double Voffset = - 1;
    static double CalibracionDelSensor = 48.31;
    static double Vcalibracion = 0.0;



    static double calculaO3(int valorBrutoMedicion) {
        double valorBruto = valorBrutoMedicion * 3.3 / 4096;
        Vcalibracion = CalibracionDelSensor*499*pow(10,-6);
        double res = valorBruto / Vcalibracion;
        return res;
    }

    static double calcula03ConOffset(int valorBrutoMedicion) {
        double valorBruto = valorBrutoMedicion * 3.3 / 4096 - Voffset;
        double res = valorBruto / Vcalibracion;
        return res;
    }

    static double calculaTemperatura(int valorBrutoTemperatura) {
        double valorBruto = valorBrutoTemperatura * 3.3 / 4096;
        double res = valorBruto * 29 - 18;
        return res;
    }

    static void anyadeUnoABeaconCounter() {
        BeaconCounter++;
    }

    static void cambiaValorMedicion(double nuevoValorMedicion) {
        anteriorValorMedicion = nuevoValorMedicion;
    }

    static void cambiaValorMedicionConOffset(double nuevoValorMedicionOffset) {
        anteriorValorMedicionOffset = nuevoValorMedicionOffset;
    }
}

public class BTLE {
    // --------------------------------------------------------------
    // -------------------------BTLE---------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";

    public static final int CODIGO_PETICION_PERMISOS = 11223344;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static BluetoothLeScanner elEscanner;

    private static ScanCallback callbackDelEscaneo = null;


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public static void esteEsElOffset(double offsetValue) {
        counters.Voffset = offsetValue;
        counters.cambiaValorMedicionConOffset(counters.calcula03ConOffset(counters.AnteriorValorBruto));
        TabCalibration.cambiaValorMedicionConOffset(counters.calcula03ConOffset(counters.AnteriorValorBruto));
    }

    // --------------------------------------------------------------
    // --------------------------------------------------------------

    public static void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        callbackDelEscaneo = new ScanCallback() {

            //cuando encuentre un beacon ejecuto este código
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE(resultado);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");


        elEscanner.startScan(callbackDelEscaneo);

    } // ()

    // --------------------------------------------------------------
    //resultadoBusquedaBTLE: ScanResult --> mostrarInformacionDispositivoBTLE() -->
    // --------------------------------------------------------------
    public static void mostrarInformacionDispositivoBTLE(ScanResult resultado) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

        /*
        ParcelUuid[] puuids = bluetoothDevice.getUuids();
        if ( puuids.length >= 1 ) {
            //Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].getUuid());
           // Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].toString());
        }*/

        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi);

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);

        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ****************************************************");

    } // ()

    // --------------------------------------------------------------
    //uuidSensor:Texto --> buscarEsteDispositivoBTLE() -->
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    public static void buscarEsteDispositivoBTLE(final String dispositivoBuscado) {

        //-----------------------------------------------------------------------------------------

        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");


        // super.onScanResult(ScanSettings.SCAN_MODE_LOW_LATENCY, result); para ahorro de energía

        callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult() ");
                byte[] bytes = resultado.getScanRecord().getBytes();
                TramaIBeacon tib = new TramaIBeacon(bytes);
                Log.d(">>>>", "onScanResult: " + Utilidades.bytesToHexString(tib.getUUID()));
                Log.d(">>>>", "dispositivoBuscado: " + dispositivoBuscado);

                //solamente si el sensor tiene el uuid que estoy buscando sigo
                if (Utilidades.bytesToHexString(tib.getUUID()).equals(dispositivoBuscado)) {
                    mostrarInformacionDispositivoBTLE(resultado);

                    //solamente si el valor de la medicion ha cambiado hago POST al servidor para introducir una nueva medicion
                    if (Utilidades.bytesToInt(tib.getMajor()) == 1) {

                        TabCalibration.cambiaValorMedicion(counters.calculaO3(Utilidades.bytesToInt(tib.getMinor())));
                        counters.AnteriorValorBruto = Utilidades.bytesToInt(tib.getMinor());

                        REST.altaNuevaMedicion(Utilidades.bytesToInt(tib.getMajor()), dispositivoBuscado, counters.calculaO3(Utilidades.bytesToInt(tib.getMinor())));
                        counters.anyadeUnoABeaconCounter();
                        counters.cambiaValorMedicion(counters.calculaO3(Utilidades.bytesToInt(tib.getMinor())));
                        if (counters.Voffset != - 1) {
                            counters.cambiaValorMedicionConOffset(counters.calcula03ConOffset(Utilidades.bytesToInt(tib.getMinor())));
                            TabCalibration.cambiaValorMedicionConOffset(counters.calcula03ConOffset(Utilidades.bytesToInt(tib.getMinor())));
                        }

                    } else if (Utilidades.bytesToInt(tib.getMajor()) == 2) {

                        TabCalibration.cambiaValorTemperatura(counters.calculaTemperatura(Utilidades.bytesToInt(tib.getMinor())));

                        REST.altaNuevaMedicion(Utilidades.bytesToInt(tib.getMajor()), dispositivoBuscado, counters.calculaTemperatura(Utilidades.bytesToInt(tib.getMinor())));
                        counters.anyadeUnoABeaconCounter();
                        counters.cambiaValorMedicion(counters.calculaTemperatura(Utilidades.bytesToInt(tib.getMinor())));
                    }
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() ");

            }
        };

        ScanFilter sf = new ScanFilter.Builder().setDeviceName(dispositivoBuscado).build();

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado);
        //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado
        //      + " -> " + Utilidades.stringToUUID( dispositivoBuscado ) );


        elEscanner.startScan(callbackDelEscaneo);
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public static void detenerBusquedaDispositivosBTLE() {

        if ( callbackDelEscaneo == null ) {
            return;
        }

        elEscanner.stopScan( callbackDelEscaneo );
        callbackDelEscaneo = null;

    } // ()


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public static void inicializarBlueTooth(Context context, Activity activity) {
        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos adaptador BT ");

        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitamos adaptador BT ");

        bta.enable();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitado =  " + bta.isEnabled() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): estado =  " + bta.getState() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos escaner btle ");

        elEscanner = bta.getBluetoothLeScanner();

        if ( elEscanner == null ) {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): Socorro: NO hemos obtenido escaner btle  !!!!");

        }

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): voy a perdir permisos (si no los tuviera) !!!!");

        if (
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PETICION_PERMISOS);
        }
        else {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): parece que YA tengo los permisos necesarios !!!!");

        }
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    /*public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    } // ()*/

    //-------------------------------------------------------------
    //-----------------------FIN BTLE------------------------------
    //-------------------------------------------------------------
}
