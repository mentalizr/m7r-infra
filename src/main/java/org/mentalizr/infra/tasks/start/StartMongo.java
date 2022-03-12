package org.mentalizr.infra.tasks.start;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.docker.m7r.M7rContainerMongo;

public class StartMongo {

    public static Task create() {
        return new TaskBuilder()
                .name("start-mongo")
                .description("start mongo")
                .execute(M7rContainerMongo::start)
                .build();
    }

}
