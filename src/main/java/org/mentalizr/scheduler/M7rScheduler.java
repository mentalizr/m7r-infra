package org.mentalizr.scheduler;

import de.arthurpicht.utils.core.exception.ExceptionUtils;
import de.arthurpicht.utils.core.system.SystemUtils;
import org.mentalizer.mailer.notifier.MailNotification;
import org.mentalizer.mailer.notifier.MailNotifier;
import org.mentalizr.infra.appInit.InfraApplicationInitialization;
import org.mentalizr.infra.appInit.InfraApplicationInitializationException;
import org.mentalizr.infra.executors.Restart;
import org.mentalizr.infra.externalApi.StatusSummary;
import org.mentalizr.scheduler.configuration.JobConfigurations;
import org.mentalizr.scheduler.configuration.JobConfigurationsManager;
import org.mentalizr.scheduler.configuration.infra.InfraConfig;
import org.mentalizr.scheduler.configuration.infra.InfraConfigLoader;
import org.mentalizr.scheduler.helper.LinuxHelper;
import org.mentalizr.scheduler.jobInitialization.JobInitializer;
import org.mentalizr.scheduler.mailNotifier.SchedulerMailNotifierCallback;
import org.mentalizr.scheduler.processManagement.DaemonPidFile;
import org.mentalizr.scheduler.processManagement.IntentionFile;
import org.mentalizr.scheduler.processManagement.IntentionFile.Intention;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mentalizr.scheduler.processManagement.IntentionFile.Intention.*;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class M7rScheduler {

    private static final Logger logger = LoggerFactory.getLogger(M7rScheduler.class);
    private static final DaemonPidFile daemonPidFile = new DaemonPidFile();

    public static void main(String[] args) {

        try {
            InfraApplicationInitialization.asScheduler();
        } catch (InfraApplicationInitializationException | SchedulerRuntimeException e) {
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
            InfraConfig infraConfig = InfraConfigLoader.load();
            Intention intention = IntentionFile.getIntention();
            if (infraConfig.isInfraAutostart() && intention == UP) {
                logger.info("auto-starting infrastructure ...");
                StatusSummary statusSummary = StatusSummary.create();
                if (statusSummary.isRunning()) {
                    logger.info("Infrastructure is already running. No autostart performed.");
                } else {
                    logger.warn("Perform infrastructure restart ...");
                    Restart.perform();
                    logger.info("Infrastructure successfully started by autostart.");
                    sendNotificationAutostart();
                }
            } else if (infraConfig.isInfraAutostart() && intention != UP) {
                logger.debug("auto-starting of infrastructure is configured but omitted as intention is not UP.");
            }
        } catch (MailNotification.MailNotificationRuntimeException e) {
            logger.error("Error sending mail notification: " + e.getMessage());
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
