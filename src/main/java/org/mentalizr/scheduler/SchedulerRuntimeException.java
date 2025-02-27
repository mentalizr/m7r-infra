package org.mentalizr.scheduler;

public class SchedulerRuntimeException extends RuntimeException {

    public SchedulerRuntimeException() {
    }

    public SchedulerRuntimeException(String message) {
        super(message);
    }

    public SchedulerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchedulerRuntimeException(Throwable cause) {
        super(cause);
    }

    public SchedulerRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
