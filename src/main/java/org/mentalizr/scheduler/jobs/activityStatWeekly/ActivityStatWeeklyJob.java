package org.mentalizr.scheduler.jobs.activityStatWeekly;

import com.google.gson.Gson;
import de.arthurpicht.console.Console;
import de.arthurpicht.console.config.ConsoleConfiguration;
import org.mentalizer.mailer.MailConfiguration;
import org.mentalizer.mailer.MailConfigurationException;
import org.mentalizer.mailer.MailConfigurationLoader;
import org.mentalizr.cli.commands.user.activity.stat.activityStatPeriod.ActivityStatPeriod;
import org.mentalizr.cli.commands.user.activity.stat.activityStatPeriod.PeriodWeek;
import org.mentalizr.client.api.ClientApiRuntimeException;
import org.mentalizr.client.api.SessionAgent;
import org.mentalizr.client.api.activityStat.ActivityStat;
import org.mentalizr.client.api.activityStat.ActivityStatRequest;
import org.mentalizr.scheduler.helper.ConsoleHelper;
import org.mentalizr.scheduler.jobs.SchedulerJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class ActivityStatWeeklyJob extends SchedulerJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ActivityStatWeeklyJob.class);

    @Override
    public void schedulerExecute(JobExecutionContext jobExecutionContext, String jobConfigurationJson)
            throws JobExecutionException {

        ActivityStatWeeklyConfiguration activityStatWeeklyConfiguration
                = getJobConfiguration(jobConfigurationJson);

        ActivityStatPeriod activityStatPeriod = new ActivityStatPeriod(new PeriodWeek(-1));
        ActivityStatRequest activityStatRequest
                = ActivityStatWeeklyHelper.createActivityStatRequest(
                activityStatPeriod,
                activityStatWeeklyConfiguration);

        ConsoleConfiguration consoleConfigurationSave = ConsoleHelper.redirectConsoleToLog("ActivityStatWeekly");
        try {
            SessionAgent sessionAgent = SessionAgent.createFromLocalConfigWithTransientCookieStorage();
            MailConfiguration mailConfiguration = obtainMailConfiguration();
            ActivityStat.execute(sessionAgent.getHttpCallContext(), activityStatRequest, mailConfiguration, false);
        } catch (ClientApiRuntimeException e) {
            throw new JobExecutionException(e);
        } finally {
            Console.configure(consoleConfigurationSave);
        }
    }

    @Override
    public ActivityStatWeeklyConfiguration getJobConfiguration(String jobConfigurationJson) {
        return new Gson().fromJson(jobConfigurationJson, ActivityStatWeeklyConfiguration.class);
    }

    private MailConfiguration obtainMailConfiguration() throws JobExecutionException {
        try {
            return MailConfigurationLoader.load();
        } catch (MailConfigurationException e) {
            throw new RuntimeException("Error loading mail configuration: " + e.getMessage(), e);
        }
    }

}
