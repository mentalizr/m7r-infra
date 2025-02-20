package org.mentalizr.infra.appInit;

import ch.qos.logback.classic.Level;
import de.arthurpicht.processExecutor.ProcessExecution;
import de.arthurpicht.processExecutor.ProcessExecutionException;
import de.arthurpicht.processExecutor.ProcessResultCollection;
import de.arthurpicht.utils.logging.LoggerInit;
import org.mentalizr.commons.paths.M7rDir;
import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.commons.paths.client.M7rClientCliConfigFile;
import org.mentalizr.commons.paths.client.M7rClientCredentialsFile;
import org.mentalizr.commons.paths.host.hostDir.M7rHostLogDir;
import org.mentalizr.commons.paths.host.hostDir.M7rInfraUserConfigFile;
import org.mentalizr.commons.paths.host.hostDir.M7rPrivateKeyFile;
import org.mentalizr.commons.paths.host.hostDir.M7rSslCertFile;
import org.mentalizr.infra.GlobalOptions;

import java.io.IOException;
import java.nio.file.Path;

public class ApplicationInitialization {

    public static void execute(GlobalOptions globalOptions) throws ApplicationInitializationException {
        assertExistsM7rFile(new M7rInfraUserConfigFile());
        assertExistsM7rFile(new M7rSslCertFile());
        assertExistsM7rFile(new M7rPrivateKeyFile());

        assertExistsM7rFile(M7rClientCliConfigFile.createInstance());
        assertExistsM7rFile(M7rClientCredentialsFile.createInstance());

        assertCommand("docker");
        assertCommand("git");

        createLogDir();
        initLogging();
        ApplicationContext.initialize(globalOptions);
    }

    private static void assertExistsM7rDir(M7rDir m7rDir) throws ApplicationInitializationException {
        if (!m7rDir.exists())
            throw new ApplicationInitializationException(
                    m7rDir.getDescription()  + " not found: [" + m7rDir.toAbsolutePathString() + "].");
    }

    private static void assertExistsM7rFile(M7rFile m7rFile) throws ApplicationInitializationException {
        if (!m7rFile.exists())
            throw new ApplicationInitializationException(
                    m7rFile.getDescription()  + " not found: [" + m7rFile.toAbsolutePathString() + "].");
    }

    private static void assertCommand(String command) throws ApplicationInitializationException {
        try {
            ProcessResultCollection result = ProcessExecution.execute("which", command);
            if (result.getExitCode() > 0 || result.getStandardOut().isEmpty())
                throw new ApplicationInitializationException(
                        "Command not installed: [" + command + "].");
        } catch (ProcessExecutionException e) {
            throw new ApplicationInitializationException(
                    "Could not check existence of command [" + command + "]: " + e.getMessage(), e);
        }
    }

    private static void createLogDir() throws ApplicationInitializationException {
        M7rHostLogDir m7rHostLogDir = new M7rHostLogDir();
        if (!m7rHostLogDir.exists()) {
            try {
                m7rHostLogDir.create();
            } catch (IOException e) {
                throw new ApplicationInitializationException("Application initialization failed. Could not create directory ["
                        + m7rHostLogDir.toAbsolutePathString() + "]");
            }
        }
    }

    private static void initLogging() {
        M7rHostLogDir m7rHostLogDir = new M7rHostLogDir();
        Path logFile = m7rHostLogDir.asPath().resolve("m7r-infra.log");
        LoggerInit.consoleAndFile(logFile, Level.INFO, Level.OFF);
    }

}
