package org.mentalizr.infra.buildEntities;

import org.mentalizr.infra.utils.IOUtils;

public class PortTomcat {

    public static boolean isListening() {
        return IOUtils.isPortListening(8080);
    }

}