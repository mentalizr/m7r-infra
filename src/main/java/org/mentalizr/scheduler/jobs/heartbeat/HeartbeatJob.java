package org.mentalizr.scheduler.jobs.heartbeat;

import com.google.gson.Gson;
import de.arthurpicht.utils.logging.LogbackConfigStatus;
import org.mentalizr.scheduler.jobs.SchedulerJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartbeatJob extends SchedulerJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatJob.class);

    @Override
    public void schedulerExecute(JobExecutionContext jobExecutionContext, String jobConfigurationJson) throws JobExecutionException {
        HeartbeatConfiguration heartbeatConfiguration = getJobConfiguration(jobConfigurationJson);
        String logMessage = heartbeatConfiguration.logMessage();
        logger.info(logMessage);
    }

    @Override
    public HeartbeatConfiguration getJobConfiguration(String jobConfigurationJson) {
        return new Gson().fromJson(jobConfigurationJson, HeartbeatConfiguration.class);
    }

}
