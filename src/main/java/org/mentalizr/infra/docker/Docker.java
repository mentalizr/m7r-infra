package org.mentalizr.infra.docker;

import de.arthurpicht.console.Console;
import de.arthurpicht.processExecutor.ProcessExecutionException;
import de.arthurpicht.processExecutor.ProcessResultCollection;
import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.infra.DockerExecutionException;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Docker {

    public static ProcessResultCollection call(
            DockerExecutionContext dockerExecutionContext,
            String... commands)
            throws DockerExecutionException {

        userOutput(dockerExecutionContext, commands);
        ProcessResultCollection result;
        try {
            result = DockerProcessExecution.execute(
                    dockerExecutionContext,
                    commands);
        } catch (ProcessExecutionException e) {
            throw new DockerExecutionException("Execution of docker command failed: " + e.getMessage(), e);
        }
        if (result.isFail()) throw createException(result);
        return result;
    }

    public static ProcessResultCollection call(
            DockerExecutionContext dockerExecutionContext,
            InputStream inputStream,
            String... commands)
            throws DockerExecutionException {

        userOutput(dockerExecutionContext, commands);
        ProcessResultCollection result;
        try {
            result = DockerProcessExecution.execute(
                    dockerExecutionContext,
                    inputStream,
                    commands);
        } catch (ProcessExecutionException e) {
            throw new DockerExecutionException("Execution of docker command failed: " + e.getMessage(), e);
        }
        if (result.isFail()) throw createException(result);
        return result;
    }

    public static void userOutput(DockerExecutionContext dockerExecutionContext, String... commands) {
        userOutput(dockerExecutionContext, Arrays.asList(commands));
    }

    public static void userOutput(DockerExecutionContext dockerExecutionContext, List<String> commands) {
        String commandString = "> " + Strings.listing(commands, " ");
        dockerExecutionContext
                .getLogger()
                .info(commandString);
        Console.printlnVerbose(commandString);
    }

    private static DockerExecutionException createException(ProcessResultCollection result) {
        if (result.isSuccess()) throw new IllegalStateException("Docker process not failed.");
        if (!result.getErrorOut().isEmpty()) {
            return new DockerExecutionException(result.getErrorOut().getFirst());
        } else {
            return new DockerExecutionException();
        }
    }

}
