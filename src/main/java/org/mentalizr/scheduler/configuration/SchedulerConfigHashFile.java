package org.mentalizr.scheduler.configuration;

import de.arthurpicht.utils.io.file.SingleValueFile;
import de.arthurpicht.utils.io.nio2.FileUtils;
import org.mentalizr.scheduler.m7rPaths.M7rSchedulerConfigHashFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SchedulerConfigHashFile {

    private static final Path configHashFile = new M7rSchedulerConfigHashFile().asPath();

    public static boolean exists() {
        return FileUtils.isExistingRegularFile(configHashFile);
    }

    public static void write(String hash) throws IOException {
        if (!FileUtils.isExistingDirectory(configHashFile.getParent()))
            Files.createDirectories(configHashFile.getParent());
        SingleValueFile singleValueFile = new SingleValueFile(configHashFile);
        singleValueFile.write(hash);
    }

    public static String read() throws IOException {
        SingleValueFile singleValueFile = new SingleValueFile(configHashFile);
        return singleValueFile.read();
    }

}
