package com.sys1yagi.android.alarmmanagersimplify;

import com.google.auto.service.AutoService;

import com.sys1yagi.android.alarmmanagersimplify.annotation.Simplify;
import com.sys1yagi.android.alarmmanagersimplify.exception.IllegalTypeException;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class AlarmManagerSimplifyProcessor extends AbstractProcessor {

    private Elements elementUtils;

    private Filer filer;

    private Messager messager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new LinkedHashSet<String>() {{
            add(Simplify.class.getName());
        }};
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        try {
            List<AlarmManagerSimplifyModel> models = EnvParser.parse(env, elementUtils);
            // Generate SimplifyAlarmReceiver, SimplifyAlarmService
            SimplifyWriter simplifyWriter = new SimplifyWriter(processingEnv, models);
            try {
                simplifyWriter.write(filer);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }

            // Generate AlarmProcessorScheduler
            models.forEach(model -> {
                {
                    AlarmProcessorSchedulerWriter writer = new AlarmProcessorSchedulerWriter(processingEnv, model);
                    try {
                        writer.write(filer);
                    } catch (IOException e) {
                        messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                    }
                }
            });
        } catch (IllegalTypeException e) {
            error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.elementUtils = env.getElementUtils();
        this.filer = env.getFiler();
        this.messager = env.getMessager();
    }

    private void error(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

}
