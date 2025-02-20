package org.mentalizr.scheduler.jobs;

import org.mentalizer.mailer.notifier.MailNotifier;
import org.mentalizr.scheduler.mailNotifier.SchedulerMailNotifierCallback;
import org.mentalizr.scheduler.helper.ExceptionUtils;
import org.mentalizr.scheduler.helper.LocalHost;
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
        if (JobHelper.isInactive()) {
            logger.info("Scheduler is configured as deactivated. Skipping execution.");
            return;
        }
        String jobConfigurationJson = getJobConfigurationAsJson(jobExecutionContext);
        JobConfiguration jobConfiguration = getJobConfiguration(jobConfigurationJson);
        if (!jobConfiguration.baseConfiguration.isEnabled()) {
            logger.info("Job [" + jobConfiguration.getJobName() + "] is configured as disabled. Skipping execution.");
            return;
        }
        try {
            schedulerExecute(jobExecutionContext, jobConfigurationJson);
            if (jobConfiguration.getBaseConfiguration().isNotifyOnSuccess())
                sendNotificationOnSuccess(jobConfiguration);
        } catch (JobExecutionException | RuntimeException e) {
            logger.error("Error executing job [" + jobConfiguration.getJobName() + "]", e);
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
        String hostname = LocalHost.getHostname();
        SchedulerMailNotifier.send(
                "[" + hostname + "] Scheduler job executed: [" + jobConfiguration.getJobName() + "].",
                "Successfully executed job [" + jobConfiguration.getJobName() + "] on [" + hostname + "].\n\n"
                + "This is a automatically generated notification. Please do not reply."
        );
    }

    private void sendNotificationOnFailure(JobConfiguration jobConfiguration, Exception e) {
        String hostname = LocalHost.getHostname();
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
