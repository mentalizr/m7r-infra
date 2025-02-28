package org.mentalizr.scheduler.jobs.activityStatWeekly;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.utils.core.collection.Sets;
import org.mentalizr.scheduler.configuration.JobConfigurationException;
import org.mentalizr.scheduler.jobs.BaseConfiguration;
import org.mentalizr.scheduler.jobs.JobConfigurationParser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mentalizr.scheduler.jobs.activityStatWeekly.ActivityStatWeeklyConfiguration.*;

public class ActivityStatWeeklyConfigurationParser extends JobConfigurationParser {

    public ActivityStatWeeklyConfigurationParser(BaseConfiguration baseConfiguration, Configuration configuration, Path configurationPath) {
        super(baseConfiguration, configuration, configurationPath);
    }

    @Override
    public ActivityStatWeeklyConfiguration parse() {

        checkForParameterSyntaxErrors(Sets.newHashSet(PROGRAMS, EXCLUDE_PROGRAMS, PROJECTS, EXCLUDE_PROJECTS, RECIPIENTS));
        checkForMandatoryParameters(Sets.newHashSet(RECIPIENTS));

        if (configuration.containsKey(PROGRAMS) && configuration.containsKey(EXCLUDE_PROGRAMS))
            throw new JobConfigurationException("Job configuration [" + this.configurationFile.toAbsolutePath() + "] " +
                                            "has contradictions: [" + PROGRAMS + "] and [" + EXCLUDE_PROGRAMS + "].");
        if (configuration.containsKey(PROJECTS) && configuration.containsKey(EXCLUDE_PROJECTS))
            throw new JobConfigurationException("Job configuration [" + this.configurationFile.toAbsolutePath() + "] " +
                                            "has contradictions: [" + PROJECTS + "] and [" + EXCLUDE_PROJECTS + "].");

        Set<String> programs = getValueSet(configuration, PROGRAMS);
        Set<String> excludePrograms = getValueSet(configuration, EXCLUDE_PROGRAMS);
        Set<String> projects = getValueSet(configuration, PROJECTS);
        Set<String> excludeProjects = getValueSet(configuration, EXCLUDE_PROJECTS);
        List<String> recipients = getValueList(configuration, RECIPIENTS);

        return new ActivityStatWeeklyConfiguration(
                baseConfiguration,
                programs,
                excludePrograms,
                projects,
                excludeProjects,
                recipients);
    }

    private Set<String> getValueSet(Configuration configuration, String parameter) {
        if (configuration.containsKey(parameter)) {
            List<String> valueList = configuration.getStringList(parameter);
            return Set.copyOf(valueList);
        } else {
            return new HashSet<>();
        }
    }

    @SuppressWarnings("SameParameterValue")
    private List<String> getValueList(Configuration configuration, String parameter) {
        if (configuration.containsKey(parameter)) {
            return configuration.getStringList(parameter);
        } else {
            return new ArrayList<>();
        }
    }

}
