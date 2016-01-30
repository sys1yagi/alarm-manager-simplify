package com.sys1yagi.android.alarmmanagersimplify;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import android.app.Activity;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        classBuilder.superclass(ClassName.get(IntentService.class));

        classBuilder.addFields(createFields());
        classBuilder.addMethod(createConstructor());
        classBuilder.addMethod(createInitializeIfNeeded(models));
        classBuilder.addMethod(createShouldProcessEvent());
        classBuilder.addMethod(createOnCreate());
        classBuilder.addMethod(createOnHandleIntent());

        TypeSpec outClass = classBuilder.build();
        JavaFile.builder("com.sys1yagi.android.alarmmanagersimplify", outClass)
                .build()
                .writeTo(filer);
    }

    List<FieldSpec> createFields() {
        List<FieldSpec> fieldSpecs = new ArrayList<>();

        ParameterizedTypeName processors = ParameterizedTypeName.get(List.class, AlarmProcessor.class);
        fieldSpecs.add(FieldSpec.builder(processors, "processors", Modifier.PRIVATE, Modifier.STATIC).build());

        ParameterizedTypeName processingMap = ParameterizedTypeName.get(Map.class, AlarmProcessor.class, String.class);
        fieldSpecs.add(FieldSpec.builder(processingMap, "processingMap", Modifier.PRIVATE, Modifier.STATIC).build());

        return fieldSpecs;
    }

    MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super($S)", "SimplifiedAlarmService")
                .build();
    }

    MethodSpec createInitializeIfNeeded(List<AlarmManagerSimplifyModel> models) {
        ParameterizedTypeName processors = ParameterizedTypeName.get(ArrayList.class, AlarmProcessor.class);
        ParameterizedTypeName processingMap = ParameterizedTypeName
                .get(HashMap.class, AlarmProcessor.class, String.class);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("initializeIfNeeded")
                .beginControlFlow("if(processors != null)")
                .addStatement("return")
                .endControlFlow()
                .addStatement("processors = new $T()", processors)
                .addStatement("processingMap = new $T()", processingMap);

        builder.addStatement("$T processor", ClassName.get(AlarmProcessor.class));
        models.forEach(model -> {
            builder
                    .addStatement("processor = new $T()", ClassName.get(model.getElement()))
                    .addStatement("processors.add(processor)")
                    .addStatement("processingMap.put(processor, $S)", model.getSimplify().value());
        });

        return builder.build();
    }

    MethodSpec createShouldProcessEvent() {
        return MethodSpec.methodBuilder("shouldProcessEvent")
                .addParameter(ClassName.get(AlarmProcessor.class), "processor")
                .addParameter(ClassName.get(String.class), "event")
                .returns(boolean.class)
                .addStatement("return processingMap.get(processor).equals(event)")
                .build();
    }

    MethodSpec createOnCreate() {
        return MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("super.onCreate()")
                .addStatement("initializeIfNeeded()")
                .build();
    }

    MethodSpec createOnHandleIntent() {
        return MethodSpec.methodBuilder("onHandleIntent")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .addParameter(ClassName.get(Intent.class), "intent")
                .addStatement("String event = intent.getStringExtra($S)", "event")
                .beginControlFlow("for(AlarmProcessor processor : processors)")
                .beginControlFlow("if (shouldProcessEvent(processor, event))")
                .addStatement("processor.process(getApplicationContext(), intent)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("$T.completeWakefulIntent(intent)", ClassName.get(WakefulBroadcastReceiver.class))
                .build();
    }
}
