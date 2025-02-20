package org.mentalizr.scheduler.m7rPaths;

import org.mentalizr.commons.paths.FileNames;
import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.commons.paths.host.hostDir.M7rHostConfigDir;
import org.mentalizr.commons.paths.host.hostDir.M7rHostRunDir;

public class M7rSchedulerConfigFile extends M7rFile {

    public M7rSchedulerConfigFile() {
        super(new M7rHostConfigDir(), FileNames.SCHEDULER_CONF);
    }

}
