package com.sys1yagi.android.alarmmanagersimplify.sample;


import com.sys1yagi.android.alarmmanagersimplify.sample.alarm.SimpleActionAlarmProcessorScheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleActionAlarmProcessorScheduler.scheduleRtcWakeup(MainActivity.this, 1000);
            }
        });
    }
}
