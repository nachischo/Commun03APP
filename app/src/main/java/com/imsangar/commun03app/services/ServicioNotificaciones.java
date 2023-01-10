package com.imsangar.commun03app.services;

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
import com.imsangar.commun03app.beaconManagement.counters;

public class ServicioNotificaciones extends BroadcastReceiver {
    private static final String PREVIOUS_VALUE = "previous_value";
    private static final String LAST_CHECK_TIME = "last_check_time";
    private static final long CHECK_INTERVAL = 1800000;//AlarmManager.INTERVAL_HOUR; // 1 hour
    private static final String CHANNEL_ID = "variable_change_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        double previousValue = prefs.getFloat(PREVIOUS_VALUE, 0);
        long lastCheckTime = prefs.getLong(LAST_CHECK_TIME, 0);
        Log.d("servicionotificaciones", "llego a onreceive");

        // Get the current value of the variable
        double currentValue = getCurrentValue();

        // Check if the value has not changed and the time since the last
        //check has exceeded the determined period
        if (currentValue == previousValue && System.currentTimeMillis() - lastCheckTime > CHECK_INTERVAL) {
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
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0, notification.build());
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
        Log.d("ServicioNotificaciones", "getCurrentValue: "+counters.anteriorValorMedicion);
        return counters.anteriorValorMedicion;
    }
}
    /*public static Long ultimoDatoEnviado = null;
    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public ServicioNotificaciones() {
        super(ServicioNotificaciones.class.getSimpleName());
    }



    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, ServicioNotificaciones.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, ServicioNotificaciones.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification();
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }


    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ns")
                .setSmallIcon(R.drawable.nuevo_logo_sin_nombre)
                .setContentTitle("CommunO3")
                .setContentText("Parece que hay un error de conexión con el dispositivo de medición. Por favor comprueba que está encendido y cerca de tu smartphone.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        if((ultimoDatoEnviado-System.currentTimeMillis()/1000)>60){
            HomeFragment.createNotificationChannel();
            final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, builder.build());
        }

    }


}*/