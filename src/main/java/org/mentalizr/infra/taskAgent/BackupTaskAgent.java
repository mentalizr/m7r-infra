package org.mentalizr.infra.taskAgent;

import de.arthurpicht.console.Console;
import de.arthurpicht.console.message.MessageBuilder;
import org.mentalizr.client.api.ClientApiRuntimeException;
import org.mentalizr.client.api.SessionAgent;
import org.mentalizr.client.api.backup.Backup;
import org.mentalizr.client.api.backup.BackupRequest;
import org.mentalizr.commons.paths.host.hostDir.BackupDestinationDir;
import org.mentalizr.infra.InfraRuntimeException;

public class BackupTaskAgent {

    public static void backup() {
        Console.out(new MessageBuilder().terminatePreviousLine().build());
        BackupDestinationDir backupDestinationDir = new BackupDestinationDir();
        try {
            BackupRequest backupRequest = new BackupRequest.Builder()
                    .withDestinationDir(backupDestinationDir.asPath())
                    .build();
            SessionAgent sessionAgent = SessionAgent.createFromLocalConfigWithTransientCookieStorage();
            Backup.execute(sessionAgent.getRESTCallContext(), backupRequest);
            sessionAgent.logout();
        } catch (ClientApiRuntimeException e) {
            throw new InfraRuntimeException("Backup failed. " + e.getMessage(), e);
        }
    }

}
