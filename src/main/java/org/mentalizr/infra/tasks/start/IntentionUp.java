package org.mentalizr.infra.tasks.start;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.scheduler.processManagement.IntentionFile;

public class IntentionUp {

    public static Task create() {
        return new TaskBuilder()
                .withName("intention-up")
                .withDescription("set intention as up")
                .withDependencies("start-nginx")
                .execute(IntentionFile::createUp)
                .build();
    }

}
