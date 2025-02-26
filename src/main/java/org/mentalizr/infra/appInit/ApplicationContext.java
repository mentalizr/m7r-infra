package org.mentalizr.infra.appInit;

import org.mentalizr.backend.config.infraUser.InfraUserConfiguration;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.GlobalOptions;
import org.mentalizr.infra.Timeout;
import org.mentalizr.infra.docker.DockerExecutionContext;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.time.Instant;

public class ApplicationContext {

    private static Instant callTimestamp;
    private static InfraUserConfiguration infraUserConfiguration;
    private static Timeout timeout = Timeout.getDefaultTimeout();
    private static GlobalOptions globalOptions;
    private static DockerExecutionContext dockerExecutionContext = null;

    private static boolean isInitialized = false;

    public static void initialize(GlobalOptions globalOptions) {
        callTimestamp = Instant.now();
        infraUserConfiguration = new InfraUserConfiguration();
        timeout = getTimeout(globalOptions);
        ApplicationContext.globalOptions = globalOptions;
        dockerExecutionContext = new DockerExecutionContext.Builder()
                .withLogger(LoggerFactory.getLogger(Const.DOCKER_LOGGER))
                .withLogLevelStdOut(Level.DEBUG)
                .withLogLevelStdErr(Level.ERROR)
                .build();
        isInitialized = true;
    }

    @Deprecated
    public static void initializeWithDefaults() {
        GlobalOptions globalOptions = new GlobalOptions(
                false,
                false,
                false,
                null,
                false,
                true);
        initialize(globalOptions);
    }

    public static Instant getCallTimestamp() {
        assertIsInitialized();
        return callTimestamp;
    }

    public static InfraUserConfiguration getInfraUserConfiguration() {
        assertIsInitialized();
        return infraUserConfiguration;
    }

    public static Timeout getTimeout() {
        assertIsInitialized();
        return timeout;
    }

    public static boolean isVerbose() {
        return globalOptions.isVerbose();
    }

    public static boolean showStacktrace() {
        return globalOptions.showStacktrace();
    }

    public static boolean isNotify() {
        return globalOptions.isNotify();
    }

    public static DockerExecutionContext getDockerExecutionContext() {
        if (dockerExecutionContext == null)
            throw new IllegalStateException(ApplicationContext.class.getSimpleName() + " not initialized yet.");
        return dockerExecutionContext;
    }

    private static Timeout getTimeout(GlobalOptions globalOptions) {
        if (globalOptions.hasTimeout()) {
            String timeoutString = globalOptions.getTimeout();
            try {
                int timeout = Integer.parseInt(timeoutString);
                return Timeout.getTimeout(timeout);
            } catch (NumberFormatException e) {
                throw new RuntimeException("No valid value for 'timeout' parameter.");
            }
        }
        return Timeout.getDefaultTimeout();
    }

    private static void assertIsInitialized() {
        if (!isInitialized) throw new IllegalStateException("ApplicationContext not yet initialized.");
    }

}
