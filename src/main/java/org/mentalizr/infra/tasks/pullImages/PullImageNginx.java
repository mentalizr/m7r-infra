package org.mentalizr.infra.tasks.pullImages;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.docker.m7r.M7rImageNginx;

public class PullImageNginx {

    public static final String NAME = "pull-image-nginx";

    public static Task create() {
        return new TaskBuilder()
                .withName(NAME)
                .execute(M7rImageNginx::pull)
                .build();
    }

}
