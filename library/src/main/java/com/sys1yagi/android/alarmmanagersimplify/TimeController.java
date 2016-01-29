package com.sys1yagi.android.alarmmanagersimplify;

import java.util.Calendar;

public class TimeController {

    private final static Calendar CALENDAR = Calendar.getInstance();

    public static Calendar now() {
        CALENDAR.setTimeInMillis(System.currentTimeMillis());
        return CALENDAR;
    }

    public static long millisAfter(int interval) {
        Calendar now = now();
        now.add(Calendar.MILLISECOND, interval);
        return now.getTimeInMillis();
    }
}
