package org.mentalizr.infra.executors;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.taskRunner.TaskRunner;
import de.arthurpicht.taskRunner.runner.TaskRunnerResult;
import org.mentalizr.commons.M7rDirs;
import org.mentalizr.infra.ApplicationContext;
import org.mentalizr.infra.InfraCli;
import org.mentalizr.infra.InfraConfigFile;
import org.mentalizr.infra.tasks.InfraTaskRunner;

public class PullUpExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {
        ApplicationContext.initialize(cliCall);

        System.out.println("pullUp called!");

        boolean verbose = cliCall.getOptionParserResultGlobal().hasOption(InfraCli.OPTION_VERBOSE);

        try {
            ExecutionPreconditions.check(verbose);
        } catch (ExecutionPreconditionFailedException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }

        InfraConfigFile infraConfigFile = new InfraConfigFile(new M7rDirs());
        String m7rInfraConfigFile = infraConfigFile.getInfraConfigFile().toAbsolutePath().toString();
        System.setProperty("m7r.config", m7rInfraConfigFile);

        TaskRunner taskRunner = InfraTaskRunner.create(cliCall);
        TaskRunnerResult result = taskRunner.run("create");
        if (result.isSuccess()) result = taskRunner.run("start");
    }

}
