/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.platform;

import org.jinjuamla.network.adapters.IAdapter;	
import org.jinjuamla.network.adapters.ip.networkinterfaces.IInterfaceMonitor;

/**
 *
 * @author psammand
 */
public interface INetworkPlatformFactory
{

    public IAdapter getAdapter(int adapterType);

    public IInterfaceMonitor getInterfaceMonitor();
}
