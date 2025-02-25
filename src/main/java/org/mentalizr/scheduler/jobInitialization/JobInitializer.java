package org.mentalizr.scheduler.jobInitialization;

import com.google.gson.Gson;
import org.mentalizr.scheduler.configuration.JobConfigurations;
import org.mentalizr.scheduler.jobFactories.SchedulerJobFactory;
import org.mentalizr.scheduler.jobs.JobConfiguration;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mentalizr.scheduler.Const.CONFIGURATION_KEY;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class JobInitializer {

    private static final Logger logger = LoggerFactory.getLogger(JobInitializer.class);

    public static void initialize(Scheduler scheduler, JobConfigurations jobConfigurations) throws SchedulerException {
        logger.info("number of jobs to schedule: " + jobConfigurations.getJobConfigurations().size());
        for (JobConfiguration jobConfiguration : jobConfigurations.getJobConfigurations()) {
            scheduleJob(scheduler, jobConfiguration);
        }
    }

    private static void scheduleJob(
            Scheduler scheduler,
            JobConfiguration jobConfiguration
            ) throws SchedulerException {

        String name = jobConfiguration.getJobName();
        String triggerName = jobConfiguration.getTriggerName();
        String cronSchedule = jobConfiguration.getCronSchedule();
        String jobTypeName = jobConfiguration.getTypeString();
        String jobJson = new Gson().toJson(jobConfiguration);

        logger.info("Scheduling job [{}] of type [{}].", name, jobTypeName);
        logger.debug("with job configuration: {}", jobJson);

        JobDetail job = JobBuilder.newJob(SchedulerJobFactory.getJobClass(jobConfiguration))
                .withIdentity(name, jobTypeName)
                .usingJobData(CONFIGURATION_KEY, jobJson)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, jobTypeName + "-trigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
                .build();

        scheduler.scheduleJob(job, trigger);
    }

}
