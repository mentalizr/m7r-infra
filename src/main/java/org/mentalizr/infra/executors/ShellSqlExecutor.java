package org.mentalizr.infra.executors;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.utils.core.collection.Lists;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.DockerExecutionException;
import org.mentalizr.infra.IllegalInfraStateException;
import org.mentalizr.infra.appInit.ApplicationContext;
import org.mentalizr.infra.docker.DockerExecutionContext;
import org.mentalizr.infra.docker.Shell;

import java.util.List;

public class ShellSqlExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {
        System.out.println("open shell on container [" + Const.CONTAINER_MARIA + "] ...");

        DockerExecutionContext dockerExecutionContext = ApplicationContext.getDockerExecutionContext();
        List<String> executionCommands = Lists.newArrayList("sh", "-c", "mariadb -u root -p");
        try {
            Shell.open(dockerExecutionContext, Const.CONTAINER_MARIA, executionCommands);
        } catch (DockerExecutionException | IllegalInfraStateException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

}
