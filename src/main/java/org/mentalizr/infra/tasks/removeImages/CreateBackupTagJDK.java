package org.mentalizr.infra.tasks.removeImages;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.docker.m7r.M7rImageJDK;

public class CreateBackupTagJDK {

    public static final String NAME = "create-backup-tag-jdk";

    public static Task create() {
        return new TaskBuilder()
                .withName(NAME)
                .skip(() -> !M7rImageJDK.exists())
                .execute(M7rImageJDK::createBackupTag)
                .build();
    }

}
