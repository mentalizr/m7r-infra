package org.mentalizr.infra.tasks.stop;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;

public class StopMaria {

    public static Task create() {
        return new TaskBuilder()
                .name("stop-maria")
                .description("stop maria")
                .dependencies("stop-tomcat")
                .execute(() -> {})
                .build();
    }

}
