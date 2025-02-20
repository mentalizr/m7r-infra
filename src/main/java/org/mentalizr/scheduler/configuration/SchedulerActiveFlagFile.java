package org.mentalizr.scheduler.configuration;

import de.arthurpicht.utils.io.file.SingleValueFile;
import de.arthurpicht.utils.io.nio2.FileUtils;
import org.mentalizr.scheduler.m7rPaths.M7rSchedulerActiveFlagFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SchedulerActiveFlagFile {

    private static final Path activeFlagFile = new M7rSchedulerActiveFlagFile().asPath();

    public static boolean exists() {
        return FileUtils.isExistingRegularFile(activeFlagFile);
    }

    public static void create() throws IOException {
        if (exists())
            throw new IllegalStateException("SchedulerActiveFlagFile file already exists. Check before calling.");
        if (!FileUtils.isExistingDirectory(activeFlagFile.getParent()))
            Files.createDirectories(activeFlagFile.getParent());
        SingleValueFile singleValueFile = new SingleValueFile(activeFlagFile);
        singleValueFile.write("Scheduler active flag file. Existence of file indicates that scheduler is active.");
    }

    public static void delete() throws IOException {
        if (!exists())
            throw new IllegalStateException("SchedulerActiveFlagFile does not exist. Check before calling.");
        Files.deleteIfExists(activeFlagFile);
    }

}
