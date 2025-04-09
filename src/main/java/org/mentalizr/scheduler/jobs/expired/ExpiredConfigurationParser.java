package org.mentalizr.scheduler.jobs.expired;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.utils.core.collection.Sets;
import org.mentalizr.scheduler.jobs.BaseConfiguration;
import org.mentalizr.scheduler.jobs.JobConfigurationParser;

import java.nio.file.Path;

import static org.mentalizr.scheduler.jobs.expired.ExpiredConfiguration.EXPIRATION_DAYS_LAST_USED;
import static org.mentalizr.scheduler.jobs.expired.ExpiredConfiguration.EXPIRATION_MONTH_UNUSED;
import static org.mentalizr.scheduler.jobs.heartbeat.HeartbeatConfiguration.LOG_MESSAGE;

public class ExpiredConfigurationParser extends JobConfigurationParser {

    public ExpiredConfigurationParser(BaseConfiguration baseConfiguration, Configuration configuration, Path configurationPath) {
        super(baseConfiguration, configuration, configurationPath);
    }

    @Override
    public ExpiredConfiguration parse() {
        checkForParameterSyntaxErrors(Sets.newHashSet(EXPIRATION_DAYS_LAST_USED, EXPIRATION_MONTH_UNUSED));

        Integer expirationMonthsUnused = null;
        if (configuration.containsKey(EXPIRATION_MONTH_UNUSED)) expirationMonthsUnused = Integer.valueOf(configuration.getString(EXPIRATION_MONTH_UNUSED));

        Integer expirationDaysLastUsed = null;
        if (configuration.containsKey(EXPIRATION_DAYS_LAST_USED)) expirationDaysLastUsed = Integer.valueOf(configuration.getString(EXPIRATION_DAYS_LAST_USED));

        return new ExpiredConfiguration(
                baseConfiguration,
                expirationMonthsUnused,
                expirationDaysLastUsed
        );
    }

}
