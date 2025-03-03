package org.mentalizr.scheduler.configuration.infra;

import ch.qos.logback.classic.Level;
import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.utils.core.collection.Sets;
import org.mentalizr.commons.paths.host.hostDir.M7rInfraConfigFile;
import org.mentalizr.scheduler.helper.ConfigurationHelper;

import static org.mentalizr.scheduler.configuration.infra.InfraConfig.*;

public class InfraConfigLoader {

    private static final M7rInfraConfigFile M7R_INFRA_CONFIG_FILE = new M7rInfraConfigFile();

    public static InfraConfig load() {

        ConfigurationFactory configurationFactory = ConfigurationHelper.bindConfigFile(M7R_INFRA_CONFIG_FILE);
        Configuration configuration = configurationFactory.getConfiguration();

        ConfigurationHelper.checkForParameterSyntaxErrors(
                M7R_INFRA_CONFIG_FILE.asPath(),
                configuration,
                Sets.newHashSet(INFRA_AUTOSTART, LOG_LEVEL_SCHEDULER, LOG_LEVEL_INFRA, LOG_LEVEL_DOCKER));

        boolean autostart
                = ConfigurationHelper.getMandatoryBoolean(configuration, INFRA_AUTOSTART, M7R_INFRA_CONFIG_FILE);
        Level logLevelInfra
                = ConfigurationHelper.getLevel(
                        configuration, LOG_LEVEL_INFRA, LOG_LEVEL_INFRA_DEFAULT, M7R_INFRA_CONFIG_FILE);
        Level logLevelScheduler
                = ConfigurationHelper.getLevel(
                        configuration, LOG_LEVEL_SCHEDULER, LOG_LEVEL_SCHEDULER_DEFAULT, M7R_INFRA_CONFIG_FILE);
        Level logLevelDocker
                = ConfigurationHelper.getLevel(
                        configuration, LOG_LEVEL_DOCKER, LOG_LEVEL_DOCKER_DEFAULT, M7R_INFRA_CONFIG_FILE);

        return new InfraConfig(
                autostart,
                logLevelInfra,
                logLevelScheduler,
                logLevelDocker
        );
    }

}
