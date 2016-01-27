package com.sys1yagi.android.alarmmanagersimplify;

import com.sys1yagi.android.alarmmanagersimplify.annotation.Simplify;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class AlarmManagerSimplifyModel {

    private TypeElement element;

    private String packageName;

    private String originalClassName;

    private String schedulerClassName;

    private List<VariableElement> processorList = new ArrayList<>();

    public AlarmManagerSimplifyModel(TypeElement element, Elements elementUtils) {
        this.element = element;
        this.packageName = getPackageName(elementUtils, element);
        this.originalClassName = getClassName(element, packageName);
        this.schedulerClassName = originalClassName.concat("Scheduler");
        findAnnotations(element);
    }

    private void findAnnotations(Element element) {
        for (Element enclosedElement : element.getEnclosedElements()) {
            findAnnotations(enclosedElement);

            Simplify simplify = enclosedElement.getAnnotation(Simplify.class);
            if (simplify != null) {
                this.processorList.add((VariableElement) enclosedElement);
            }
        }
    }

    private String getPackageName(Elements elementUtils, TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    public TypeElement getElement() {
        return element;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getOriginalClassName() {
        return originalClassName;
    }

    public String getSchedulerClassName() {
        return schedulerClassName;
    }

    public List<VariableElement> getProcessorList() {
        return processorList;
    }
}
