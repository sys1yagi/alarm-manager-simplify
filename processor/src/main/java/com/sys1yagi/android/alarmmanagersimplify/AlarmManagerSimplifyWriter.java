package com.sys1yagi.android.alarmmanagersimplify;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;

public class AlarmManagerSimplifyWriter {

    AlarmManagerSimplifyModel model;

    ProcessingEnvironment environment;

    public AlarmManagerSimplifyWriter(ProcessingEnvironment environment, AlarmManagerSimplifyModel model) {
        this.environment = environment;
        this.model = model;
    }

    public void write(Filer filer) throws IOException {

    }

}
