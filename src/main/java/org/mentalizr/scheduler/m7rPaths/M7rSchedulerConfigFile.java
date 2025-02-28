package org.mentalizr.scheduler.m7rPaths;

import org.mentalizr.commons.paths.FileNames;
import org.mentalizr.commons.paths.M7rFile;
import org.mentalizr.commons.paths.host.hostDir.M7rHostConfigDir;

public class M7rSchedulerConfigFile extends M7rFile {

    public M7rSchedulerConfigFile() {
        super(new M7rHostConfigDir(), FileNames.M7R_INFRA_CONF);
    }

}
