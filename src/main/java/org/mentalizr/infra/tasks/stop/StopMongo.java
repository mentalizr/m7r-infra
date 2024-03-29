package org.mentalizr.infra.tasks.stop;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.docker.m7r.M7rContainerMongo;

public class StopMongo {

    public static Task create() {
        return new TaskBuilder()
                .withName("stop-mongo")
                .withDescription("stop mongo")
                .withDependencies("stop-maria")
                .execute(M7rContainerMongo::stop)
                .build();
    }

}
