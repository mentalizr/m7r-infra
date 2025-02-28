package org.mentalizr.infra.executors;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.console.Console;
import de.arthurpicht.console.message.MessageBuilder;
import de.arthurpicht.console.message.format.BlockFormat;
import org.mentalizr.infra.scheduler.Scheduler;
import org.mentalizr.scheduler.configuration.JobConfigurations;
import org.mentalizr.scheduler.configuration.JobConfigurationsManager;
import org.mentalizr.scheduler.jobInitialization.JobOverview;
import org.mentalizr.scheduler.jobInitialization.JobOverview.JobOverviewRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class SchedulerShowExecutor implements CommandExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerShowExecutor.class);

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {
        logger.info(SchedulerShowExecutor.class.getSimpleName() + " invoked.");

        Console.out(new MessageBuilder()
                .addText("scheduler:",
                        new BlockFormat.Builder()
                                .withWidth(18)
                                .build())
                .withNoLineFeed()
                .build());

        boolean schedulerRunning = Scheduler.isRunning();
        boolean schedulerActive = Scheduler.isActive();

        if (schedulerRunning) {
            Console.out(new MessageBuilder()
                    .addText("RUNNING", BlockFormat.GREEN_TEXT())
                    .withNoLineFeed()
                    .build());
        } else {
            Console.out(new MessageBuilder()
                    .addText("STOPPED", BlockFormat.RED_TEXT())
                    .withNoLineFeed()
                    .build());
        }
        if (schedulerActive) {
            Console.out(new MessageBuilder()
                    .addText(" ACTIVATED", BlockFormat.GREEN_TEXT())
                    .build());
        } else {
            Console.out(new MessageBuilder()
                    .addText(" DEACTIVATED", BlockFormat.RED_TEXT())
                    .build());
        }

        Console.out(new MessageBuilder()
                .addText("scheduler config:",
                        new BlockFormat.Builder()
                                .withWidth(18)
                                .build())
                .withNoLineFeed()
                .build());

        boolean consistent = Scheduler.hasUnmodifiedConfiguration();
        if (consistent) {
            Console.out(new MessageBuilder()
                    .addText("UP-TO-DATE", BlockFormat.GREEN_TEXT())
                    .build());
        } else {
            Console.out(new MessageBuilder()
                    .addText("CHANGED", BlockFormat.RED_TEXT())
                    .build());
        }

        Console.println();

        int widthEnabled = 8;
        int widthName = 20;
        int widthType = 20;
        int widthCronSchedule = 20;
        int widthNextExecutionTime = 20;

        JobConfigurations jobConfigurations = JobConfigurationsManager.fromConfigFiles();
        List<JobOverviewRecord> jobOverviewRecordList
                = JobOverview.getJobOverviewRecords(jobConfigurations);

        Console.out(new MessageBuilder()
                .addText("enabled",
                        new BlockFormat.Builder()
                                .withWidth(widthEnabled)
                                .build())
                .addText(" | ")
                .addText("job name",
                        new BlockFormat.Builder()
                                .withWidth(widthName)
                                .withAbbreviationSign("*")
                                .build())
                .addText(" | ")
                .addText("job type",
                        new BlockFormat.Builder()
                                .withWidth(widthType)
                                .withAbbreviationSign("*")
                                .build())
                .addText(" | ")
                .addText("next execution time",
                        new BlockFormat.Builder()
                                .withWidth(widthNextExecutionTime)
                                .withAbbreviationSign("*")
                                .build())
                .addText(" | ")
                .addText("cron schedule",
                        new BlockFormat.Builder()
                                .withWidth(widthCronSchedule)
                                .withAbbreviationSign(">")
                                .build())
                .build());

        Console.println(
                "-".repeat(widthEnabled)
                + "-+-"
                + "-".repeat(widthName)
                + "-+-"
                + "-".repeat(widthType)
                + "-+-"
                + "-".repeat(widthNextExecutionTime)
                + "-+-"
                + "-".repeat(widthCronSchedule)
        );


        for (JobOverviewRecord jobOverviewRecord : jobOverviewRecordList) {
            MessageBuilder messageBuilder = new MessageBuilder();

            boolean enabled = jobOverviewRecord.enabled();
            if (enabled) {
                messageBuilder.addText(
                        "ENABLED",
                        BlockFormat.GREEN_TEXT(),
                        new BlockFormat.Builder()
                                .withWidth(widthEnabled)
                                .build()
                );
            } else {
                messageBuilder.addText(
                        "DISABLED",
                        BlockFormat.RED_TEXT(),
                        new BlockFormat.Builder()
                                .withWidth(widthEnabled)
                                .build()
                );
            }

            messageBuilder.addText(" | ")
                    .addText(jobOverviewRecord.name(),
                            new BlockFormat.Builder()
                                    .withWidth(widthName)
                                    .withAbbreviationSign("*")
                                    .build())

                    .addText(" | ")
                    .addText(jobOverviewRecord.type(),
                            new BlockFormat.Builder()
                                    .withWidth(widthType)
                                    .withAbbreviationSign("*")
                                    .build())
                    .addText(" | ")
                    .addText(jobOverviewRecord.nextExecutionTime(),
                            new BlockFormat.Builder()
                                    .withWidth(widthNextExecutionTime)
                                    .withAbbreviationSign("*")
                                    .build())
                    .addText(" | ")
                    .addText(jobOverviewRecord.cronSchedule(),
                            new BlockFormat.Builder()
                                    .withWidth(widthCronSchedule)
                                    .withAbbreviationSign("*")
                                    .build());

            Console.out(messageBuilder.build());
        }
    }

}
