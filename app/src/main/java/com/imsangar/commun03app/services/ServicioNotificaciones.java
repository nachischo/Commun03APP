package com.imsangar.commun03app.services;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.imsangar.commun03app.R;
import com.imsangar.commun03app.RESTrequest.PeticionarioREST;
import com.imsangar.commun03app.RESTrequest.REST;
import com.imsangar.commun03app.beaconManagement.counters;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ServicioNotificaciones extends BroadcastReceiver {
    private static final String PREVIOUS_VALUE = "previous_value";
    private static final String LAST_CHECK_TIME = "last_check_time";
    private static final long CHECK_INTERVAL = 360000;//1min//1800000;//30 min//AlarmManager.INTERVAL_HOUR; // 1 hour
    private static final String CHANNEL_ID = "variable_change_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        double previousValue = prefs.getFloat(PREVIOUS_VALUE, 0);
        long lastCheckTime = prefs.getLong(LAST_CHECK_TIME, 0);
        Log.d("servicionotificaciones", "llego a onreceive");

        // Get the current value of the variable
        double currentValue = getCurrentValue();
        final DecimalFormat df = new DecimalFormat("0.00");

        Log.d("servicionotificaciones", "anterior valor para comparación: "+df.format(previousValue));

        // Check if the value has not changed and the time since the last
        //check has exceeded the determined period
        if (df.format(currentValue).equals(df.format(previousValue)) && System.currentTimeMillis() - lastCheckTime > CHECK_INTERVAL) {

            SharedPreferences userPrefs = context.getSharedPreferences("shared_prefs",MODE_PRIVATE);
            SharedPreferences.Editor editarPreferencias = userPrefs.edit();

            editarPreferencias.putBoolean("SensorActivo", false);
            editarPreferencias.commit();

            Log.d("servicionotificaciones", String.valueOf(userPrefs.getBoolean("SensorActivo", true)));

            // Create a notification channel if running on API level 26 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "R.string.channel_name";
                String description = "R.string.channel_description";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                channel.setSound(Uri.parse("content://settings/system/notification_sound"), Notification.AUDIO_ATTRIBUTES_DEFAULT);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            // Create and display the notification
            NotificationCompat.Builder notification = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("Problemas con el dispositivo CommunO3®")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Parece que la conexión con el dispositivo de medición de calidad del aire ha fallado. Por favor comprueba que se encuentra encendido y cerca del smartphone."))
                        .setSmallIcon(R.drawable.nuevo_logo_sin_nombre)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 0, 200, 300, 400 })
                        .setLights(Color.BLUE, 3000, 3000)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            }
            if(!userPrefs.getBoolean("SensorActivo", true)){
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(0, notification.build());
                REST.postSensorActivo(false, userPrefs.getString("nickname",""));
            }

            else {
                REST.postSensorActivo(true, userPrefs.getString("nickname",""));
            }
            /*
            // Update the stored values for the next check
            prefs.edit()
                    .putFloat(PREVIOUS_VALUE, (float)currentValue)
                    .putLong(LAST_CHECK_TIME, System.currentTimeMillis())
                    .apply();*/
        }

        // Update the stored values for the next check
        prefs.edit()
                .putFloat(PREVIOUS_VALUE, (float)currentValue)
                .putLong(LAST_CHECK_TIME, System.currentTimeMillis())
                .apply();
    }

            private double getCurrentValue() {
        // Replace this with code to retrieve the current value of the variable
                final DecimalFormat df = new DecimalFormat("0.00");
                Log.d("ServicioNotificaciones", "getCurrentValue: "+df.format(counters.anteriorValorMedicion));
                return counters.anteriorValorMedicion;
            }
}
