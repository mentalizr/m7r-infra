package org.mentalizr.infra.tasks.stop;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.mentalizr.scheduler.processManagement.IntentionFile;

public class IntentionDown {

    public static Task create() {
        return new TaskBuilder()
                .withName("intention-down")
                .withDescription("set intention as down")
                .withDependencies("stop-mongo")
                .execute(IntentionFile::createDown)
                .build();
    }

}
