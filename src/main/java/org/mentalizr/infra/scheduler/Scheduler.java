package org.mentalizr.infra.scheduler;

import org.mentalizr.scheduler.configuration.JobConfigurationsManager;
import org.mentalizr.scheduler.configuration.SchedulerActiveFlagFile;
import org.mentalizr.scheduler.processManagement.SchedulerProcess;

import java.io.IOException;

public class Scheduler {

    public static boolean isRunning() {
        return SchedulerProcess.isRunning();
    }

    public static boolean isStopped() {
        return SchedulerProcess.isStopped();
    }

    public static void start() {
        SchedulerProcess.start();
    }

    public static void stop() {
        SchedulerProcess.stop();
    }

    public static boolean isActive() {
        return SchedulerActiveFlagFile.exists();
    }

    public static boolean isNotActive() {
        return !SchedulerActiveFlagFile.exists();
    }

    public static void activate() {
        try {
            SchedulerActiveFlagFile.create();
        } catch (IOException e) {
            throw new RuntimeException("Error creating scheduler active flag file: " + e.getMessage(), e);
        }
    }

    public static void deactivate() {
        try {
            SchedulerActiveFlagFile.delete();
        } catch (IOException e) {
            throw new RuntimeException("Error deleting scheduler active flag file: " + e.getMessage(), e);
        }
    }

    public static boolean hasUnmodifiedConfiguration() {
        return JobConfigurationsManager.hasUnmodifiedConfiguration();
    }

}
