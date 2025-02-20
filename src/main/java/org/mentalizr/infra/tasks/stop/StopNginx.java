package org.mentalizr.infra.tasks.stop;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.docker.m7r.M7rContainerNginx;

public class StopNginx {

    public static Task create() {
        return new TaskBuilder()
                .withName("stop-nginx")
                .withDescription("stop nginx")
                .withDependencies("intention-down")
                .execute(M7rContainerNginx::stop)
                .build();
    }

}
