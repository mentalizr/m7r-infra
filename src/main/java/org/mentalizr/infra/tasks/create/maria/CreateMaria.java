package org.mentalizr.infra.tasks.create.maria;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;

public class CreateMaria {

    public static Task create() {
        return new TaskBuilder()
                .isTarget()
                .name("create-maria")
                .description("create maria")
                .dependencies("initialize-container-maria")
                .execute(()-> {})
                .build();
    }

}
