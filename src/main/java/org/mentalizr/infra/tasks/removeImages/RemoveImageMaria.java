package org.mentalizr.infra.tasks.removeImages;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.docker.m7r.M7rImageMaria;

public class RemoveImageMaria {

    public static Task create() {
        return new TaskBuilder()
                .name("remove-image-maria")
                .dependencies("create-backup-tag-maria")
                .isUpToDate(() -> !M7rImageMaria.exists())
                .execute(M7rImageMaria::remove)
                .build();
    }

}
