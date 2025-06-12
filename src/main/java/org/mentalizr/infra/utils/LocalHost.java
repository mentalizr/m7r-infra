package org.mentalizr.infra.utils;

import org.mentalizr.commons.constants.VmConst;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "UNKNOWN";
        }
    }

    public static boolean isDevVm() {
        return getHostname().equals(VmConst.VM_HOSTNAME);
    }

}
