package com.google.developer.bugmaster.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.developer.bugmaster.R;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String INTENT_FILTER_ON_QUIZ_REMINDER_CHANGE = "intent_filter_on_quiz_reminder_change";

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        //Schedule alarm on BOOT_COMPLETED
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action) ||
                INTENT_FILTER_ON_QUIZ_REMINDER_CHANGE.equals(action)) {
            scheduleAlarm(context);
        }
    }

    /* Schedule the alarm based on user preferences */
    public static void scheduleAlarm(Context context) {

        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        String keyReminder = context.getString(R.string.pref_key_reminder);
        String keyAlarm = context.getString(R.string.pref_key_alarm);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean enabled = preferences.getBoolean(keyReminder, false);

        //Intent to trigger
        Intent intent = new Intent(context, ReminderService.class);
        PendingIntent operation = PendingIntent
                .getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (enabled) {
            //Gather the time preference
            long currentTimeMillis = System.currentTimeMillis();
            Calendar alarmTime = Calendar.getInstance();
            Calendar nowTime = Calendar.getInstance();
            alarmTime.setTimeInMillis(currentTimeMillis);
            nowTime.setTimeInMillis(currentTimeMillis);

            String alarmPref = preferences.getString(keyAlarm, "12:00");
            String[] time = alarmPref.split(":");

            int hoursOfDay = Integer.valueOf(time[0]);
            int minutes = Integer.valueOf(time[1]);
            alarmTime.set(Calendar.HOUR_OF_DAY, hoursOfDay);
            alarmTime.set(Calendar.MINUTE, minutes);
            alarmTime.set(Calendar.SECOND, 0);

            //Start at the preferred time
            //If that time has passed today, set for tomorrow
            if (nowTime.after(alarmTime)) {
                alarmTime.add(Calendar.DATE, 1);
            }

            Log.d(TAG, "Scheduling quiz reminder alarm");
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, operation);
        } else {
            Log.d(TAG, "Disabling quiz reminder alarm");
            manager.cancel(operation);
        }
    }

}
