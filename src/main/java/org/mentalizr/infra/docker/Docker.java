package org.mentalizr.infra.docker;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.infra.DockerExecutionException;
import org.mentalizr.infra.processExecutor.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Docker {

    public static ProcessResultCollection call(DockerExecutionContext dockerExecutionContext, String... commands) throws DockerExecutionException {
        output(dockerExecutionContext, commands);
        ProcessResultCollection result;
        try {
            result = ProcessExecution.execute(
                    dockerExecutionContext.getLogger(),
                    dockerExecutionContext.isVerbose(),
                    commands);
        } catch (ProcessExecutionException e) {
            throw new DockerExecutionException("Execution of docker command failed: " + e.getMessage(), e);
        }
        if (result.isFail()) throw createException(result);
        return result;
    }

    public static ProcessResultCollection call(DockerExecutionContext dockerExecutionContext, InputStream inputStream, String... commands) throws DockerExecutionException {
        output(dockerExecutionContext, commands);
        ProcessResultCollection result;
        try {
            result = ProcessExecution.execute(
                    dockerExecutionContext.getLogger(),
                    dockerExecutionContext.isVerbose(),
                    inputStream,
                    commands);
        } catch (ProcessExecutionException e) {
            throw new DockerExecutionException("Execution of docker command failed: " + e.getMessage(), e);
        }
        if (result.isFail()) throw createException(result);
        return result;
    }

    public static void output(DockerExecutionContext dockerExecutionContext, String... commands) {
        output(dockerExecutionContext, Arrays.asList(commands));
    }

    public static void output(DockerExecutionContext dockerExecutionContext, List<String> commands) {
        String commandString = "> " + Strings.listing(commands, " ");
        dockerExecutionContext.getLogger().info(commandString);
        if (dockerExecutionContext.isVerbose()) System.out.println(commandString);
    }

    private static DockerExecutionException createException(ProcessResultCollection result) {
        if (result.isSuccess()) throw new IllegalStateException("Docker process not failed.");
        if (!result.getErrorOut().isEmpty()) {
            return new DockerExecutionException(result.getErrorOut().get(0));
        } else {
            return new DockerExecutionException();
        }
    }

}
