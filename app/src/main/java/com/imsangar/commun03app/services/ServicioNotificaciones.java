package com.imsangar.commun03app.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.imsangar.commun03app.MainActivity;
import com.imsangar.commun03app.R;
import com.imsangar.commun03app.fragments.HomeFragment;

public class ServicioNotificaciones extends IntentService {

    public static Long ultimoDatoEnviado = null;
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


}