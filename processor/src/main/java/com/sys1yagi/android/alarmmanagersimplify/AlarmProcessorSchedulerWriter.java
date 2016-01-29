package com.sys1yagi.android.alarmmanagersimplify;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sys1yagi.android.alarmmanagersimplify.annotation.Simplify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class AlarmProcessorSchedulerWriter {

    AlarmManagerSimplifyModel model;

    ProcessingEnvironment environment;

    public AlarmProcessorSchedulerWriter(ProcessingEnvironment environment, AlarmManagerSimplifyModel model) {
        this.environment = environment;
        this.model = model;
    }

    public void write(Filer filer) throws IOException {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(model.getSchedulerClassName());
        classBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        classBuilder.addMethod(createScheduleRtcWakeup(model));

        TypeSpec outClass = classBuilder.build();
        JavaFile.builder(model.getPackageName(), outClass)
                .build()
                .writeTo(filer);
    }

    MethodSpec createScheduleRtcWakeup(AlarmManagerSimplifyModel model) {

        Simplify simplify = model.getElement().getAnnotation(Simplify.class);

        return MethodSpec.methodBuilder("scheduleRtcWakeup")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(ClassName.get(Context.class), "context")
                .addParameter(int.class, "interval")
                .beginControlFlow("try")
                .addStatement(
                        "$T alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE)",
                        ClassName.get(AlarmManager.class))
                .addStatement(
                        "$T intent = new Intent(context, Class.forName($S))",
                        ClassName.get(Intent.class),
                        "com.sys1yagi.android.alarmmanagersimplify.SimplifiedAlarmReceiver")
                .addStatement("intent.putExtra(\"event\", $S)", simplify.value())
                .addStatement("$T pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)",
                        ClassName.get(PendingIntent.class))
                .addStatement("alarmManager.cancel(pendingIntent)")
                .addStatement("alarmManager.set(AlarmManager.RTC_WAKEUP, $T.millisAfter(interval), pendingIntent)",
                        ClassName.get(TimeController.class))
                .nextControlFlow("catch($T e)", ClassNotFoundException.class) // ★
                .addStatement("throw new IllegalStateException(e)")
                .endControlFlow()
                .build();
    }
}
