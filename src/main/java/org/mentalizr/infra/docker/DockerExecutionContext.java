package org.mentalizr.infra.docker;

import org.slf4j.Logger;
import org.slf4j.event.Level;

public class DockerExecutionContext {

    private final Logger logger;
    private final Level logLevelStdOut;
    private final Level logLevelStdErr;

    public static class Builder {
        private Logger logger = null;
        private Level logLevelStdOut = Level.DEBUG;
        private Level logLevelStdErr = Level.ERROR;

        public Builder withLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder withLogLevelStdOut(Level logLevel) {
            this.logLevelStdOut = logLevel;
            return this;
        }

        public Builder withLogLevelStdErr(Level logLevel) {
            this.logLevelStdErr = logLevel;
            return this;
        }

        public DockerExecutionContext build() {
            return new DockerExecutionContext(this.logger, this.logLevelStdOut, this.logLevelStdErr);
        }
    }

    private DockerExecutionContext(Logger logger, Level logLevelStdOut, Level logLevelStdErr) {
        this.logger = logger;
        this.logLevelStdOut = logLevelStdOut;
        this.logLevelStdErr = logLevelStdErr;
    }

    public Logger getLogger() {
        return logger;
    }

    public Level getLogLevelStdOut() {
        return logLevelStdOut;
    }

    public Level getLogLevelStdErr() {
        return logLevelStdErr;
    }

}
