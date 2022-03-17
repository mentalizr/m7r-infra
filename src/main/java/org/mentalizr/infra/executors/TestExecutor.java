package org.mentalizr.infra.executors;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import org.mentalizr.commons.M7rDirs;
import org.mentalizr.infra.ApplicationContext;
import org.mentalizr.infra.InfraConfigFile;
import org.mentalizr.infra.buildEntities.ConnectionMongo;

public class TestExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {
        ApplicationContext.initialize(cliCall);

        System.out.println("test called.");

        InfraConfigFile infraConfigFile = new InfraConfigFile(new M7rDirs());
        String m7rInfraConfigFile = infraConfigFile.getInfraConfigFile().toAbsolutePath().toString();
        System.setProperty("m7r.config", m7rInfraConfigFile);

        try {
            ConnectionMongo.probe();
            System.out.println("Success");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }


//        Logger logger = LoggerFactory.getLogger(LoggerUtils.DOCKER_LOGGER);
//        logger.info("test!");
//
//        Logger anotherLogger = LoggerFactory.getLogger("some.other");
//        anotherLogger.info("some other log statement.");

    }


}
