package org.mentalizr.scheduler.helper;

import de.arthurpicht.console.Console;
import de.arthurpicht.console.config.ConsoleConfiguration;
import de.arthurpicht.console.config.ConsoleConfigurationBuilder;
import de.arthurpicht.consoleToSlf4j.Slf4jChannel;
import de.arthurpicht.consoleToSlf4j.Slf4jChannelBuilder;

public class ConsoleHelper {

    public static ConsoleConfiguration redirectConsoleToLog(String loggerName) {
        ConsoleConfiguration consoleConfigurationSave = Console.getConfiguration();
        Slf4jChannel slf4jChannel = new Slf4jChannelBuilder()
                .withLoggerName(loggerName)
                .build();
        ConsoleConfiguration consoleConfigurationIntermediate = new ConsoleConfigurationBuilder()
                .withMutedOutput()
                .addMessageChannel(slf4jChannel)
                .build();
        Console.configure(consoleConfigurationIntermediate);
        return consoleConfigurationSave;
    }


}
