package org.mentalizr.infra.executors;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.taskRunner.TaskRunner;
import de.arthurpicht.taskRunner.runner.TaskRunnerResult;
import org.mentalizr.infra.tasks.InfraTaskRunner;
import org.mentalizr.infra.tasks.scheduler.StopScheduler;
import org.mentalizr.scheduler.processManagement.IntentionFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class SchedulerStopExecutor implements CommandExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerStopExecutor.class);

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {
        logger.debug(SchedulerStopExecutor.class.getSimpleName() + " invoked.");
        TaskRunner taskRunner = InfraTaskRunner.create(cliCall);
        TaskRunnerResult result = taskRunner.run(StopScheduler.NAME);
        if (result.isSuccess()) {
            logger.info("Scheduler successfully stopped.");
        } else {
            logger.error("Stopping scheduler failed. See scheduler log file for details.");
            throw new CommandExecutorException();
        }
    }

}
