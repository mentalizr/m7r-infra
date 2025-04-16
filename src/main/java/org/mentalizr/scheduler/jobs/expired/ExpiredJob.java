package org.mentalizr.scheduler.jobs.expired;

import com.google.gson.Gson;
import de.arthurpicht.console.Console;
import de.arthurpicht.utils.core.exception.ExceptionUtils;
import de.arthurpicht.utils.core.system.SystemUtils;
import org.mentalizer.mailer.notifier.MailNotification;
import org.mentalizer.mailer.notifier.MailNotifier;
import org.mentalizr.client.api.ClientApiRuntimeException;
import org.mentalizr.client.api.SessionAgent;
import org.mentalizr.client.api.deleteExpired.AccessKeyDeleteExpired;
import org.mentalizr.client.api.deleteExpired.AccessKeyDeleteExpiredRequest;
import org.mentalizr.client.api.deleteExpired.AccessKeyDeleteExpiredResult;
import org.mentalizr.infra.executors.Restart;
import org.mentalizr.infra.externalApi.StatusSummary;
import org.mentalizr.scheduler.jobs.SchedulerJob;
import org.mentalizr.scheduler.jobs.watchdog.WatchdogConfiguration;
import org.mentalizr.scheduler.mailNotifier.SchedulerMailNotifierCallback;
import org.mentalizr.scheduler.processManagement.IntentionFile;
import org.mentalizr.scheduler.processManagement.IntentionFile.Intention;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class ExpiredJob extends SchedulerJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ExpiredJob.class);

    @Override
    public void schedulerExecute(JobExecutionContext jobExecutionContext, String jobConfigurationJson)
            throws JobExecutionException {

        ExpiredConfiguration expiredConfiguration = getJobConfiguration(jobConfigurationJson);

        AccessKeyDeleteExpiredRequest request = new AccessKeyDeleteExpiredRequest(
                expiredConfiguration.getExpirationMonthUnused(),
                expiredConfiguration.getExpirationDaysLastUsed(),
                expiredConfiguration.isDeleteExpiredUsed(),
                expiredConfiguration.isDeleteExpiredUnused(),
                false,
                false
        );

        logger.debug(request.toString());

        AccessKeyDeleteExpiredResult result;
        try {
            SessionAgent sessionAgent = SessionAgent.createFromLocalConfigWithTransientCookieStorage();
            result = AccessKeyDeleteExpired.execute(request, sessionAgent.getHttpCallContext());
            sessionAgent.logout();
        } catch (ClientApiRuntimeException e) {
            throw new JobExecutionException(e);
        }

        if (result.foundExpired()) {
            logger.info("Expired users were found. Send notification.");
            sendNotification(result);
        } else {
            logger.info("No expired users found.");
        }

    }

    @Override
    public ExpiredConfiguration getJobConfiguration(String jobConfigurationJson) {
        return new Gson().fromJson(jobConfigurationJson, ExpiredConfiguration.class);
    }

    private void sendNotification(AccessKeyDeleteExpiredResult result) {
        MailNotification mailNotification = new MailNotification(
                "[" + SystemUtils.getHostname() + "] has expired users",
                "expired users on [" + SystemUtils.getHostname() + "]:\n\n"
                        + result.getSummary());
        MailNotifier.sendNotification(mailNotification, new SchedulerMailNotifierCallback());
    }

}
