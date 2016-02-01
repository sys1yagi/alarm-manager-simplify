package com.sys1yagi.android.alarmmanagersimplify.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// workaround for kapt
// @Retention(RetentionPolicy.SOURCE)
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Simplify {

    /**
     * @return Alarm event name.
     */
    String value();

    //boolean grouping() default false;
    //boolean wakeLock() default true;
}
