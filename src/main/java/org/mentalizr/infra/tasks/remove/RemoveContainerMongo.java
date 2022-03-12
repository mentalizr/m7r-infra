package org.mentalizr.infra.tasks.remove;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.docker.m7r.M7rContainerMongo;
import org.mentalizr.infra.docker.m7r.M7rNetwork;

public class RemoveContainerMongo {

    public static Task create() {
        return new TaskBuilder()
                .name("remove-container-mongo")
                .description("remove container mongo")
                .inputChanged(() -> false)
                .outputExists(() -> !M7rContainerMongo.exists())
                .execute(M7rContainerMongo::remove)
                .build();
    }

}
