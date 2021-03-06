package org.mentalizr.infra;

import de.arthurpicht.cli.CliCall;
import org.mentalizr.infra.docker.DockerExecutionContext;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class ExecutionContext {

    private static Instant callTimestamp;
    private static DockerExecutionContext dockerExecutionContext = null;
    private static CliCall cliCall;
    private static boolean verbose;
    private static boolean showStacktrace;

    public static void initialize(CliCall cliCall) {
        callTimestamp = Instant.now();
        ExecutionContext.dockerExecutionContext = new DockerExecutionContext.Builder()
                .beVerbose(cliCall.getOptionParserResultGlobal().hasOption(InfraCli.OPTION_VERBOSE))
                .withLogger(LoggerFactory.getLogger(Const.DOCKER_LOGGER))
                .build();
        ExecutionContext.cliCall = cliCall;
        verbose = cliCall.getOptionParserResultGlobal().hasOption(InfraCli.OPTION_VERBOSE);
        showStacktrace = cliCall.getOptionParserResultGlobal().hasOption(InfraCli.OPTION_STACKTRACE);
    }

    public static Instant getCallTimestamp() {
        return callTimestamp;
    }

    public static DockerExecutionContext getDockerExecutionContext() {
        if (ExecutionContext.dockerExecutionContext == null)
            throw new IllegalStateException(ExecutionContext.class.getSimpleName() + " not initialized yet.");
        return ExecutionContext.dockerExecutionContext;
    }

    public static boolean isVerbose() {
        return verbose;
    }

    public static boolean showStacktrace() {
        return showStacktrace;
    }

    public static CliCall getCliCall() {
        return cliCall;
    }

}
