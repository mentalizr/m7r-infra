package org.mentalizr.infra.executors;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.taskRunner.TaskRunner;
import de.arthurpicht.taskRunner.runner.TaskRunnerResult;
import org.mentalizr.infra.tasks.InfraTaskRunner;
import org.mentalizr.infra.tasks.scheduler.StartScheduler;
import org.mentalizr.scheduler.processManagement.IntentionFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class SchedulerStartExecutor implements CommandExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerStartExecutor.class);

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {
        logger.debug(SchedulerStartExecutor.class.getSimpleName() + " invoked.");
        TaskRunner taskRunner = InfraTaskRunner.create(cliCall);
        TaskRunnerResult result = taskRunner.run(StartScheduler.NAME);
        if (result.isSuccess()) {
            logger.info("Scheduler successfully started.");
        } else {
            logger.error("Starting scheduler failed. See scheduler log file for details.");
            throw new CommandExecutorException();
        }
    }

}
