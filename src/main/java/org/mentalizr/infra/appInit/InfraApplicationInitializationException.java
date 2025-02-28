package org.mentalizr.infra.appInit;

public class InfraApplicationInitializationException extends Exception {

    public InfraApplicationInitializationException() {
    }

    public InfraApplicationInitializationException(String message) {
        super(message);
    }

    public InfraApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfraApplicationInitializationException(Throwable cause) {
        super(cause);
    }

    public InfraApplicationInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
