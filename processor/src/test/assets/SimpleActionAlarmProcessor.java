package com.sys1yagi.android.alarmmanagersimplify.sample.alarm;

import com.sys1yagi.android.alarmmanagersimplify.AlarmProcessor;
import com.sys1yagi.android.alarmmanagersimplify.annotation.Simplify;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

@Simplify("SimpleAction")
public class SimpleActionAlarmProcessor implements AlarmProcessor {

    private final static String TAG = SimpleActionAlarmProcessor.class.getSimpleName();

    @Override
    public void process(Context context, Intent intent) {
        Log.d(TAG, "Receive");
    }
}
