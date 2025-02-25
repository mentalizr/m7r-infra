package org.mentalizr.scheduler.configuration;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import org.mentalizr.commons.paths.host.hostDir.SchedulerConfigFile;
import org.slf4j.event.Level;

import static org.mentalizr.scheduler.configuration.ConfigurationHelper.*;
import static org.mentalizr.scheduler.configuration.SchedulerConfig.*;

public class SchedulerConfigLoader {

    private static final SchedulerConfigFile SCHEDULER_CONFIG_FILE = new SchedulerConfigFile();

    public static SchedulerConfig load() {

        ConfigurationFactory configurationFactory = bindConfigFile(SCHEDULER_CONFIG_FILE);
        Configuration configuration = configurationFactory.getConfiguration();

        boolean autostart = getMandatoryBoolean(configuration, INFRA_AUTOSTART, SCHEDULER_CONFIG_FILE);
        Level level = getLevel(configuration, LOG_LEVEL, Level.INFO, SCHEDULER_CONFIG_FILE);

        return new SchedulerConfig(
                autostart,
                level
        );
    }

}
