package org.mentalizr.scheduler.appInit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import de.arthurpicht.utils.logging.LoggerInit;
import org.mentalizr.commons.paths.host.hostDir.M7rHostLogDir;
import org.mentalizr.commons.paths.host.hostDir.M7rSchedulerConfigDir;
import org.mentalizr.scheduler.configuration.SchedulerConfig;
import org.mentalizr.scheduler.configuration.SchedulerConfigLoader;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class ApplicationInitialization {

    public static void execute() {
        createLogDir();
        initLogging();
        createDaemonConfigDir();
    }

    private static SchedulerConfig loadSchedulerConfig() {
        return SchedulerConfigLoader.load();
    }

    private static void createLogDir() throws ApplicationInitializationException {
        M7rHostLogDir m7rHostLogDir = new M7rHostLogDir();
        if (!m7rHostLogDir.exists()) {
            try {
                m7rHostLogDir.create();
            } catch (IOException e) {
                throw new ApplicationInitializationException(
                        "Application initialization failed. Could not create directory ["
                                + m7rHostLogDir.toAbsolutePathString() + "]");
            }
        }
    }

    private static void initLogging() {
        M7rHostLogDir m7rHostLogDir = new M7rHostLogDir();
        Path logFile = m7rHostLogDir.asPath().resolve("m7r-scheduler.log");
        LoggerInit.consoleAndFile(logFile, Level.DEBUG, Level.OFF);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger mongoLogger = loggerContext.getLogger("org.mongo");
        mongoLogger.setLevel(Level.INFO);
        Logger quartzLogger = loggerContext.getLogger("org.quartz");
        quartzLogger.setLevel(Level.INFO);

    }

    private static void createDaemonConfigDir() {
        M7rSchedulerConfigDir m7rSchedulerConfigDir = new M7rSchedulerConfigDir();
        if (!m7rSchedulerConfigDir.exists()) {
            try {
                m7rSchedulerConfigDir.create();
            } catch (IOException e) {
                throw new ApplicationInitializationException(
                        "Application initialization failed. Could not create directory ["
                                + m7rSchedulerConfigDir.toAbsolutePathString() + "]");
            }
        }
    }

}
