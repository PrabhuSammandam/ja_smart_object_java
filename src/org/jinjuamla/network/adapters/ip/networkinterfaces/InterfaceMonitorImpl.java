/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.adapters.ip.networkinterfaces;

import org.jinjuamla.network.datatypes.IpAddr;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jinjuamla.network.enums.ErrCode;

/**
 *
 * @author psammand
 */
public class InterfaceMonitorImpl implements IInterfaceMonitor {

    LinkedList<InterfaceAddress> currentInterfaceAddressList = new LinkedList<>();
    LinkedList<IInterfaceEventHandler> interfaceEventHandlerList = new LinkedList<>();

    @Override
    public ErrCode addInterfaceEventHandler(IInterfaceEventHandler interfaceEventHandler) {
        if (interfaceEventHandler != null) {
            interfaceEventHandlerList.add(interfaceEventHandler);
        }
        return ErrCode.OK;
    }

    @Override
    public ErrCode getInterfacesList(LinkedList<InterfaceAddress> interfaceAddrList) {
        currentInterfaceAddressList.forEach((interfaceAddress) -> {
            interfaceAddrList.add(interfaceAddress);
        });
        return ErrCode.OK;
    }

    @Override
    public ErrCode getNewlyFoundInterfacesList(LinkedList<InterfaceAddress> interfaceAddrList) {
        return ErrCode.OK;
    }

    @Override
    public ErrCode removeInterfaceEventHandler(IInterfaceEventHandler interfaceEventHandler) {
        if (interfaceEventHandler != null) {
            interfaceEventHandlerList.remove(interfaceEventHandler);
        }
        return ErrCode.OK;
    }

    @Override
    public ErrCode startMonitor(int adapterType) {
        interfaceEventHandlerList.clear();
        currentInterfaceAddressList.clear();

        try {
            Enumeration<NetworkInterface> networkInterfacesList = NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface networkInterface : Collections.list(networkInterfacesList)) {
                if (networkInterface.isLoopback() || networkInterface.isUp() == false) {
                    continue;
                }

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (inetAddress instanceof Inet4Address) {
                        Inet4Address ipv4Addr = (Inet4Address) inetAddress;

                        if (!ipv4Addr.isAnyLocalAddress() && !ipv4Addr.isLoopbackAddress()) {
                            InterfaceAddress ifAddr = new InterfaceAddress();

                            ifAddr.setName(networkInterface.getName());
                            ifAddr.setIndex(networkInterface.getIndex());
                            ifAddr.setIpAddrFamily(IpAddr.IPV4);
                            ifAddr.setAddress(ipv4Addr.getHostAddress());

                            currentInterfaceAddressList.add(ifAddr);

                            continue;
                        }
                    }
                    if (inetAddress instanceof Inet6Address) {
                        Inet6Address ipv6Addr = (Inet6Address) inetAddress;

                        if (!ipv6Addr.isAnyLocalAddress() && !ipv6Addr.isLoopbackAddress() && ipv6Addr.isLinkLocalAddress()) {
                            InterfaceAddress ifAddr = new InterfaceAddress();

                            ifAddr.setName(networkInterface.getName());
                            ifAddr.setIndex(networkInterface.getIndex());
                            ifAddr.setIpAddrFamily(IpAddr.IPV6);
                            String hostAddress = ipv6Addr.getHostAddress();
                            ifAddr.setAddress(hostAddress.substring(0, hostAddress.indexOf('%')));

                            currentInterfaceAddressList.add(ifAddr);

                            continue;
                        }
                    }
                }
            }

        }
        catch (SocketException ex) {
            Logger.getLogger(InterfaceMonitorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ErrCode.OK;
    }

    @Override
    public ErrCode stopMonitor(int adapterType) {
        return ErrCode.OK;
    }

}
