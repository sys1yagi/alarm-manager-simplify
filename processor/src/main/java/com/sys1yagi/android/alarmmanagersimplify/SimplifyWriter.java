package com.sys1yagi.android.alarmmanagersimplify;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

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

//        classBuilder.addMethod(createScheduleRtcWakeup(model));

        TypeSpec outClass = classBuilder.build();
        JavaFile.builder("com.sys1yagi.android.alarmmanagersimplify", outClass)
                .build()
                .writeTo(filer);
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
