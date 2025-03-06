package org.mentalizr.infra;

import de.arthurpicht.cli.*;
import de.arthurpicht.cli.command.Commands;
import de.arthurpicht.cli.command.InfoDefaultCommand;
import de.arthurpicht.cli.common.UnrecognizedArgumentException;
import de.arthurpicht.cli.option.ManOption;
import de.arthurpicht.cli.option.OptionBuilder;
import de.arthurpicht.cli.option.Options;
import de.arthurpicht.cli.option.VersionOption;
import de.arthurpicht.console.Console;
import de.arthurpicht.console.config.ConsoleConfigurationBuilder;
import de.arthurpicht.console.message.Level;
import org.mentalizr.cli.helper.ConsoleWriter;
import org.mentalizr.infra.appInit.ApplicationContext;
import org.mentalizr.infra.appInit.InfraApplicationInitialization;
import org.mentalizr.infra.appInit.InfraApplicationInitializationException;
import org.mentalizr.infra.executors.*;
import org.mentalizr.scheduler.SchedulerRuntimeException;

public class InfraCli {

    private static Cli createCli() {

        Commands commands = new Commands();
        commands.setDefaultCommand(new InfoDefaultCommand());
        commands.add(StatusDef.get());
        commands.add(CreateDef.get());
        commands.add(TestDef.get());
        commands.add(RemoveDef.get());
        commands.add(StartDef.get());
        commands.add(StopDef.get());
        commands.add(RestartDef.get());
        commands.add(PullUpDef.get());
        commands.add(FullPullDef.get());
        commands.add(TearDownDef.get());
        commands.add(DeployDef.get());
        commands.add(CleanDef.get());
        commands.add(RecoverDef.get());
        commands.add(BackupDef.get());
        commands.add(PullImagesDef.get());
        commands.add(CreateImagesDef.get());
        commands.add(RemoveImagesDef.get());
        commands.add(CleanImagesDef.get());
        commands.add(ShellMongoDef.get());
        commands.add(ShellMariaDef.get());
        commands.add(ShellTomcatDef.get());
        commands.add(ShellSqlDef.get());
        commands.add(LogsDef.get());
        commands.add(SchedulerStartDef.get());
        commands.add(SchedulerStopDef.get());
        commands.add(SchedulerActivateDef.get());
        commands.add(SchedulerDeactivateDef.get());
        commands.add(SchedulerRestartDef.get());
        commands.add(SchedulerShowDef.get());

        Options globalOptions = new Options()
                .add(new VersionOption())
                .add(new ManOption())
                .add(new OptionBuilder()
                        .withLongName("verbose")
                        .withDescription("verbose output")
                        .build(GlobalOptions.GLOBAL_OPTION__VERBOSE))
                .add(new OptionBuilder()
                        .withShortName('s')
                        .withLongName("stacktrace")
                        .withDescription("Show stacktrace when running on error.")
                        .build(GlobalOptions.GLOBAL_OPTION__STACKTRACE))
                .add(new OptionBuilder()
                        .withLongName("silent")
                        .withDescription("Make no output to console.")
                        .build(GlobalOptions.GLOBAL_OPTION__SILENT))
                .add(new OptionBuilder()
                        .withShortName('t')
                        .withLongName("timeout")
                        .withArgumentName("timeout")
                        .withDescription("Override default timeout parameters (seconds).")
                        .build(GlobalOptions.GLOBAL_OPTION__TIMEOUT))
                .add(new OptionBuilder()
                        .withShortName('n')
                        .withLongName("notify")
                        .withDescription("Send email notification to admins on command execution.")
                        .build(GlobalOptions.GLOBAL_OPTION__NOTIFY))
                .add(new OptionBuilder()
                        .withLongName("no-color")
                        .withDescription("no colors on console output.")
                        .build(GlobalOptions.GLOBAL_OPTION__NO_COLOR));

        CliDescription cliDescription = new CliDescriptionBuilder()
                .withDescription("mentalizr infra structure manager CLI\nhttps://github.com/mentalizr/m7r-infra")
                .withVersionByTag("0.0.1-SNAPSHOT", "2025-02-11")
                .build("m7r-infra");

        return new CliBuilder()
                .withGlobalOptions(globalOptions)
                .withCommands(commands)
                .withAutoHelp()
                .build(cliDescription);
    }

    public static void main(String[] args) {

        Cli cli = createCli();
        CliCall cliCall = null;
        try {
            cliCall = cli.parse(args);
        } catch (UnrecognizedArgumentException e) {
            System.out.println(e.getExecutableName() + " call syntax error. " + e.getMessage());
            System.out.println(e.getCallString());
            System.out.println(e.getCallPointerString());
            System.exit(1);
        }

        GlobalOptions globalOptions = new GlobalOptions(cliCall);

        Console.configure(new ConsoleConfigurationBuilder()
                .withMutedOutput(globalOptions.isSilent())
                .withLevel(globalOptions.isVerbose() ? Level.VERBOSE : Level.NORMAL)
                .withSuppressedColors(globalOptions.hasNoColor())
                .build()
        );

        try {
            InfraApplicationInitialization.asCli(new GlobalOptions(cliCall));
        } catch (InfraApplicationInitializationException | SchedulerRuntimeException e) {
            ConsoleWriter.error(e.getMessage());
            if (globalOptions.showStacktrace()) {
                Console.printStackTrace(e);
            }
            System.exit(1);
        }

        try {
            cli.execute(cliCall);
        } catch (CommandExecutorException e) {
            ConsoleWriter.error("m7r-infra execution failed.");
            if (e.getMessage() != null) Console.println(e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            ConsoleWriter.error("RuntimeException: " + e.getMessage());
            if (ApplicationContext.showStacktrace()) Console.printStackTrace(e);
            System.exit(1);
        }
    }

}
