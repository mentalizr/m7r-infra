package org.mentalizr.infra.tasks.start;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;

public class StartTarget {

    public static Task create() {
        return new TaskBuilder()
                .asTarget()
                .withName("start")
                .withDescription("start docker infrastructure")
                .withDependencies("intention-up")
                .execute(()-> {})
                .build();
    }

}
