package org.mentalizr.scheduler.configuration;

import org.slf4j.event.Level;

public class SchedulerConfig {

    public static final String INFRA_AUTOSTART = "infra-autostart";
    public static final String LOG_LEVEL = "log-level";

    private final boolean infraAutostart;
    private final Level logLevel;

    public SchedulerConfig(boolean infraAutostart, Level logLevel) {
        this.infraAutostart = infraAutostart;
        this.logLevel = logLevel;
    }

    public boolean isInfraAutostart() {
        return infraAutostart;
    }

    public Level getLogLevel() {
        return logLevel;
    }

}
