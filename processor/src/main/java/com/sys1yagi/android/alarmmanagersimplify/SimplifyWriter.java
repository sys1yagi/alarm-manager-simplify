package com.sys1yagi.android.alarmmanagersimplify;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class SimplifyWriter {

    ProcessingEnvironment environment;

    List<AlarmManagerSimplifyModel> models;

    public SimplifyWriter(ProcessingEnvironment environment, List<AlarmManagerSimplifyModel> models) {
        this.environment = environment;
        this.models = models;
    }

    public void write(Filer filer) throws IOException {
        if (models.isEmpty()) {
            return;
        }
        createReceiver(filer);
        createService(filer);
    }

    void createReceiver(Filer filer) throws IOException {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("SimplifiedAlarmReceiver");
        classBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        classBuilder.superclass(ClassName.get(WakefulBroadcastReceiver.class));

        classBuilder.addMethod(createOnReceive());

        TypeSpec outClass = classBuilder.build();
        JavaFile.builder("com.sys1yagi.android.alarmmanagersimplify", outClass)
                .build()
                .writeTo(filer);
    }

    MethodSpec createOnReceive() {
        return MethodSpec.methodBuilder("onReceive")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(Context.class), "context")
                .addParameter(ClassName.get(Intent.class), "intent")
                .addStatement("$T component = new ComponentName(context.getPackageName(), $S)",
                        ClassName.get(ComponentName.class),
                        "com.sys1yagi.android.alarmmanagersimplify.SimplifiedAlarmService"
                )
                .addStatement("intent.setComponent(component)")
                .addStatement("startWakefulService(context, intent)")
                .addStatement("setResultCode($T.RESULT_OK)", ClassName.get(Activity.class))
                .build();
    }


    void createService(Filer filer) throws IOException {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("SimplifiedAlarmService");
        classBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

//        classBuilder.addMethod(createScheduleRtcWakeup(model));

        TypeSpec outClass = classBuilder.build();
        JavaFile.builder("com.sys1yagi.android.alarmmanagersimplify", outClass)
                .build()
                .writeTo(filer);
    }

}
