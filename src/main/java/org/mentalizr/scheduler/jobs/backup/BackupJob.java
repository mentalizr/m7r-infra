package org.mentalizr.scheduler.jobs.backup;

import com.google.gson.Gson;
import org.mentalizr.client.api.SessionAgent;
import org.mentalizr.client.api.backup.Backup;
import org.mentalizr.client.api.backup.BackupRequest;
import org.mentalizr.scheduler.jobs.SchedulerJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackupJob extends SchedulerJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(BackupJob.class);

    @Override
    public void schedulerExecute(JobExecutionContext jobExecutionContext, String jobConfigurationJson)
            throws JobExecutionException {

        BackupRequest backupRequest = new BackupRequest.Builder().asArchive().build();

        SessionAgent sessionAgent = SessionAgent.createFromLocalConfigWithTransientCookieStorage();
        Backup.execute(sessionAgent.getHttpCallContext(), backupRequest);
        sessionAgent.logout();
    }

    @Override
    public BackupConfiguration getJobConfiguration(String jobConfigurationJson) {
        return new Gson().fromJson(jobConfigurationJson, BackupConfiguration.class);
    }

}
