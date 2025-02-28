package org.mentalizr.infra.appInit;

import ch.qos.logback.classic.Level;
import de.arthurpicht.processExecutor.ProcessExecution;
import de.arthurpicht.processExecutor.ProcessExecutionException;
import de.arthurpicht.processExecutor.ProcessResultCollection;
import de.arthurpicht.utils.logging.LogFile;
import de.arthurpicht.utils.logging.LogbackInit;
import org.mentalizr.commons.paths.M7rDir;
import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.commons.paths.client.M7rClientCliConfigFile;
import org.mentalizr.commons.paths.client.M7rClientCredentialsFile;
import org.mentalizr.commons.paths.host.hostDir.*;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.GlobalOptions;
import org.mentalizr.scheduler.configuration.infra.InfraConfig;
import org.mentalizr.scheduler.configuration.infra.InfraConfigLoader;

import java.io.IOException;

public class InfraApplicationInitialization {

    public enum Application { CLI, SCHEDULER }

    public static void asCli(GlobalOptions globalOptions) throws InfraApplicationInitializationException {
        execute(globalOptions, Application.CLI);
    }

    public static void asScheduler() throws InfraApplicationInitializationException {
        GlobalOptions globalOptions = new GlobalOptions(
                false,
                false,
                false,
                null,
                false,
                true);
        execute(globalOptions, Application.SCHEDULER);
    }

    private static void execute(GlobalOptions globalOptions, Application application) throws InfraApplicationInitializationException {
        assertExistsM7rFile(new M7rInfraUserConfigFile());
        assertExistsM7rFile(new M7rSslCertFile());
        assertExistsM7rFile(new M7rPrivateKeyFile());

        assertExistsM7rFile(M7rClientCliConfigFile.createInstance());
        assertExistsM7rFile(M7rClientCredentialsFile.createInstance());

        assertCommand("docker");
        assertCommand("git");

        createLogDir();
        createDaemonConfigDir();
        InfraConfig infraConfig = InfraConfigLoader.load();

        Logging.configure(infraConfig, application);

        ApplicationContext.initialize(globalOptions);
    }

    private static void assertExistsM7rDir(M7rDir m7rDir) throws InfraApplicationInitializationException {
        if (!m7rDir.exists())
            throw new InfraApplicationInitializationException(
                    m7rDir.getDescription()  + " not found: [" + m7rDir.toAbsolutePathString() + "].");
    }

    private static void assertExistsM7rFile(M7rFile m7rFile) throws InfraApplicationInitializationException {
        if (!m7rFile.exists())
            throw new InfraApplicationInitializationException(
                    m7rFile.getDescription()  + " not found: [" + m7rFile.toAbsolutePathString() + "].");
    }

    private static void assertCommand(String command) throws InfraApplicationInitializationException {
        try {
            ProcessResultCollection result = ProcessExecution.execute("which", command);
            if (result.getExitCode() > 0 || result.getStandardOut().isEmpty())
                throw new InfraApplicationInitializationException(
                        "Command not installed: [" + command + "].");
        } catch (ProcessExecutionException e) {
            throw new InfraApplicationInitializationException(
                    "Could not check existence of command [" + command + "]: " + e.getMessage(), e);
        }
    }

    private static void createLogDir() throws InfraApplicationInitializationException {
        M7rHostLogDir m7rHostLogDir = new M7rHostLogDir();
        if (!m7rHostLogDir.exists()) {
            try {
                m7rHostLogDir.create();
            } catch (IOException e) {
                throw new InfraApplicationInitializationException("Application initialization failed. Could not create directory ["
                        + m7rHostLogDir.toAbsolutePathString() + "]");
            }
        }
    }

    private static void createDaemonConfigDir() throws InfraApplicationInitializationException {
        M7rSchedulerConfigDir m7rSchedulerConfigDir = new M7rSchedulerConfigDir();
        if (!m7rSchedulerConfigDir.exists()) {
            try {
                m7rSchedulerConfigDir.create();
            } catch (IOException e) {
                throw new InfraApplicationInitializationException(
                        "Application initialization failed. Could not create directory ["
                                + m7rSchedulerConfigDir.toAbsolutePathString() + "]");
            }
        }
    }

    private static void configureLogging(InfraConfig infraConfig) {
        M7rHostLogDir m7rHostLogDir = new M7rHostLogDir();
        new LogbackInit()
                .addLogFile(new LogFile.Builder()
                        .withPath(m7rHostLogDir.asPath().resolve("m7r-infra.log"))
                        .withLevel(infraConfig.getLogLevelInfra())
                        .build())
                .addLogFile(new LogFile.Builder()
                        .withPath(new M7rHostLogDir().asPath().resolve("m7r-scheduler.log"))
                        .withLogger("org.mentalizr.scheduler")
                        .withLevel(infraConfig.getLogLevelScheduler())
                        .build())
                .addLoggerLevel("org.mongo", Level.INFO)
                .addLoggerLevel("org.quartz", Level.INFO)
                .addLoggerLevel(Const.DOCKER_LOGGER, infraConfig.getLogLevelDocker())
                .initialize();
    }

}
