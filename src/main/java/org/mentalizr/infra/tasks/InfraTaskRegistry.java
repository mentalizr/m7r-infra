package org.mentalizr.infra.tasks;

import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistryBuilder;
import org.mentalizr.infra.tasks.backup.Backup;
import org.mentalizr.infra.tasks.cleanImages.*;
import org.mentalizr.infra.tasks.create.*;
import org.mentalizr.infra.tasks.create.maria.CreateContainerMaria;
import org.mentalizr.infra.tasks.create.maria.CreateMaria;
import org.mentalizr.infra.tasks.create.maria.CreateVolumeMaria;
import org.mentalizr.infra.tasks.create.maria.InitializeContainerMaria;
import org.mentalizr.infra.tasks.create.mongo.CreateContainerMongo;
import org.mentalizr.infra.tasks.create.mongo.CreateMongo;
import org.mentalizr.infra.tasks.create.mongo.CreateVolumeMongo;
import org.mentalizr.infra.tasks.create.mongo.InitializeContainerMongo;
import org.mentalizr.infra.tasks.create.nginx.CreateContainerNginx;
import org.mentalizr.infra.tasks.create.nginx.CreateNginx;
import org.mentalizr.infra.tasks.create.nginx.InitializeContainerNginx;
import org.mentalizr.infra.tasks.create.tomcat.CreateContainerTomcat;
import org.mentalizr.infra.tasks.create.tomcat.CreateTomcat;
import org.mentalizr.infra.tasks.create.tomcat.CreateVolumeTomcat;
import org.mentalizr.infra.tasks.create.tomcat.InitializeContainerTomcat;
import org.mentalizr.infra.tasks.createImages.*;
import org.mentalizr.infra.tasks.scheduler.*;
import org.mentalizr.infra.tasks.deploy.*;
import org.mentalizr.infra.tasks.pullImages.*;
import org.mentalizr.infra.tasks.recover.RecoverDev;
import org.mentalizr.infra.tasks.recover.RecoverLatest;
import org.mentalizr.infra.tasks.remove.*;
import org.mentalizr.infra.tasks.remove.maria.RemoveContainerMaria;
import org.mentalizr.infra.tasks.remove.maria.RemoveMaria;
import org.mentalizr.infra.tasks.remove.maria.RemoveVolumeMaria;
import org.mentalizr.infra.tasks.remove.mongo.RemoveContainerMongo;
import org.mentalizr.infra.tasks.remove.mongo.RemoveMongo;
import org.mentalizr.infra.tasks.remove.mongo.RemoveVolumeMongo;
import org.mentalizr.infra.tasks.remove.nginx.RemoveContainerNginx;
import org.mentalizr.infra.tasks.remove.nginx.RemoveNginx;
import org.mentalizr.infra.tasks.remove.tomcat.RemoveContainerTomcat;
import org.mentalizr.infra.tasks.remove.tomcat.RemoveTomcat;
import org.mentalizr.infra.tasks.remove.tomcat.RemoveVolumeTomcat;
import org.mentalizr.infra.tasks.removeImages.*;
import org.mentalizr.infra.tasks.start.*;
import org.mentalizr.infra.tasks.start.maria.AwaitUpMaria;
import org.mentalizr.infra.tasks.start.maria.StartContainerMaria;
import org.mentalizr.infra.tasks.start.maria.StartMaria;
import org.mentalizr.infra.tasks.start.mongo.AwaitUpMongo;
import org.mentalizr.infra.tasks.start.mongo.StartContainerMongo;
import org.mentalizr.infra.tasks.start.mongo.StartMongo;
import org.mentalizr.infra.tasks.start.nginx.AwaitUpNginx;
import org.mentalizr.infra.tasks.start.nginx.StartContainerNginx;
import org.mentalizr.infra.tasks.start.nginx.StartNginx;
import org.mentalizr.infra.tasks.start.tomcat.AwaitUpTomcat;
import org.mentalizr.infra.tasks.start.tomcat.StartContainerTomcat;
import org.mentalizr.infra.tasks.start.tomcat.StartTomcat;
import org.mentalizr.infra.tasks.stop.*;

public class InfraTaskRegistry {

