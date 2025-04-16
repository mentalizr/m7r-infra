package org.mentalizr.scheduler.jobs.expired;

import org.mentalizr.scheduler.jobs.BaseConfiguration;
import org.mentalizr.scheduler.jobs.JobConfiguration;

public final class ExpiredConfiguration extends JobConfiguration {

    public static final String SECTION_NAME = "expired";
    public static final String EXPIRATION_MONTH_UNUSED = "expiration-months-unused";
    public static final String EXPIRATION_DAYS_LAST_USED = "expiration-days-last-used";
    public static final String DELETE_UNUSED = "delete-expired-unused";
    public static final String DELETE_USED = "delete-expired-used";

    private final Integer expirationMonthUnused;
    private final Integer expirationDaysLastUsed;
    private final boolean deleteExpiredUnused;
    private final boolean deleteExpiredUsed;

    public ExpiredConfiguration(
            BaseConfiguration baseConfiguration,
            Integer expirationMonthUnused,
            boolean deleteExpiredUnused,
            Integer expirationDaysLastUsed,
            boolean deleteExpiredUsed
    ) {
        super(baseConfiguration);
        this.expirationMonthUnused = expirationMonthUnused;
        this.deleteExpiredUnused = deleteExpiredUnused;
        this.expirationDaysLastUsed = expirationDaysLastUsed;
        this.deleteExpiredUsed = deleteExpiredUsed;
    }

    public Integer getExpirationMonthUnused() {
        return expirationMonthUnused;
    }

    public boolean isDeleteExpiredUnused() {
        return deleteExpiredUnused;
    }

    public Integer getExpirationDaysLastUsed() {
        return expirationDaysLastUsed;
    }

    public boolean isDeleteExpiredUsed() {
        return deleteExpiredUsed;
    }

}
