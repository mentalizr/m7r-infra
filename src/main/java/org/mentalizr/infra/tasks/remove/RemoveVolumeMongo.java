package org.mentalizr.infra.tasks.remove;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.docker.m7r.M7rNetwork;
import org.mentalizr.infra.docker.m7r.M7rVolumeMongo;

public class RemoveVolumeMongo {

    public static Task create() {
        return new TaskBuilder()
                .name("remove-volume-mongo")
                .description("remove docker volume for mongo")
                .dependencies("remove-container-mongo")
                .inputChanged(() -> false)
                .outputExists(() -> !M7rVolumeMongo.exists())
                .execute(M7rVolumeMongo::remove)
                .build();
    }

}
