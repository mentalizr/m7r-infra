package org.mentalizr.infra.taskAgent;

import de.arthurpicht.console.Console;
import de.arthurpicht.console.config.ConsoleConfiguration;
import de.arthurpicht.console.config.ConsoleConfigurationBuilder;
import de.arthurpicht.consoleToSlf4j.Slf4jChannel;
import de.arthurpicht.consoleToSlf4j.Slf4jChannelBuilder;
import org.mentalizr.client.api.ClientApiRuntimeException;
import org.mentalizr.client.api.SessionAgent;
import org.mentalizr.client.api.recover.Recover;
import org.mentalizr.client.api.recover.RecoverLatest;
import org.mentalizr.client.api.recover.RecoverRequestFull;
import org.mentalizr.client.api.recover.RecoverRequestLatest;
import org.mentalizr.client.http.httpClient.HttpCallContext;
import org.mentalizr.client.service.ServiceCaller;
import org.mentalizr.commons.paths.host.hostDir.BackupDefaultDir;
import org.mentalizr.infra.InfraRuntimeException;
import org.mentalizr.infra.buildEntities.Backups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class RecoverTaskAgent {

    private static final Logger logger = LoggerFactory.getLogger(RecoverTaskAgent.class.getSimpleName());

    public static boolean isDatabaseNotEmpty() {
        try {
            SessionAgent sessionAgent = SessionAgent.createFromLocalConfigWithTransientCookieStorage();
            boolean isEmpty = !isDatabaseEmpty(sessionAgent.getHttpCallContext());
            sessionAgent.logout();
            return isEmpty;
        } catch (ClientApiRuntimeException e) {
            throw new InfraRuntimeException("Determining database status failed. " + e.getMessage(), e);
        }
    }

    public static void recoverDev() {
        BackupDefaultDir backupDefaultDir = new BackupDefaultDir();
        logger.info("Recover from backup for dev.");
        ConsoleConfiguration consoleConfigurationSave = alterConsoleConfiguration();
        try {
            SessionAgent sessionAgent = SessionAgent.createFromLocalConfigWithTransientCookieStorage();
            RecoverRequestFull recoverRequestFull = new RecoverRequestFull(
                    backupDefaultDir.asPath(),
                    false,
                    true);
            Recover.execute(recoverRequestFull, sessionAgent.getHttpCallContext());
            sessionAgent.logout();
        } catch (ClientApiRuntimeException e) {
            throw new InfraRuntimeException("Recover from dev backup failed. " + e.getMessage(), e);
        } finally {
            Console.configure(consoleConfigurationSave);
        }
    }

    public static void recoverLatest() {
        if (!Backups.hasBackup())
            throw new InfraRuntimeException("No backup found.");
        Path lastestBackupPath = Backups.getLatestBackup();
        logger.info("Recover latest backup: [" + lastestBackupPath.toAbsolutePath() + "].");
        ConsoleConfiguration consoleConfigurationSave = alterConsoleConfiguration();
        try {
            SessionAgent sessionAgent = SessionAgent.createFromLocalConfigWithTransientCookieStorage();
            RecoverRequestLatest recoverRequestLatest = new RecoverRequestLatest(false);
            RecoverLatest.execute(recoverRequestLatest, sessionAgent.getHttpCallContext());
            sessionAgent.logout();
        } catch (ClientApiRuntimeException e) {
            throw new InfraRuntimeException("Recover latest backup failed. " + e.getMessage(), e);
        } finally {
            Console.configure(consoleConfigurationSave);
        }
    }

    private static ConsoleConfiguration alterConsoleConfiguration() {
        ConsoleConfiguration consoleConfigurationSave = Console.getConfiguration();
        Slf4jChannel slf4jChannel = new Slf4jChannelBuilder()
                .withLoggerName("Recover")
                .build();
        ConsoleConfiguration consoleConfigurationIntermediate = new ConsoleConfigurationBuilder()
                .withMutedOutput()
                .addMessageChannel(slf4jChannel)
                .build();
        Console.configure(consoleConfigurationIntermediate);
        return consoleConfigurationSave;
    }

    private static boolean isDatabaseEmpty(HttpCallContext httpCallContext) throws ClientApiRuntimeException {
        if (ServiceCaller.isDbEmpty(httpCallContext)) {
            Console.printlnVerbose("DB is empty.");
            return true;
        } else {
            Console.printlnVerbose("DB not empty.");
            return false;
        }
    }

}
