package org.mentalizr.infra.externalApi;

import org.mentalizr.infra.buildEntities.connections.ConnectionMaria;
import org.mentalizr.infra.buildEntities.connections.ConnectionMongo;
import org.mentalizr.infra.buildEntities.connections.ConnectionTomcat;
import org.mentalizr.infra.buildEntities.ports.PortTomcat;
import org.mentalizr.infra.docker.m7r.M7rNetwork;
import org.mentalizr.infra.utils.InfraLogicHelper;

public record StatusSummary(
        boolean networkUp,
        boolean mongoClientConnection,
        boolean mariaDBClientConnection,
        boolean tomcatM7rService,
        boolean nginxPortOpen) {

    public boolean isRunning() {
        return InfraLogicHelper.allTrue(
                networkUp,
                mongoClientConnection,
                mariaDBClientConnection,
                tomcatM7rService,
                nginxPortOpen);
    }

    public static StatusSummary create() {
        boolean networkUp = M7rNetwork.exists();
        boolean mongoClientConnection = ConnectionMongo.connectionSuccessful();
        boolean mariaDBClientConnection = ConnectionMaria.probe();
        boolean tomcatM7rService = ConnectionTomcat.probe();
        boolean nginxPortOpen = PortTomcat.isListening();

        return new StatusSummary(
                networkUp,
                mongoClientConnection,
                mariaDBClientConnection,
                tomcatM7rService,
                nginxPortOpen);
    }

}
