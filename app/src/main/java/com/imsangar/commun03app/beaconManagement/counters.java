package com.imsangar.commun03app.beaconManagement;

import static java.lang.Math.pow;

import com.imsangar.commun03app.services.ServicioNotificaciones;

public class counters {
    static int BeaconCounter = 1;
    static int AnteriorValorBruto = 0;
    public static double anteriorValorMedicion = 0.0;
    static double anteriorValorMedicionOffset = -1;
    public static double Voffset = -1;
    static double CalibracionDelSensor = 48.31;
    static double Vcalibracion = 0.0;
    static double anteriorValorBrutoO3 = 0.0;
    static double anteriorValorTemperatura = 0.0;


    static double calculaO3(int valorBrutoMedicion) {
        double valorBruto = valorBrutoMedicion * 3.3 / 4096;
        Vcalibracion = CalibracionDelSensor * 499 * pow(10, -6);
        double res = valorBruto / Vcalibracion;
        return Math.round(res * 100.0) / 100.0;
    }

    static double calcula03ConOffset(int valorBrutoMedicion) {
        double valorBruto = valorBrutoMedicion * 3.3 / 4096 - Voffset;
        double res = valorBruto / Vcalibracion;
        return res;
    }

    static double calculaTemperatura(int valorBrutoTemperatura) {
        double valorBruto = valorBrutoTemperatura * 3.3 / 4096;
        double res = valorBruto * 29 - 18;
        return Math.round(res);
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
