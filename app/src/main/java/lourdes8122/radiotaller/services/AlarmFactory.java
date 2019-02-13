package lourdes8122.radiotaller.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.TimeZone;

import androidx.core.app.AlarmManagerCompat;
import lourdes8122.radiotaller.model.Horario;

import static androidx.core.content.ContextCompat.getSystemService;


public class AlarmFactory {

    public static void generarAlarma(Context context, Horario horario){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("ART"));
        calendar.set(Calendar.DAY_OF_WEEK,horario.getDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY,horario.getTime().getHours());
        calendar.set(Calendar.MINUTE,horario.getTime().getMinutes());
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);


        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, alarmIntent);


        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void cancelarAlarma(Context context){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.cancel(alarmIntent);

    }
}

