package org.mentalizr.infra.tasks.stop;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;

public class StopTarget {

    public static Task create() {
        return new TaskBuilder()
                .asTarget()
                .withName("stop")
                .withDescription("stop docker infrastructure")
                .withDependencies("stop-mongo")
                .execute(()-> {})
                .build();
    }

}
