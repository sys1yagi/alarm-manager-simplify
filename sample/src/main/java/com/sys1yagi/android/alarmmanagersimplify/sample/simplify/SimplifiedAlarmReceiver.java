package com.sys1yagi.android.alarmmanagersimplify.sample.simplify;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

// generated code
public class SimplifiedAlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                SimplifiedAlarmService.class.getName());
        intent.setComponent(comp);
        startWakefulService(context, intent);
        setResultCode(Activity.RESULT_OK);
    }
}
