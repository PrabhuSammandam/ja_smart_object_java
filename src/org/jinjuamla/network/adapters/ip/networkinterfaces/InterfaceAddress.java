/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.adapters.ip.networkinterfaces;

import org.jinjuamla.network.datatypes.IpAddr;

/**
 *
 * @author psammand
 */
public class InterfaceAddress {
    String address;
    int flags;
    int index;
    int ipAddrFamily;
    String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIpAddrFamily() {
        return ipAddrFamily;
    }

    public void setIpAddrFamily(int ipAddrFamily) {
        this.ipAddrFamily = ipAddrFamily;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIPV4() {
        return ipAddrFamily == IpAddr.IPV4;
    }

    public boolean isIPV6() {
        return ipAddrFamily == IpAddr.IPV6;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("Name :").append(name).append("\n").append("Index :").append(index);
        strBuilder.append("\n").append("Address :").append(address);

        return strBuilder.toString();
    }

}
