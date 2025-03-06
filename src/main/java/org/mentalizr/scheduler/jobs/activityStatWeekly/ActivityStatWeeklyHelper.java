package org.mentalizr.scheduler.jobs.activityStatWeekly;

import org.mentalizr.cli.commands.user.activity.stat.activityStatPeriod.ActivityStatPeriod;
import org.mentalizr.client.api.activityStat.ActivityStatRequest;

import java.util.List;

public class ActivityStatWeeklyHelper {

    public static ActivityStatRequest createActivityStatRequest(
            ActivityStatPeriod activityStatPeriod,
            ActivityStatWeeklyConfiguration activityStatWeeklyConfiguration
    ) {

        ActivityStatRequest.Builder activityStatRequestBuilder =
                new ActivityStatRequest.Builder()
                        .withFromTimestamp(activityStatPeriod.getFromTimestamp())
                        .withUntilTimestamp(activityStatPeriod.getUntilTimestamp());

        if (activityStatWeeklyConfiguration.hasPrograms()) {
            activityStatRequestBuilder
                    .withProgramsIncludeMode(true)
                    .withPrograms(activityStatWeeklyConfiguration.getPrograms());
        }

        if (activityStatWeeklyConfiguration.hasExcludePrograms()) {
            activityStatRequestBuilder
                    .withProjectIncludeMode(false)
                    .withPrograms(activityStatWeeklyConfiguration.getExcludePrograms());
        }

        if (activityStatWeeklyConfiguration.hasProjects()) {
            activityStatRequestBuilder
                    .withProjectIncludeMode(true)
                    .withProjects(activityStatWeeklyConfiguration.getProjects());
        }

        if (activityStatWeeklyConfiguration.hasExcludeProjects()) {
            activityStatRequestBuilder
                    .withProjectIncludeMode(false)
                    .withProjects(activityStatWeeklyConfiguration.getExcludeProjects());
        }

        List<String> mailRecipients = activityStatWeeklyConfiguration.getRecipients();
        activityStatRequestBuilder
                .withSendAsMail(true)
                .withMailRecipients(mailRecipients)
                .withMailSubject("Wöchentliche Aktivitätsstatistik");

        return activityStatRequestBuilder.build();
    }

}
