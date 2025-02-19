package org.mentalizr.infra.docker;

import de.arthurpicht.processExecutor.ProcessExecutionException;
import de.arthurpicht.processExecutor.ProcessExecutor;
import de.arthurpicht.processExecutor.ProcessExecutorBuilder;
import de.arthurpicht.processExecutor.outputHandler.generalOutputHandler.GeneralStandardErrorHandler;
import de.arthurpicht.processExecutor.outputHandler.generalOutputHandler.GeneralStandardOutHandler;
import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.utils.io.nio2.FileUtils;
import org.mentalizr.infra.DockerExecutionException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DockerCopy {

    public static void copyFile(DockerExecutionContext dockerExecutionContext, Path sourceFile, String containerName, String destinationDir) throws DockerExecutionException {
        String source = sourceFile.toAbsolutePath().toString();
        String target = containerName + ":" + destinationDir;
        Docker.call(dockerExecutionContext, "docker", "cp", source, target);
    }

    public static void copyFileFromContainer(DockerExecutionContext dockerExecutionContext, String containerName, String sourceFile, Path destinationDir) throws DockerExecutionException {
        String source = containerName + ":" + sourceFile;
        String target = destinationDir.toAbsolutePath().toString();
        Docker.call(dockerExecutionContext, "docker", "cp", source, target);
    }

    public static void copyFileWithPreservedRights(DockerExecutionContext context, Path sourceFile, String containerName, String destinationDir) throws DockerExecutionException {
        if (!FileUtils.isExistingRegularFile(sourceFile))
            throw new IllegalArgumentException("File not found: [" + sourceFile.toAbsolutePath() + "].");
        String parentDir = sourceFile.getParent().toString();
        String fileName = sourceFile.getFileName().toString();
        String target = containerName + ":" + destinationDir;
        target = Strings.assureEndsWith(target, "/");

        List<String> commands1 = Arrays.asList("tar", "-cf", "-", "-C", parentDir, fileName, "--mode", "777", "--owner", "root", "--group", "root");
        List<String> commands2 = Arrays.asList("docker", "cp", "-", target);

        List<String> allCommandsForOutput = new ArrayList<>(commands1);
        allCommandsForOutput.add("|");
        allCommandsForOutput.addAll(commands2);
        Docker.userOutput(context, allCommandsForOutput);

        GeneralStandardOutHandler stdOutHandler = new GeneralStandardOutHandler(context.getLogger(), false);
        GeneralStandardErrorHandler stdErrorHandler = new GeneralStandardErrorHandler(context.getLogger(), false);
        ProcessExecutor processExecutor = new ProcessExecutorBuilder()
                .withCommands("tar", "-cf", "-", "-C", parentDir, fileName, "--mode", "777", "--owner", "root", "--group", "root")
                .withPipeToCommands("docker", "cp", "-", target)
                .withStandardOutHandler(stdOutHandler)
                .withStandardErrorHandler(stdErrorHandler)
                .build();
        try {
            int exitCode = processExecutor.execute();
            if (exitCode != 0)
                throw new DockerExecutionException("Docker process finished with non-zero exit code: [" + exitCode + "].");
        } catch (ProcessExecutionException e) {
            throw new DockerExecutionException(e);
        }
    }

}
