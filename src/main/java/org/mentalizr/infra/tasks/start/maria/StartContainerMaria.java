package org.mentalizr.infra.tasks.start.maria;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.docker.m7r.M7rContainerMaria;

public class StartContainerMaria {

    public static Task create() {
        return new TaskBuilder()
                .withName("start-container-maria")
                .withDescription("start container [" + Const.CONTAINER_MARIA + "]")
                .isUpToDate(M7rContainerMaria::isRunning)
                .execute(M7rContainerMaria::start)
                .build();
    }

}
