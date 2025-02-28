package org.mentalizr.infra.executors;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.commons.paths.client.M7rClientDir;
import org.mentalizr.commons.paths.host.ContentDir;
import org.mentalizr.commons.paths.host.GitReposDir;
import org.mentalizr.commons.paths.host.hostDir.M7rHostDir;
import org.mentalizr.infra.Const;
import org.mentalizr.infra.buildEntities.connections.ConnectionMaria;
import org.mentalizr.infra.buildEntities.connections.ConnectionMongo;
import org.mentalizr.infra.buildEntities.connections.ConnectionTomcat;
import org.mentalizr.infra.buildEntities.ports.PortMaria;
import org.mentalizr.infra.buildEntities.ports.PortMongo;
import org.mentalizr.infra.buildEntities.ports.PortTomcat;
import org.mentalizr.infra.docker.m7r.*;
import org.mentalizr.infra.scheduler.Scheduler;
import org.mentalizr.infra.utils.LocalHost;
import org.mentalizr.scheduler.configuration.JobConfigurationsManager;
import org.mentalizr.scheduler.processManagement.IntentionFile;
import org.mentalizr.scheduler.processManagement.IntentionFile.Intention;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusExecutor implements CommandExecutor {

    private static final Logger logger = LoggerFactory.getLogger(StatusExecutor.class);

    private static final int minLengthString = 44;

    private static final String UP = Ansi.colorize("UP", Attribute.GREEN_TEXT());
    private static final String INTENTION_DOWN = Ansi.colorize("DOWN", Attribute.RED_TEXT());
    private static final String PRESENT = Ansi.colorize("PRESENT", Attribute.GREEN_TEXT());
    private static final String DOWN = Ansi.colorize("--", Attribute.RED_TEXT());
    private static final String RUNNING = Ansi.colorize("RUNNING", Attribute.GREEN_TEXT());
    private static final String STOPPED = Ansi.colorize("STOPPED", Attribute.RED_TEXT());
    private static final String OPEN = Ansi.colorize("OPEN", Attribute.GREEN_TEXT());
    private static final String CLOSED = Ansi.colorize("CLOSED", Attribute.RED_TEXT());
    private static final String SUCCESS = Ansi.colorize("SUCCESS", Attribute.GREEN_TEXT());
    private static final String SKIPPED = Ansi.colorize("SKIPPED", Attribute.YELLOW_TEXT());
    private static final String FAILED = Ansi.colorize("FAILED", Attribute.RED_TEXT());
    private static final String ACTIVATED = Ansi.colorize("ACTIVATED", Attribute.GREEN_TEXT());
    private static final String DEACTIVATED = Ansi.colorize("DEACTIVATED", Attribute.RED_TEXT());
    private static final String UP_TO_DATE = Ansi.colorize("UP-TO-DATE", Attribute.GREEN_TEXT());
    private static final String CHANGED = Ansi.colorize("CHANGED", Attribute.RED_TEXT());
    private static final String UNKNOWN = Ansi.colorize("UNKNOWN", Attribute.YELLOW_TEXT());

    @SuppressWarnings("StringConcatenationArgumentToLogCall")
    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {
        logger.info(StatusExecutor.class.getSimpleName() + " called...");

        String intentionString;
        if (IntentionFile.exists()) {
            Intention intention = IntentionFile.getIntention();
            if (intention == Intention.UP) {
                intentionString = UP;
            } else if (intention == Intention.DOWN) {
                intentionString = INTENTION_DOWN;
            } else {
                throw new IllegalStateException("Unknown intention: " + intention);
            }
        } else {
            intentionString = UNKNOWN;
        }

        System.out.println("mentalizr infrastructure status on "
                + Ansi.colorize(LocalHost.getHostname(), Attribute.WHITE_TEXT(), Attribute.BOLD())
                + " with intention " + intentionString);

        boolean showConfiguration
                = cliCall.getOptionParserResultSpecific().hasOption(StatusDef.SPECIFIC_OPTION__CONFIGURATION);
        if (showConfiguration) {
            System.out.println(Strings.rightPad("m7r-host dir:", minLengthString)
                    + "[" + new M7rHostDir().toAbsolutePathString() + "].");
            System.out.println(Strings.rightPad("m7r client dir:", minLengthString)
                    + "[" + M7rClientDir.createInstance().toAbsolutePathString() + "].");
            System.out.println(Strings.rightPad("m7r repos dir:", minLengthString)
                    + "[" + GitReposDir.createInstance().toAbsolutePathString() + "].");
            System.out.println(Strings.rightPad("m7r content dir:", minLengthString)
                    + "[" + ContentDir.createInstance().toAbsolutePathString() + "].");
        }

        String networkString = Strings.fillUpRight("Network [" + Const.NETWORK + "]: ", ' ', minLengthString);
        if (M7rNetwork.exists()) {
            System.out.println(networkString + UP);
        } else {
            System.out.println(networkString + DOWN);
        }

        outputImageStatus("MongoDB", Const.IMAGE_MONGO);

        String volumeMongoString = Strings.fillUpRight("MongoDB volume [" + Const.VOLUME_MONGO + "]: ", ' ', minLengthString);
        if (M7rVolumeMongo.exists()) {
            System.out.println(volumeMongoString + UP);
        } else {
            System.out.println(volumeMongoString + DOWN);
        }

        String containerMongoString = Strings.fillUpRight("MongoDB container [" + Const.CONTAINER_MONGO + "]: ", ' ', minLengthString);
        if (M7rContainerMongo.exists()) {
            if (M7rContainerMongo.isRunning()) {
                System.out.println(containerMongoString + UP + " " + RUNNING);
            } else {
                System.out.println(containerMongoString + UP + " " + STOPPED);
            }
        } else {
            System.out.println(containerMongoString + DOWN);
        }

        String portMongoString = Strings.fillUpRight("MongoDB port 27017: ", ' ', minLengthString);
        boolean mongoPortIsListening = PortMongo.isListening();
        if (mongoPortIsListening) {
            System.out.println(portMongoString + OPEN);
        } else {
            System.out.println(portMongoString + CLOSED);
        }

        String connectionMongoString = Strings.fillUpRight("MongoDB probe client connection: ", ' ', minLengthString);
        System.out.print(connectionMongoString);
        if (mongoPortIsListening) {
            if (ConnectionMongo.connectionSuccessful()) {
                System.out.println(SUCCESS);
            } else {
                System.out.println(FAILED);
            }
        } else {
            System.out.println(SKIPPED);
        }

        outputImageStatus("MariaDB", Const.IMAGE_MARIA);

        String volumeMariaString = Strings.fillUpRight("MariaDB volume [" + Const.VOLUME_MARIA + "]: ", ' ', minLengthString);
        if (M7rVolumeMaria.exists()) {
            System.out.println(volumeMariaString + UP);
        } else {
            System.out.println(volumeMariaString + DOWN);
        }

        String containerMariaString = Strings.fillUpRight("MariaDB container [" + Const.CONTAINER_MARIA + "]: ", ' ', minLengthString);
        if (M7rContainerMaria.exists()) {
            if (M7rContainerMaria.isRunning()) {
                System.out.println(containerMariaString + UP + " " + RUNNING);
            } else {
                System.out.println(containerMariaString + UP + " " + STOPPED);
            }
        } else {
            System.out.println(containerMariaString + DOWN);
        }

        String portMariaString = Strings.fillUpRight("MariaDB port 3306: ", ' ', minLengthString);
        boolean mariaPortIsListening = PortMaria.isListening();
        if (mariaPortIsListening) {
            System.out.println(portMariaString + OPEN);
        } else {
            System.out.println(portMariaString + CLOSED);
        }

        String connectionMariaString = Strings.fillUpRight("MariaDB probe client connection: ", ' ', minLengthString);
        System.out.print(connectionMariaString);
        if (mariaPortIsListening) {
            if (ConnectionMaria.probe()) {
                System.out.println(SUCCESS);
            } else {
                System.out.println(FAILED);
            }
        } else {
            System.out.println(SKIPPED);
        }

        outputImageStatus("Tomcat", Const.IMAGE_TOMCAT);

        String volumeTomcatString = Strings.fillUpRight("Tomcat volume [" + Const.VOLUME_TOMCAT + "]: ", ' ', minLengthString);
        if (M7rVolumeTomcat.exists()) {
            System.out.println(volumeTomcatString + UP);
        } else {
            System.out.println(volumeTomcatString + DOWN);
        }

        String containerTomcatString = Strings.fillUpRight("Tomcat container [" + Const.CONTAINER_TOMCAT + "]: ", ' ', minLengthString);
        if (M7rContainerTomcat.exists()) {
            if (M7rContainerTomcat.isRunning()) {
                System.out.println(containerTomcatString + UP + " " + RUNNING);
            } else {
                System.out.println(containerTomcatString + UP + " " + STOPPED);
            }
        } else {
            System.out.println(containerTomcatString + DOWN);
        }

        String portTomcatString = Strings.fillUpRight("Tomcat port 8080: ", ' ', minLengthString);
        boolean tomcatPortIsListening = PortTomcat.isListening();
        if (tomcatPortIsListening) {
            System.out.println(portTomcatString + OPEN);
        } else {
            System.out.println(portTomcatString + CLOSED);
        }

        String connectionTomcatGenericString = Strings.fillUpRight("Tomcat probe http connector: ", ' ', minLengthString);
        System.out.print(connectionTomcatGenericString);
        if (tomcatPortIsListening) {
            if (ConnectionTomcat.probeGeneric()) {
                System.out.println(SUCCESS);
            } else {
                System.out.println(FAILED);
            }
        } else {
            System.out.println(SKIPPED);
        }

        String connectionTomcatString = Strings.fillUpRight("Tomcat probe M7R service: ", ' ', minLengthString);
        System.out.print(connectionTomcatString);
        if (tomcatPortIsListening) {
            if (ConnectionTomcat.probe()) {
                System.out.println(SUCCESS);
            } else {
                System.out.println(FAILED);
            }
        } else {
            System.out.println(SKIPPED);
        }

        outputImageStatus("nginx", Const.IMAGE_NGINX);

        String containerNginxString = Strings.fillUpRight("nginx container [" + Const.CONTAINER_NGINX + "]: ", ' ', minLengthString);
        if (M7rContainerNginx.exists()) {
            if (M7rContainerNginx.isRunning()) {
                System.out.println(containerNginxString + UP + " " + RUNNING);
            } else {
                System.out.println(containerNginxString + UP + " " + STOPPED);
            }
        } else {
            System.out.println(containerNginxString + DOWN);
        }

        String portNginxString = Strings.fillUpRight("nginx port 443: ", ' ', minLengthString);
        boolean nginxPortIsListening = PortTomcat.isListening();
        if (nginxPortIsListening) {
            System.out.println(portNginxString + OPEN);
        } else {
            System.out.println(portNginxString + CLOSED);
        }

        String schedulerString = Strings.fillUpRight("scheduler: ", ' ', minLengthString);
        boolean schedulerRunning = Scheduler.isRunning();
        boolean schedulerActive = Scheduler.isActive();

        String deamonOutString = schedulerString;
        if (schedulerRunning) {
            deamonOutString += RUNNING;
        } else {
            deamonOutString += STOPPED;
        }
        if (schedulerActive) {
            deamonOutString += " " + ACTIVATED;
        } else {
            deamonOutString += " " + DEACTIVATED;
        }
        System.out.println(deamonOutString);

        String schedulerConfigConsistencyString = Strings.fillUpRight("scheduler config: ", ' ', minLengthString);
        boolean consistent = Scheduler.hasUnmodifiedConfiguration();
        if (consistent) {
            schedulerConfigConsistencyString += UP_TO_DATE;
        } else {
            schedulerConfigConsistencyString += CHANGED;
        }
        System.out.println(schedulerConfigConsistencyString);
    }

    private static void outputImageStatus(String serverName, String taggedImageName) {
        String imageString = Strings.fillUpRight(serverName + " image [" + taggedImageName + "]: ", ' ', minLengthString);
        if (M7rImage.exists(taggedImageName)) {
            System.out.println(imageString + PRESENT);
        } else {
            System.out.println(imageString + DOWN);
        }
    }

}
