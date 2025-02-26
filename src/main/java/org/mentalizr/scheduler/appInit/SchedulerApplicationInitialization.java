//package org.mentalizr.scheduler.appInit;
//
//import ch.qos.logback.classic.Level;
//import de.arthurpicht.utils.logging.LogFile;
//import de.arthurpicht.utils.logging.LogbackInit;
//import org.mentalizr.commons.paths.host.hostDir.M7rHostLogDir;
//import org.mentalizr.commons.paths.host.hostDir.M7rSchedulerConfigDir;
//import org.mentalizr.infra.appInit.InfraApplicationInitialization;
//import org.mentalizr.infra.appInit.InfraApplicationInitializationException;
//import org.mentalizr.scheduler.configuration.SchedulerConfig;
//import org.mentalizr.scheduler.configuration.SchedulerConfigLoader;
//
//import java.io.IOException;
//
//public class SchedulerApplicationInitialization {
//
//    public static void execute() {
////        createLogDir();
////        initLogging();
////        createDaemonConfigDir();
//        try {
//            InfraApplicationInitialization.executeWithDefaults();
//        } catch (InfraApplicationInitializationException e) {
//            throw new SchedulerApplicationInitializationException(e.getMessage(), e);
//        }
////        ApplicationContext.initializeWithDefaults();
//    }
//
//    private static SchedulerConfig loadSchedulerConfig() {
//        return SchedulerConfigLoader.load();
//    }
//
////    private static void createLogDir() throws SchedulerApplicationInitializationException {
////        M7rHostLogDir m7rHostLogDir = new M7rHostLogDir();
////        if (!m7rHostLogDir.exists()) {
////            try {
////                m7rHostLogDir.create();
////            } catch (IOException e) {
////                throw new SchedulerApplicationInitializationException(
////                        "Application initialization failed. Could not create directory ["
////                                + m7rHostLogDir.toAbsolutePathString() + "]");
////            }
////        }
////    }
//
//    private static void initLogging() {
//        new LogbackInit()
//                .addLogFile(new LogFile.Builder()
//                        .withPath(new M7rHostLogDir().asPath().resolve("m7r-scheduler.log"))
//                        .withLevel(Level.DEBUG)
//                        .build())
//                .addLoggerLevel("org.mongo", Level.INFO)
//                .addLoggerLevel("org.quartz", Level.INFO)
//                .initialize();
//    }
//
////    private static void createDaemonConfigDir() {
////        M7rSchedulerConfigDir m7rSchedulerConfigDir = new M7rSchedulerConfigDir();
////        if (!m7rSchedulerConfigDir.exists()) {
////            try {
////                m7rSchedulerConfigDir.create();
////            } catch (IOException e) {
////                throw new SchedulerApplicationInitializationException(
////                        "Application initialization failed. Could not create directory ["
////                                + m7rSchedulerConfigDir.toAbsolutePathString() + "]");
////            }
////        }
////    }
//
//}
