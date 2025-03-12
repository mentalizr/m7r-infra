package org.mentalizr.scheduler.jobFactories;

import org.mentalizr.scheduler.jobs.JobConfiguration;
import org.mentalizr.scheduler.jobs.activityStatWeekly.ActivityStatWeeklyConfiguration;
import org.mentalizr.scheduler.jobs.activityStatWeekly.ActivityStatWeeklyJob;
import org.mentalizr.scheduler.jobs.backup.BackupConfiguration;
import org.mentalizr.scheduler.jobs.backup.BackupJob;
import org.mentalizr.scheduler.jobs.heartbeat.HeartbeatConfiguration;
import org.mentalizr.scheduler.jobs.heartbeat.HeartbeatJob;
import org.mentalizr.scheduler.jobs.watchdog.WatchdogConfiguration;
import org.mentalizr.scheduler.jobs.watchdog.WatchdogJob;
import org.quartz.Job;

public class SchedulerJobFactory {

    public static Class<? extends Job> getJobClass(JobConfiguration jobConfiguration) {
        if (jobConfiguration instanceof HeartbeatConfiguration) {
            return HeartbeatJob.class;
        } else if (jobConfiguration instanceof ActivityStatWeeklyConfiguration) {
            return ActivityStatWeeklyJob.class;
        } else if (jobConfiguration instanceof WatchdogConfiguration) {
            return WatchdogJob.class;
        } else if (jobConfiguration instanceof BackupConfiguration) {
            return BackupJob.class;
        }
        throw new IllegalStateException("Unsupported job configuration type: " + jobConfiguration.getClass().getName());
    }

}
