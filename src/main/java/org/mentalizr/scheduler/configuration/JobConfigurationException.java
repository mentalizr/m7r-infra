package org.mentalizr.scheduler.configuration;

import org.mentalizr.scheduler.SchedulerRuntimeException;

@SuppressWarnings("unused")
public class JobConfigurationException extends SchedulerRuntimeException {

    public JobConfigurationException(String message) {
        super(message);
    }

}
