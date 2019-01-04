/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.platform;

import org.jinjuamla.network.adapters.IAdapter;
import org.jinjuamla.network.adapters.ip.IpAdapter;
import org.jinjuamla.network.adapters.ip.networkinterfaces.IInterfaceMonitor;
import org.jinjuamla.network.adapters.ip.networkinterfaces.InterfaceMonitorImpl;
import org.jinjuamla.network.enums.consts.AdapterType;

/**
 *
 * @author psammand
 */
public class NetworkPlatformFactory implements INetworkPlatformFactory
{

    IpAdapter ipAdapter = new IpAdapter();
    InterfaceMonitorImpl interfaceMonitor = new InterfaceMonitorImpl();

    @Override
    public IAdapter getAdapter(int adapterType)
    {
        if (adapterType == AdapterType.IP)
        {
            return ipAdapter;
        }

        return null;
    }

    @Override
    public IInterfaceMonitor getInterfaceMonitor()
    {
        return interfaceMonitor;
    }

}
