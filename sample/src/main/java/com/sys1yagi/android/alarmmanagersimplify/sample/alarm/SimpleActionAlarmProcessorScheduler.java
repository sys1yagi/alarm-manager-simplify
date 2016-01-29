package com.sys1yagi.android.alarmmanagersimplify.sample.alarm;

import com.sys1yagi.android.alarmmanagersimplify.TimeController;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

// generated code
public class SimpleActionAlarmProcessorScheduler {

    public static void scheduleRtcWakeup(Context context, int interval) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context,
                    Class.forName("com.sys1yagi.android.alarmmanagersimplify.sample.simplify.SimplifiedAlarmReceiver"));
            intent.putExtra("event", "SimpleAction");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
            alarmManager.cancel(pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, TimeController.millisAfter(interval), pendingIntent);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