    public static TaskRegistry create() {
        TaskRegistryBuilder taskRegistryBuilder = new TaskRegistryBuilder();

        taskRegistryBuilder.withTask(CreateTarget.create());

        taskRegistryBuilder
                .withTask(CreateNginx.create())
                .withTask(InitializeContainerNginx.create())
                .withTask(CreateContainerNginx.create());

        taskRegistryBuilder
                .withTask(CreateTomcat.create())
                .withTask(InitializeContainerTomcat.create())
                .withTask(CreateContainerTomcat.create())
                .withTask(CreateVolumeTomcat.create());

        taskRegistryBuilder
                .withTask(CreateMaria.create())
                .withTask(InitializeContainerMaria.create())
                .withTask(CreateContainerMaria.create())
                .withTask(CreateVolumeMaria.create());

        taskRegistryBuilder.withTask(CreateMongo.create())
                .withTask(InitializeContainerMongo.create())
                .withTask(CreateContainerMongo.create())
                .withTask(CreateVolumeMongo.create());

        taskRegistryBuilder.withTask(CreateNetwork.create());
        taskRegistryBuilder.withTask(CreateDirs.create());

        taskRegistryBuilder.withTask(RemoveTarget.create());
        taskRegistryBuilder.withTask(RemoveMongo.create());
        taskRegistryBuilder.withTask(RemoveVolumeMongo.create());
        taskRegistryBuilder.withTask(RemoveContainerMongo.create());
        taskRegistryBuilder.withTask(RemoveMaria.create());
        taskRegistryBuilder.withTask(RemoveVolumeMaria.create());
        taskRegistryBuilder.withTask(RemoveContainerMaria.create());
        taskRegistryBuilder.withTask(RemoveTomcat.create());
        taskRegistryBuilder.withTask(RemoveVolumeTomcat.create());
        taskRegistryBuilder.withTask(RemoveContainerTomcat.create());
        taskRegistryBuilder.withTask(RemoveNginx.create());
        taskRegistryBuilder.withTask(RemoveContainerNginx.create());

        taskRegistryBuilder.withTask(RemoveNetwork.create());

        taskRegistryBuilder
                .withTask(IntentionUp.create())
                .withTask(StartTarget.create())
                .withTask(StartNginx.create())
                .withTask(AwaitUpNginx.create())
                .withTask(StartContainerNginx.create())
                .withTask(StartTomcat.create())
                .withTask(AwaitUpTomcat.create())
                .withTask(StartContainerTomcat.create())
                .withTask(StartMaria.create())
                .withTask(AwaitUpMaria.create())
                .withTask(StartContainerMaria.create())
                .withTask(StartMongo.create())
                .withTask(AwaitUpMongo.create())
                .withTask(StartContainerMongo.create());

        taskRegistryBuilder
                .withTask(IntentionDown.create())
                .withTask(StopTarget.create())
                .withTask(StopMongo.create())
                .withTask(StopMaria.create())
                .withTask(StopTomcat.create())
                .withTask(StopNginx.create());

        taskRegistryBuilder
                .withTask(Deploy.create())
                .withTask(UpdateHtml.create())
                .withTask(DeployResrc.create())
                .withTask(DeployWar.create())
                .withTask(InitM7rAdmin.create())
                .withTask(InitDbSchema.create());

        taskRegistryBuilder
                .withTask(RecoverLatest.create())
                .withTask(RecoverDev.create());

        taskRegistryBuilder
                .withTask(Backup.create());

        taskRegistryBuilder
                .withTask(PullImages.create())
                .withTask(PullImageMongo.create())
                .withTask(PullImageMaria.create())
                .withTask(PullImageTomcat.create())
                .withTask(PullImageNginx.create());

        taskRegistryBuilder
                .withTask(CreateImages.create())
                .withTask(CreateImageDebian.create())
                .withTask(CreateImageJDK.create())
                .withTask(CreateImageTomcat.create())
                .withTask(CreateImageNginx.create());

        taskRegistryBuilder
                .withTask(CreateBackups.create())
                .withTask(RemoveImages.create())
                .withTask(CleanImages.create())
                .withTask(CreateBackupTagMongo.create())
                .withTask(RemoveImageMongo.create())
                .withTask(CleanImagesMongo.create())
                .withTask(CreateBackupTagMaria.create())
                .withTask(RemoveImageMaria.create())
                .withTask(CleanImagesMaria.create())
                .withTask(CreateBackupTagDebian.create())
                .withTask(RemoveImageDebian.create())
                .withTask(CleanImagesDebian.create())
                .withTask(RemoveBaseImageDebian.create())
                .withTask(CreateBackupTagJDK.create())
                .withTask(RemoveImageJDK.create())
                .withTask(CleanImagesJDK.create())
                .withTask(CreateBackupTagTomcat.create())
                .withTask(RemoveImageTomcat.create())
                .withTask(CleanImagesTomcat.create())
                .withTask(CreateBackupTagNginx.create())
                .withTask(RemoveImageNginx.create())
                .withTask(CleanImagesNginx.create())
                .withTask(RemoveBaseImageNginx.create());

        taskRegistryBuilder
                .withTask(StartScheduler.create())
                .withTask(StopScheduler.create())
                .withTask(RestartScheduler.create())
                .withTask(ActivateScheduler.create())
                .withTask(DeactivateScheduler.create());

        return taskRegistryBuilder.build();
    }

}
