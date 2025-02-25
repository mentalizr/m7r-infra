package org.mentalizr.scheduler;

import de.arthurpicht.utils.core.exception.ExceptionUtils;
import de.arthurpicht.utils.core.system.SystemUtils;
import org.mentalizer.mailer.notifier.MailNotification;
import org.mentalizer.mailer.notifier.MailNotifier;
import org.mentalizr.infra.appInit.ApplicationContext;
import org.mentalizr.infra.executors.Restart;
import org.mentalizr.infra.externalApi.StatusSummary;
import org.mentalizr.scheduler.appInit.ApplicationInitialization;
import org.mentalizr.scheduler.appInit.ApplicationInitializationException;
import org.mentalizr.scheduler.configuration.JobConfigurations;
import org.mentalizr.scheduler.configuration.JobConfigurationsManager;
import org.mentalizr.scheduler.configuration.SchedulerConfig;
import org.mentalizr.scheduler.configuration.SchedulerConfigLoader;
import org.mentalizr.scheduler.helper.LinuxHelper;
import org.mentalizr.scheduler.jobInitialization.JobInitializer;
import org.mentalizr.scheduler.mailNotifier.SchedulerMailNotifierCallback;
import org.mentalizr.scheduler.processManagement.DaemonPidFile;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class M7rScheduler {

    private static final Logger logger = LoggerFactory.getLogger(M7rScheduler.class);
    private static final DaemonPidFile daemonPidFile = new DaemonPidFile();

    public static void main(String[] args) {

        try {
            ApplicationInitialization.execute();
        } catch (ApplicationInitializationException e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }

        logger.info("Starting Scheduler...");
        logger.debug("PID file is: " + daemonPidFile.asPath().toAbsolutePath());

        try {
            if (alreadyRunning()) {
                logger.error("Scheduler is already running. Exiting.");
                System.exit(2);
            }
            daemonPidFile.create();
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }

        try {
            SchedulerConfig schedulerConfig = SchedulerConfigLoader.load();
            if (schedulerConfig.isInfraAutostart()) {
                logger.info("auto-starting infrastructure ...");
                ApplicationContext.initializeWithDefaults();
                StatusSummary statusSummary = StatusSummary.create();
                if (statusSummary.isRunning()) {
                    logger.info("Infrastructure is already running.");
                } else {
                    Restart.perform();
                    logger.warn("m7r infrastructure restarted.");
                    sendNotificationAutostart();
                }
            }
        } catch (RuntimeException | Restart.RestartException e) {
            logger.error("Auto-starting infrastructure failed: " + e.getMessage(), e);
            logger.error("Abort scheduler start-up sequence. See m7r-infra logs for more infos.");
            sendNotificationAutostartFailed(e);
            System.exit(1);
        }

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            addShutdownHook(scheduler);

            logger.info("load scheduler configurations ...");
            JobConfigurations jobConfigurations = JobConfigurationsManager.fromConfigFiles();
            logger.info("initialize scheduler ...");
            JobInitializer.initialize(scheduler, jobConfigurations);
            JobConfigurationsManager.saveHash();

            scheduler.start();

        } catch (SchedulerException | RuntimeException e) {
            logger.error("Starting daemon failed: " + e.getMessage(), e);
            System.exit(1);
        }

    }

    private static boolean alreadyRunning() {
        if (daemonPidFile.exists()) {
            long pid = daemonPidFile.getPid();
            return LinuxHelper.hasProcess(pid);
        }
        return false;
    }

    private static void addShutdownHook(Scheduler scheduler) {
        Thread shutdownHook = new Thread(() -> {
            try {
                 daemonPidFile.removeIfExists();
            } catch (RuntimeException e) {
                logger.error("Cannot delete PID file [" + daemonPidFile.asPath() + "]: " + e.getMessage(), e);
            }
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                logger.error("Shutdown of scheduler failed: " + e.getMessage(), e);
            }
            logger.info("Scheduler is shut down.");
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    private static void sendNotificationAutostart() {
        MailNotification mailNotification = new MailNotification(
                "[" + SystemUtils.getHostname() + "] auto-started",
                "m7r-infrastructure on [" + SystemUtils.getHostname() + "] was auto-started by scheduler.");
        MailNotifier.sendNotification(mailNotification, new SchedulerMailNotifierCallback());
    }

    private static void sendNotificationAutostartFailed(Exception e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        MailNotification mailNotification = new MailNotification(
                "[" + SystemUtils.getHostname() + "] autostart FAILED",
                "m7r-infrastructure [" + SystemUtils.getHostname() + "] failed to be auto-started by scheduler.\n\n"
                        + stacktrace);
        MailNotifier.sendNotification(mailNotification, new SchedulerMailNotifierCallback());
    }

}
