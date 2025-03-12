package org.mentalizr.scheduler.jobs.backup;

import org.mentalizr.scheduler.jobs.BaseConfiguration;
import org.mentalizr.scheduler.jobs.JobConfiguration;

public final class BackupConfiguration extends JobConfiguration {

    public static final String SECTION_NAME = "backup";

    public BackupConfiguration(
            BaseConfiguration baseConfiguration
    ) {
        super(baseConfiguration);
    }

}
