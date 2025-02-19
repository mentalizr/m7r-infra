package org.mentalizr.infra;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.option.OptionParserResult;

public class GlobalOptions {

    public static final String GLOBAL_OPTION__VERBOSE = "verbose";
    public static final String GLOBAL_OPTION__STACKTRACE = "stacktrace";
    public static final String GLOBAL_OPTION__SILENT = "silent";
    public static final String GLOBAL_OPTION__TIMEOUT = "timeout";
    public static final String GLOBAL_OPTION__NOTIFY = "notify";
    public static final String GLOBAL_OPTION__NO_COLOR = "no-color";

    private final boolean verbose;
    private final boolean stacktrace;
    private final boolean silent;
    private final String timeout;
    private final boolean notify;
    private final boolean noColor;

    public GlobalOptions(CliCall cliCall) {
        OptionParserResult optionParserResult = cliCall.getOptionParserResultGlobal();
        this.verbose = optionParserResult.hasOption(GLOBAL_OPTION__VERBOSE);
        this.silent = optionParserResult.hasOption(GLOBAL_OPTION__SILENT);
        this.stacktrace = optionParserResult.hasOption(GLOBAL_OPTION__STACKTRACE);
        if (optionParserResult.hasOption(GLOBAL_OPTION__TIMEOUT)) {
            this.timeout = optionParserResult.getValue(GLOBAL_OPTION__TIMEOUT);
        } else {
            this.timeout = null;
        }
        this.notify = optionParserResult.hasOption(GLOBAL_OPTION__NOTIFY);
        this.noColor = optionParserResult.hasOption(GLOBAL_OPTION__NO_COLOR);
    }

    public GlobalOptions(boolean verbose, boolean stacktrace, boolean silent, String timeout, boolean notify, boolean noColor) {
        this.verbose = verbose;
        this.stacktrace = stacktrace;
        this.silent = silent;
        this.timeout = timeout;
        this.notify = notify;
        this.noColor = noColor;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean showStacktrace() {
        return stacktrace;
    }

    public boolean isSilent() {
        return silent;
    }

    public boolean hasTimeout() {
        return timeout != null;
    }

    public String getTimeout() {
        if (timeout == null) throw new IllegalStateException("Timeout has not been set. Check before using this method.");
        return timeout;
    }

    public boolean isNotify() {
        return notify;
    }

    public boolean hasNoColor() {
        return noColor;
    }

}
