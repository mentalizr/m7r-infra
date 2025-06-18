package org.mentalizr.scheduler.jobs.watchdog;

import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.commons.paths.host.hostDir.M7rHostRunDir;

public class M7rInfraRestartFile extends M7rFile {

    public M7rInfraRestartFile() {
        super(new M7rHostRunDir(), "infra-restart.timestamp");
    }

}
