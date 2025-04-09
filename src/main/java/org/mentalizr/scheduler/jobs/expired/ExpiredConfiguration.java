package org.mentalizr.scheduler.jobs.expired;

import org.mentalizr.scheduler.jobs.BaseConfiguration;
import org.mentalizr.scheduler.jobs.JobConfiguration;

public final class ExpiredConfiguration extends JobConfiguration {

    public static final String SECTION_NAME = "expired";
    public static final String EXPIRATION_MONTH_UNUSED = "expiration-months-unused";
    public static final String EXPIRATION_DAYS_LAST_USED = "expiration-days-last-used";

    private final Integer expirationMonthUnused;
    private final Integer expirationDaysLastUsed;

    public ExpiredConfiguration(
            BaseConfiguration baseConfiguration,
            Integer expirationMonthUnused,
            Integer expirationDaysLastUsed
    ) {
        super(baseConfiguration);
        this.expirationMonthUnused = expirationMonthUnused;
        this.expirationDaysLastUsed = expirationDaysLastUsed;
    }

    public Integer getExpirationMonthUnused() {
        return expirationMonthUnused;
    }

    public Integer getExpirationDaysLastUsed() {
        return expirationDaysLastUsed;
    }

}
