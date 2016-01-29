package com.sys1yagi.alarmmanagersimplify;

import com.google.testing.compile.JavaFileObjects;

import com.sys1yagi.alarmmanagersimplify.testtool.AssetsUtils;
import com.sys1yagi.android.alarmmanagersimplify.AlarmManagerSimplifyProcessor;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class AlarmManagerSimplifyProcessorTest {

    JavaFileObject readClass(String fileName, String className) {
        String javaFile = AssetsUtils.readString(fileName);

        return JavaFileObjects
                .forSourceString(className, javaFile);
    }

    @Test
    public void invalidClass() throws Exception {
        assert_().about(javaSource())
                .that(readClass("InvalidTestProcessor.java", "TestProcessor"))
                .processedWith(new AlarmManagerSimplifyProcessor())
                .failsToCompile()
                .withErrorContaining(
                        "@Simplify can be defined only if the base class implements com.sys1yagi.android.alarmmanagersimplify.AlarmProcessor. : com.sys1yagi.android.alarmmanagersimplify.TestProcessor");
    }

    @Test
    public void simpleAction() throws Exception {
        assert_().about(javaSource())
                .that(readClass("SimpleActionAlarmProcessor.java", "SimpleActionAlarmProcessor"))
                .processedWith(new AlarmManagerSimplifyProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(
                        readClass("expected/SimpleActionAlarmProcessorScheduler.expected",
                                "SimpleActionAlarmProcessorScheduler"),
                        readClass("expected/SimplifiedAlarmReceiver.expected",
                                "SimplifiedAlarmReceiver"));
    }


}
