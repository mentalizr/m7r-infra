package org.mentalizr.scheduler.processManagement;

import de.arthurpicht.utils.io.file.SingleValueFile;
import de.arthurpicht.utils.io.nio2.FileUtils;
import org.mentalizr.commons.paths.host.hostDir.M7rIntentionFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IntentionFile {

    public enum Intention {UP, DOWN, UNKNOWN}

    private static final Path intentionFilePath = new M7rIntentionFile().asPath();

    public static void createUp() {
        Path parentDir = intentionFilePath.getParent();
        try {
            if (!FileUtils.isExistingDirectory(parentDir))
                Files.createDirectories(parentDir);
            SingleValueFile intentionFile = new SingleValueFile(intentionFilePath);
            intentionFile.write("up");
        } catch (IOException e) {
            throw new RuntimeException("Creating intention file failed: " + e.getMessage(), e);
        }
    }

    public static void createDown() {
        Path parentDir = intentionFilePath.getParent();
        try {
            if (!FileUtils.isExistingDirectory(parentDir))
                Files.createDirectories(parentDir);
            SingleValueFile intentionFile = new SingleValueFile(intentionFilePath);
            intentionFile.write("down");
        } catch (IOException e) {
            throw new RuntimeException("Creating intention file failed: " + e.getMessage(), e);
        }
    }

    public static Intention getIntention() {
        SingleValueFile intentionFile = new SingleValueFile(intentionFilePath);
        String line;
        try {
            line = intentionFile.read().trim();
        } catch (IOException e) {
            throw new RuntimeException("Reading intention file failed: " + e.getMessage(), e);
        }
        if (line.toUpperCase().equals(Intention.UP.toString())) return Intention.UP;
        if (line.toUpperCase().equals(Intention.DOWN.toString())) return Intention.DOWN;
        return Intention.UNKNOWN;
    }

}
