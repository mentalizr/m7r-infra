package org.mentalizr.scheduler.jobs;

import de.arthurpicht.utils.core.exception.ExceptionUtils;
import de.arthurpicht.utils.core.system.SystemUtils;
import org.mentalizr.scheduler.mailNotifier.SchedulerMailNotifier;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.mentalizr.scheduler.Const.CONFIGURATION_KEY;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public abstract class SchedulerJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerJob.class);

    public abstract void schedulerExecute(JobExecutionContext context, String jobConfiguration) throws JobExecutionException;

    public abstract JobConfiguration getJobConfiguration(String jobConfigurationJson);

    @Override
    public final void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String jobConfigurationJson = getJobConfigurationAsJson(jobExecutionContext);
        JobConfiguration jobConfiguration = getJobConfiguration(jobConfigurationJson);
        if (JobHelper.isInactive()) {
            logger.debug("Scheduler is deactivated. Skipping execution of job [" + jobConfiguration.getJobName() + "].");
            return;
        }
        if (!jobConfiguration.baseConfiguration.isEnabled()) {
            logger.debug("Job [" + jobConfiguration.getJobName() + "] is configured as disabled. Skipping execution.");
            return;
        }
        try {
            logger.info("Start execution of job [" + jobConfiguration.getJobName() + "].");
            schedulerExecute(jobExecutionContext, jobConfigurationJson);
            logger.info("Finished executing job [" + jobConfiguration.getJobName() + "].");
            if (jobConfiguration.getBaseConfiguration().isNotifyOnSuccess())
                sendNotificationOnSuccess(jobConfiguration);
        } catch (JobExecutionException | RuntimeException e) {
            logger.error("Error executing job [" + jobConfiguration.getJobName() + "].", e);
            if (jobConfiguration.getBaseConfiguration().isNotifyOnFailure())
                sendNotificationOnFailure(jobConfiguration, e);
            throw e;
        }
    }

    private String getJobConfigurationAsJson(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String[] contextKeys = jobDataMap.getKeys();
        if (!Arrays.asList(contextKeys).contains(CONFIGURATION_KEY))
            throw new IllegalStateException("Job context does not contain configuration.");
        return jobDataMap.getString(CONFIGURATION_KEY);
    }

    private void sendNotificationOnSuccess(JobConfiguration jobConfiguration) {
        String hostname = SystemUtils.getHostname();
        SchedulerMailNotifier.send(
                "[" + hostname + "] Scheduler job executed: [" + jobConfiguration.getJobName() + "].",
                "Successfully executed job [" + jobConfiguration.getJobName() + "] on [" + hostname + "].\n\n"
                + "This is a automatically generated notification. Please do not reply."
        );
    }

    private void sendNotificationOnFailure(JobConfiguration jobConfiguration, Exception e) {
        String hostname = SystemUtils.getHostname();
        String stacktrace = ExceptionUtils.getStackTrace(e);
        SchedulerMailNotifier.send(
                "[" + hostname + "] Scheduler job execution FAILED for [" + jobConfiguration.getJobName() + "].",
                "Execution of job [" + jobConfiguration.getJobName() + "] on [" + hostname + "] failed.\n\n"
                        + "Exception message: " + e.getMessage() + "\n\n"
                        + stacktrace + "\n\n"
                        + "This is a automatically generated notification. Please do not reply."
        );
    }

}
