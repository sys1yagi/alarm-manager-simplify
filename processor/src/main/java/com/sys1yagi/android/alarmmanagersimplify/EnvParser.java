package com.sys1yagi.android.alarmmanagersimplify;


import com.sys1yagi.android.alarmmanagersimplify.annotation.Simplify;
import com.sys1yagi.android.alarmmanagersimplify.exception.IllegalTypeException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class EnvParser {

    public static List<AlarmManagerSimplifyModel> parse(RoundEnvironment env, Elements elementUtils) {

        ArrayList<AlarmManagerSimplifyModel> models = new ArrayList<>();
        ArrayList<Element> elements = new ArrayList<>(
                env.getElementsAnnotatedWith(Simplify.class));
        for (Element element : elements) {
            AlarmManagerSimplifyModel model = new AlarmManagerSimplifyModel((TypeElement) element, elementUtils);
            models.add(model);
        }

        validateAlarmProcessorModel(models);
        return models;
    }

    public static void validateAlarmProcessorModel(List<AlarmManagerSimplifyModel> models) {
        models.forEach(model -> {
            List<? extends TypeMirror> interfaces = model.getElement().getInterfaces();
            for (TypeMirror i : interfaces) {
                if ("com.sys1yagi.android.alarmmanagersimplify.AlarmProcessor".equals(i.toString())) {
                    return;
                }
            }
            throw new IllegalTypeException(
                    "@Simplify can be defined only if the base class implements com.sys1yagi.android.alarmmanagersimplify.AlarmProcessor. : "
                            + model.getElement().toString());
        });
    }
}
