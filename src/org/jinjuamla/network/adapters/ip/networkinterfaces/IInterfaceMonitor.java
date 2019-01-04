/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.adapters.ip.networkinterfaces;

import java.util.LinkedList;
import org.jinjuamla.network.enums.ErrCode;

/**
 *
 * @author psammand
 */
public interface IInterfaceMonitor {

    public ErrCode startMonitor(int adapterType);

    public ErrCode stopMonitor(int adapterType);

    public ErrCode getInterfacesList(LinkedList<InterfaceAddress> interfaceAddrList);

    public ErrCode getNewlyFoundInterfacesList(LinkedList<InterfaceAddress> interfaceAddrList);

    public ErrCode addInterfaceEventHandler(IInterfaceEventHandler interfaceEventHandler);

    public ErrCode removeInterfaceEventHandler(IInterfaceEventHandler interfaceEventHandler);

}
