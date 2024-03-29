package org.mentalizr.infra.tasks.create.tomcat;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.infra.docker.m7r.M7rVolumeTomcat;

public class CreateVolumeTomcat {

    public static Task create() {
        return new TaskBuilder()
                .withName("create-volume-tomcat")
                .withDescription("create docker volume for tomcat")
                .withDependencies("create-network")
                .isUpToDate(M7rVolumeTomcat::exists)
                .execute(M7rVolumeTomcat::create)
                .build();
    }

}
