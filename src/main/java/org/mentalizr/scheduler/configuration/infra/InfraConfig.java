package org.mentalizr.scheduler.configuration.infra;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class InfraConfig {

    public static final String INFRA_AUTOSTART = "infra-autostart";
    public static final String LOG_LEVEL_INFRA = "log-level-infra";
    public static final Level LOG_LEVEL_INFRA_DEFAULT = Level.INFO;
    public static final String LOG_LEVEL_SCHEDULER = "log-level-scheduler";
    public static final Level LOG_LEVEL_SCHEDULER_DEFAULT = Level.INFO;
    public static final String LOG_LEVEL_DOCKER = "log-level-docker";
    public static final Level LOG_LEVEL_DOCKER_DEFAULT = Level.INFO;

    private final boolean infraAutostart;
    private final Level logLevelInfra;
    private final Level logLevelScheduler;
    private final Level logLevelDocker;

    public InfraConfig(boolean infraAutostart, Level logLevelInfra, Level logLevelScheduler, Level logLevelDocker) {
        this.infraAutostart = infraAutostart;
        this.logLevelInfra = logLevelInfra;
        this.logLevelScheduler = logLevelScheduler;
        this.logLevelDocker = logLevelDocker;
    }

    public boolean isInfraAutostart() {
        return infraAutostart;
    }

    public Level getLogLevelInfra() {
        return logLevelInfra;
    }

    public Level getLogLevelScheduler() {
        return logLevelScheduler;
    }

    public Level getLogLevelDocker() {
        return logLevelDocker;
    }

}
