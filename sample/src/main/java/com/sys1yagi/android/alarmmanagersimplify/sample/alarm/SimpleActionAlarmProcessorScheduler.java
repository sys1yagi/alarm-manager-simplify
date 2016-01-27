package com.sys1yagi.android.alarmmanagersimplify.sample.alarm;

import com.sys1yagi.android.alarmmanagersimplify.sample.simplify.SimplifiedAlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

// generated code
public class SimpleActionAlarmProcessorScheduler {

    public static void scheduleRtcWakeup(Context context, int interval) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        now.add(Calendar.MILLISECOND, interval);

        Intent intent = new Intent(context, SimplifiedAlarmReceiver.class);
        intent.putExtra("event", "SimpleAction");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent);
    }
}
