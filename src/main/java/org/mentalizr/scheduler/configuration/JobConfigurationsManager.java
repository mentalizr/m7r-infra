package org.mentalizr.scheduler.configuration;

import de.arthurpicht.utils.io.nio2.FileUtils;
import org.mentalizr.commons.paths.host.hostDir.M7rSchedulerConfigDir;
import org.mentalizr.scheduler.SchedulerRuntimeException;
import org.mentalizr.scheduler.helper.Checksums;
import org.mentalizr.scheduler.helper.FileHelper;
import org.mentalizr.scheduler.jobFactories.JobConfigurationFactory;
import org.mentalizr.scheduler.jobs.JobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class JobConfigurationsManager {

    private static final Logger logger = LoggerFactory.getLogger(JobConfigurationsManager.class);

    public static JobConfigurations fromConfigFiles() {
        return fromConfigFiles(new M7rSchedulerConfigDir().asPath());
    }

    public static JobConfigurations fromConfigFiles(Path configDir) {
        List<Path> configurationFiles = scanSchedulerConfigDir(configDir);

        logger.debug(configurationFiles.size() + " configuration files found.");
        for (Path path : configurationFiles) {
            logger.debug("found configuration file: " + path.toString());
        }

        List<JobConfiguration> jobConfigurations = new ArrayList<>();

        for (Path configurationFile : configurationFiles) {
            JobConfiguration jobConfiguration = JobConfigurationFactory.create(configurationFile);
            jobConfigurations.add(jobConfiguration);
        }

        return new JobConfigurations(jobConfigurations);
    }

    public static void saveHash() {
        Path configDir = new M7rSchedulerConfigDir().asPath();
        saveHash(configDir);
    }

    public static void saveHash(Path configDir) {
        List<Path> configurationFiles = scanSchedulerConfigDir(configDir);
        saveHashToFile(configurationFiles);
    }

    public static boolean hasUnmodifiedConfiguration() {
        Path schedulerConfigDir = new M7rSchedulerConfigDir().asPath();
        if (!SchedulerConfigHashFile.exists())
            return false;
        List<Path> configurationFiles = scanSchedulerConfigDir(schedulerConfigDir);
        String currentHash;
        try {
            currentHash = Checksums.computeSha256Checksum(configurationFiles);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new SchedulerRuntimeException("Error computing config hash: " + e.getMessage(), e);
        }
        String savedHash;
        try {
            savedHash = SchedulerConfigHashFile.read();
        } catch (IOException e) {
            throw new SchedulerRuntimeException("Error reading scheduler config hash file: " + e.getMessage(), e);
        }
        return currentHash.equals(savedHash);
    }

    private static List<Path> scanSchedulerConfigDir(Path configDir) {
        if (!FileUtils.isExistingDirectory(configDir))
            throw new JobConfigurationException("The config directory does not exist: " +
                    "[" + configDir.toAbsolutePath() + "].");

        try {
            return FileHelper.getRegularFilesNotEndingWithTildeInDirectory(configDir);
        } catch (IOException e) {
            throw new SchedulerRuntimeException("Could not scan directory: [" + configDir.toAbsolutePath() + "].", e);
        }
    }

    private static void saveHashToFile(List<Path> configurationFiles) {
        try {
            String hash = Checksums.computeSha256Checksum(configurationFiles);
            SchedulerConfigHashFile.write(hash);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new SchedulerRuntimeException("Error on saving scheduler configuration hash file: " + e.getMessage(), e);
        }
    }

}
