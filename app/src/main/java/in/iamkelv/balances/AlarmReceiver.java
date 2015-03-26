// TODO: DEBUG AND FIX ALARMS

package in.iamkelv.balances;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

import in.iamkelv.balances.models.PreferencesModel;

public class AlarmReceiver extends WakefulBroadcastReceiver{

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private PreferencesModel preferences;


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, SchedulingService.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set alarm trigger time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        preferences = new PreferencesModel(context);
        calendar.set(Calendar.HOUR_OF_DAY, preferences.getNotificationHours());
        calendar.set(Calendar.MINUTE, preferences.getNotificationMinutes());

        // Set alarm to fire at specified time, rebooting once per day
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        // Enable automatic restart on device reboot
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }

        // Disable BootReceiver
        ComponentName receiver = new ComponentName(context, BootReceiver.class.getSimpleName());
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
