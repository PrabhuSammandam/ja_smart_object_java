/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network;

import org.jinjuamla.network.datatypes.IpAdapterConfig;

/**
 *
 * @author psammand
 */
public class IpAdapterConfigMgr {

    private static IpAdapterConfigMgr instance = null;

    public static IpAdapterConfigMgr Inst() {
        if (instance == null) {
            instance = new IpAdapterConfigMgr();
        }

        return instance;
    }

    IpAdapterConfig ipAdapterConfig = new IpAdapterConfig();

    public IpAdapterConfig getIpAdapterConfig()
    {
        return ipAdapterConfig;
    }

    public void setIpAdapterConfig(IpAdapterConfig ipAdapterConfig)
    {
        this.ipAdapterConfig = ipAdapterConfig;
    }
}
