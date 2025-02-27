package org.mentalizr.infra.appInit;

import ch.qos.logback.classic.Level;
import de.arthurpicht.utils.logging.LogFile;
import de.arthurpicht.utils.logging.LogbackInit;
import org.mentalizr.commons.paths.host.hostDir.M7rHostLogDir;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.appInit.InfraApplicationInitialization.Application;
import org.mentalizr.scheduler.configuration.infra.InfraConfig;

public class Logging {

    public static void configure(InfraConfig infraConfig, Application application) {
        if (application == Application.SCHEDULER) {
            Logging.configureForScheduler(infraConfig);
        } else if (application == Application.CLI) {
            Logging.configureForCLI(infraConfig);
        } else {
            throw new IllegalArgumentException("Unsupported application: " + application);
        }
    }

    private static void configureForCLI(InfraConfig infraConfig) {
        M7rHostLogDir m7rHostLogDir = new M7rHostLogDir();
        new LogbackInit()
                .addLogFile(new LogFile.Builder()
                        .withPath(m7rHostLogDir.asPath().resolve("m7r-infra.log"))
                        .withLevel(infraConfig.getLogLevelInfra())
                        .build())
                .addLoggerLevel("org.mongodb", Level.INFO)
                .addLoggerLevel("org.quartz", Level.INFO)
                .addLoggerLevel(Const.DOCKER_LOGGER, infraConfig.getLogLevelDocker())
                .initialize();
    }

    private static void configureForScheduler(InfraConfig infraConfig) {
        M7rHostLogDir m7rHostLogDir = new M7rHostLogDir();
        new LogbackInit()
                .addLogFile(new LogFile.Builder()
                        .withPath(m7rHostLogDir.asPath().resolve("m7r-scheduler.log"))
                        .withLevel(infraConfig.getLogLevelScheduler())
                        .build())
                .addLoggerLevel("org.mongodb", Level.INFO)
                .addLoggerLevel("org.quartz", Level.INFO)
                .addLoggerLevel(Const.DOCKER_LOGGER, infraConfig.getLogLevelDocker())
                .initialize();
    }

}
