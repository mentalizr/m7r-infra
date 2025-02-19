package org.mentalizr.infra.docker;

import de.arthurpicht.processExecutor.ProcessExecutionException;
import de.arthurpicht.processExecutor.ProcessExecutor;
import de.arthurpicht.processExecutor.ProcessExecutorBuilder;
import de.arthurpicht.processExecutor.ProcessResultCollection;
import de.arthurpicht.processExecutor.outputHandler.generalOutputHandler.GeneralStandardErrorHandler;
import de.arthurpicht.processExecutor.outputHandler.generalOutputHandler.GeneralStandardOutHandler;

import java.io.InputStream;

public class DockerProcessExecution {

    public static ProcessResultCollection execute(
            DockerExecutionContext dockerExecutionContext,
            String... commands)
            throws ProcessExecutionException {

        GeneralStandardOutHandler standardOutHandler = new GeneralStandardOutHandler.Builder()
                .withLogger(dockerExecutionContext.getLogger())
                .withLogLevel(dockerExecutionContext.getLogLevelStdOut())
                .withConsoleOutput(false)
                .build();
        GeneralStandardErrorHandler standardErrorHandler = new GeneralStandardErrorHandler.Builder()
                .withLogger(dockerExecutionContext.getLogger())
                .withLogLevel(dockerExecutionContext.getLogLevelStdErr())
                .withConsoleOutput(false)
                .build();
        ProcessExecutor processExecutor = new ProcessExecutorBuilder()
                .withCommands(commands)
                .withStandardOutHandler(standardOutHandler)
                .withStandardErrorHandler(standardErrorHandler)
                .build();
        processExecutor.execute();
        return new ProcessResultCollection(processExecutor, standardOutHandler, standardErrorHandler);
    }

    public static ProcessResultCollection execute(
            DockerExecutionContext dockerExecutionContext,
            InputStream inputStream,
            String... commands)
            throws ProcessExecutionException {

        GeneralStandardOutHandler standardOutHandler = new GeneralStandardOutHandler.Builder()
                .withLogger(dockerExecutionContext.getLogger())
                .withLogLevel(dockerExecutionContext.getLogLevelStdOut())
                .withConsoleOutput(false)
                .build();
        GeneralStandardErrorHandler standardErrorHandler = new GeneralStandardErrorHandler.Builder()
                .withLogger(dockerExecutionContext.getLogger())
                .withLogLevel(dockerExecutionContext.getLogLevelStdErr())
                .withConsoleOutput(false)
                .build();
        ProcessExecutor processExecutor = new ProcessExecutorBuilder()
                .withCommands(commands)
                .withInput(inputStream)
                .withStandardOutHandler(standardOutHandler)
                .withStandardErrorHandler(standardErrorHandler)
                .build();
        processExecutor.execute();
        return new ProcessResultCollection(processExecutor, standardOutHandler, standardErrorHandler);
    }

}
