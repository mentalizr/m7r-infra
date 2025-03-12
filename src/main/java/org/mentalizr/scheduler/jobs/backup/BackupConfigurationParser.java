package org.mentalizr.scheduler.jobs.backup;

import de.arthurpicht.configuration.Configuration;
import org.mentalizr.scheduler.jobs.BaseConfiguration;
import org.mentalizr.scheduler.jobs.JobConfigurationParser;

import java.nio.file.Path;

public class BackupConfigurationParser extends JobConfigurationParser {

    public BackupConfigurationParser(BaseConfiguration baseConfiguration, Configuration configuration, Path configurationPath) {
        super(baseConfiguration, configuration, configurationPath);
    }

    @Override
    public BackupConfiguration parse() {
        return new BackupConfiguration(
                baseConfiguration
        );
    }

}
