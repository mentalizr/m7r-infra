package org.mentalizr.scheduler.helper;

import ch.qos.logback.classic.Level;
import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.scheduler.SchedulerRuntimeException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class ConfigurationHelper {

    public static void checkForParameterSyntaxErrors(
            Path configurationFile,
            Configuration configuration,
            Set<String> occurringParameters) {

        Set<String> keys = configuration.getKeys();
        for (String key : keys) {
            if (!occurringParameters.contains(key)) {
                String message = "Illegal parameter [" + key + "] in configuration file ["
                                 + configurationFile.toAbsolutePath() + "]";
                if (Strings.isSpecified(configuration.getSectionName())) {
                    message += " section name [" + configuration.getSectionName() + "].";
                } else {
                    message += ".";
                }
                throw new SchedulerRuntimeException(message);
            }
        }
    }

    public static void checkForMandatoryParameters(
            Path configurationFile,
            Configuration configuration,
            Set<String> mandatoryParameters) {

        Set<String> keys = configuration.getKeys();
        for (String parameter : mandatoryParameters) {
            if (!keys.contains(parameter)) {
                String message = "Mandatory parameter [" + parameter + "] " +
                                 "not found in configuration file [" + configurationFile.toAbsolutePath() + "]";
                if (Strings.isSpecified(configuration.getSectionName())) {
                    message += " section name [" + configuration.getSectionName() + "].";
                } else {
                    message += ".";
                }
                throw new SchedulerRuntimeException(message);
            }
        }
    }

    public static ConfigurationFactory bindConfigFile(M7rFile m7rConfigFile) {
        return bindConfigFile(m7rConfigFile.asPath());
    }

    public static ConfigurationFactory bindConfigFile(Path configurationFile) {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        try {
            configurationFactory.addConfigurationFileFromFilesystem(configurationFile.toFile());
        } catch (ConfigurationFileNotFoundException | IOException e) {
            throw new SchedulerRuntimeException("Error reading configuration file ["
                    + configurationFile.toAbsolutePath() + "]: " + e.getMessage(), e);
        }
        return configurationFactory;
    }

    public static boolean getMandatoryBoolean(Configuration configuration, String name, M7rFile m7rFile) {
        if (!configuration.containsKey(name))
            throw new SchedulerRuntimeException(
                    "Mandatory configuration parameter [" + name + "] not found in ["
                            + m7rFile.asPath().toAbsolutePath() + "].");
        return configuration.getBoolean(name);
    }

    public static int getMandatoryInt(Configuration configuration, String name, M7rFile m7rFile) {
        if (!configuration.containsKey(name))
            throw new SchedulerRuntimeException(
                    "Mandatory configuration parameter [" + name + "] not found in ["
                            + m7rFile.asPath().toAbsolutePath() + "].");
        return configuration.getInt(name);
    }

    public static Level getLevel(Configuration configuration, String parameterName, Level defaultLevel, M7rFile m7rFile) {
        if (!configuration.containsKey(parameterName))
            return defaultLevel;
        String value = configuration.getString(parameterName).toUpperCase();
        try {
            return Level.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new SchedulerRuntimeException(
                    "Configuration parameter [" + parameterName + "] with illegal value: [" + value + "] in ["
                            + m7rFile.asPath().toAbsolutePath() + "]."
            );
        }
    }
}
