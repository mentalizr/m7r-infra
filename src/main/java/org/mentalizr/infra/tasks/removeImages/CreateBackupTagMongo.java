package org.mentalizr.infra.tasks.removeImages;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.docker.m7r.M7rImageMongo;

public class CreateBackupTagMongo {

    public static final String NAME = "create-backup-tag-mongo";

    public static Task create() {
        return new TaskBuilder()
                .withName(NAME)
                .skip(() -> !M7rImageMongo.exists())
                .execute(M7rImageMongo::createBackupTag)
                .build();
    }

}
