package org.mentalizr.scheduler.m7rPaths;

import org.mentalizr.commons.paths.FileNames;
import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.commons.paths.host.hostDir.M7rHostRunDir;

public class M7rSchedulerActiveFlagFile extends M7rFile {

    public M7rSchedulerActiveFlagFile() {
        super(new M7rHostRunDir(), FileNames.SCHEDULER_ACTIVE_FLAG_FILE);
    }

}
