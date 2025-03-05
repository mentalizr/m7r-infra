package org.mentalizr.infra.taskAgent;

import org.mentalizr.clientSdk.ClientSdkException;
import org.mentalizr.clientSdk.SessionAgent;
import org.mentalizr.clientSdk.backup.Backup;
import org.mentalizr.clientSdk.backup.BackupRequest;
import org.mentalizr.commons.paths.host.hostDir.BackupDestinationDir;
import org.mentalizr.infra.InfraRuntimeException;

public class BackupTaskAgent {

    public static void backup() {
        BackupDestinationDir backupDestinationDir = new BackupDestinationDir();
        try {
            BackupRequest backupRequest = new BackupRequest.Builder()
                    .withDestinationDir(backupDestinationDir.asPath())
                    .build();
            SessionAgent sessionAgent = SessionAgent.createFromLocalConfigWithTransientCookieStorage();
            Backup.exec(sessionAgent.getRESTCallContext(), backupRequest);
            sessionAgent.logout();
        } catch (ClientSdkException e) {
            throw new InfraRuntimeException("Backup failed. " + e.getMessage(), e);
        }
    }

}
