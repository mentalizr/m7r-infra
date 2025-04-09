package org.mentalizr.scheduler.jobs.expired;

import com.google.gson.Gson;
import de.arthurpicht.utils.core.exception.ExceptionUtils;
import de.arthurpicht.utils.core.system.SystemUtils;
import org.mentalizer.mailer.notifier.MailNotification;
import org.mentalizer.mailer.notifier.MailNotifier;
import org.mentalizr.client.api.deleteExpired.AccessKeyDeleteExpiredRequest;
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

//        AccessKeyDeleteExpiredRequest request = new AccessKeyDeleteExpiredRequest(
//                expiredConfiguration.getExpirationMonthUnused(),
//                expiredConfiguration.getExpirationDaysLastUsed(),
//
//        )
//
//        Intention intention = IntentionFile.getIntention();
//        if (intention != Intention.UP) {
//            logger.debug("Intention is [" + intention + "]. Watchdog continues to sleep.");
//            return;
//        }
//
//        StatusSummary statusSummary = StatusSummary.create();
//
//        if (statusSummary.isRunning()) {
//            logger.debug("Intention is UP and m7r infrastructure is running. Watchdog continues to sleep.");
//        } else {
//            logger.warn("Intention is UP and m7r infrastructure is not running. Try to restart ...");
//            try {
//                Restart.perform();
//                logger.warn("m7r infrastructure restarted.");
//                sendNotificationRestart();
//            } catch (Restart.RestartException e) {
//                logger.error("Restart failed.", e);
//                sendNotificationRestartFailed(e);
//            }
//        }
    }

    @Override
    public ExpiredConfiguration getJobConfiguration(String jobConfigurationJson) {
        return new Gson().fromJson(jobConfigurationJson, ExpiredConfiguration.class);
    }

    private void sendNotificationRestart() {
        MailNotification mailNotification = new MailNotification(
                "[" + SystemUtils.getHostname() + "] restarted by watchdog job",
                "System [" + SystemUtils.getHostname() + "] was found down UP by watchdog job while intention ip UP.\n"
                        + "m7r infrastructure was restarted successfully.");
        MailNotifier.sendNotification(mailNotification, new SchedulerMailNotifierCallback());
    }

    private void sendNotificationRestartFailed(Exception e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        MailNotification mailNotification = new MailNotification(
                "[" + SystemUtils.getHostname() + "] FAILED restart by watchdog job",
                "System [" + SystemUtils.getHostname() + "] was found down with intention UP by watchdog job.\n"
                        + "watchdog job failed to restart m7r infrastructure.\n\n"
                        + stacktrace);
        MailNotifier.sendNotification(mailNotification, new SchedulerMailNotifierCallback());
    }

}
