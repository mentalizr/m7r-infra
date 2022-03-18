package org.mentalizr.infra.executors;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import org.mentalizr.infra.ApplicationContext;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.DockerExecutionException;
import org.mentalizr.infra.IllegalInfraStateException;
import org.mentalizr.infra.docker.DockerExecutionContext;
import org.mentalizr.infra.docker.Shell;
import org.mentalizr.infra.docker.m7r.M7rContainerMongo;

public class ShellMongoExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {
        ApplicationContext.initialize(cliCall);

        System.out.println("open shell on container [" + Const.CONTAINER_MONGO + "] ...");

        DockerExecutionContext dockerExecutionContext = ApplicationContext.getDockerExecutionContext();
        try {
            Shell.open(dockerExecutionContext, Const.CONTAINER_MONGO);
        } catch (DockerExecutionException | IllegalInfraStateException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }

    }

}