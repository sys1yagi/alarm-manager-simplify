package com.sys1yagi.android.alarmmanagersimplify.sample.simplify;

import com.sys1yagi.android.alarmmanagersimplify.AlarmProcessor;
import com.sys1yagi.android.alarmmanagersimplify.sample.alarm.SimpleActionAlarmProcessor;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// generated code
public class SimplifiedAlarmService extends IntentService {

    static List<AlarmProcessor> processors;

    static Map<AlarmProcessor, String> processingMap;

    public SimplifiedAlarmService() {
        super("SimplifiedAlarmService");
    }

    void initializeIfNeeded() {
        if (processors != null) {
            return;
        }
        processors = new ArrayList<>();
        processingMap = new HashMap<>();

        AlarmProcessor processor = new SimpleActionAlarmProcessor();
        processors.add(processor);
        processingMap.put(processor, "SimpleAction");
    }

    boolean shouldProcessEvent(AlarmProcessor processor, String event) {
        return processingMap.get(processor).equals(event);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeIfNeeded();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String event = intent.getStringExtra("event");
        for (AlarmProcessor processor : processors) {
            if (shouldProcessEvent(processor, event)) {
                processor.process(getApplicationContext(), intent);
            }
        }
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }
}
