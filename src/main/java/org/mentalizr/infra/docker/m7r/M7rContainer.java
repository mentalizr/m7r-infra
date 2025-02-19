package org.mentalizr.infra.docker.m7r;

import de.arthurpicht.console.Console;
import de.arthurpicht.processExecutor.ProcessResultCollection;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.DockerExecutionException;
import org.mentalizr.infra.IllegalInfraStateException;
import org.mentalizr.infra.InfraRuntimeException;
import org.mentalizr.infra.appInit.ApplicationContext;
import org.mentalizr.infra.buildEntities.initFiles.InitFile;
import org.mentalizr.infra.docker.Container;
import org.mentalizr.infra.docker.DockerCopy;
import org.mentalizr.infra.docker.DockerExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class M7rContainer {

    private static final Logger logger = LoggerFactory.getLogger(Const.DOCKER_LOGGER);

    public static boolean exists(String name) {
        DockerExecutionContext context = ApplicationContext.getDockerExecutionContext();
        try {
            return Container.exists(context, name);
        } catch (DockerExecutionException e) {
            throw new InfraRuntimeException(e);
        }
    }

    public static boolean isRunning(String name) {
        DockerExecutionContext context = ApplicationContext.getDockerExecutionContext();
        try {
            return Container.isRunning(context, name);
        } catch (DockerExecutionException e) {
            throw new InfraRuntimeException(e);
        }
    }

    public static boolean isStopped(String name) {
        DockerExecutionContext context = ApplicationContext.getDockerExecutionContext();
        try {
            return !Container.isRunning(context, name);
        } catch (DockerExecutionException e) {
            throw new InfraRuntimeException(e);
        }
    }

    public static void start(String name) {
        DockerExecutionContext context = ApplicationContext.getDockerExecutionContext();
        try {
            Container.start(context, name);
        } catch (DockerExecutionException | IllegalInfraStateException e) {
            throw new InfraRuntimeException(e);
        }
    }

    public static void stop(String name) {
        DockerExecutionContext context = ApplicationContext.getDockerExecutionContext();
        try {
            Container.stop(context, name);
        } catch (DockerExecutionException | IllegalInfraStateException e) {
            throw new InfraRuntimeException(e);
        }
    }

    public static void remove(String name) {
        DockerExecutionContext context = ApplicationContext.getDockerExecutionContext();
        try {
            Container.remove(context, name);
        } catch (DockerExecutionException | IllegalInfraStateException e) {
            throw new InfraRuntimeException(e);
        }
    }

    public static InfraRuntimeException createInfraRuntimeException(ProcessResultCollection result) {
        if (result.isSuccess()) throw new IllegalStateException("Result is no fail.");
        String message = "Docker execution finished with error.";
        if (!result.getErrorOut().isEmpty()) message += " " + result.getErrorOut().getFirst();
        return new InfraRuntimeException(message);
    }

    public static void copyInitFileToContainer(InitFile initFile, String containerName, String destinationDir) {
        DockerExecutionContext context = ApplicationContext.getDockerExecutionContext();

        String messageHeader = "Copy configuration file [" + initFile.getFileName() + "] to [" + containerName + "]:";
        Console.printlnVerbose(messageHeader);
        Console.printlnVerbose(initFile.getContent());
        logger.debug(messageHeader);
        logger.debug(initFile.getContent());

        try {
            Path initFilePath = initFile.writeToHostTempDir();
            DockerCopy.copyFileWithPreservedRights(context, initFilePath, containerName, destinationDir);
        } catch (DockerExecutionException | IOException e) {
            throw new InfraRuntimeException(e.getMessage(), e);
        }
    }


}
