package org.jinjuamla.network.datatypes;

import org.jinjuamla.network.enums.consts.AdapterType;
import org.jinjuamla.network.enums.consts.NetworkFlag;
import java.util.Objects;

public class Endpoint implements Cloneable{

    short adapterType = AdapterType.DEFAULT;
    int interfaceIndex = 0;
    IpAddr ipAddr;
    int networkFlag = NetworkFlag.NONE;
    int port = 0;

    public Endpoint() {
    }

    public Endpoint(short adapterType, short networkFlag, int port, int interfaceIndex, IpAddr ipAddr) {
        this.adapterType = adapterType;
        this.networkFlag = networkFlag;
        this.port = port;
        this.interfaceIndex = interfaceIndex;
        this.ipAddr = ipAddr;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Endpoint newEndpoint = (Endpoint) super.clone();

        newEndpoint.adapterType = adapterType;
        newEndpoint.networkFlag = networkFlag;
        newEndpoint.port = port;
        newEndpoint.interfaceIndex = interfaceIndex;
        newEndpoint.ipAddr = (IpAddr) ipAddr.clone();

        return newEndpoint;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Endpoint other = (Endpoint) obj;
        if (this.port != other.port) {
            return false;
        }
        return Objects.equals(this.ipAddr, other.ipAddr);
    }

    public short getAdapterType() {
        return adapterType;
    }

    public void setAdapterType(short adapterType) {
        this.adapterType = adapterType;
    }

    public int getInterfaceIndex() {
        return interfaceIndex;
    }

    public void setInterfaceIndex(int interfaceIndex) {
        this.interfaceIndex = interfaceIndex;
    }

    public IpAddr getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(IpAddr ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getNetworkFlag() {
        return networkFlag;
    }

    public void setNetworkFlag(int networkFlag) {
        this.networkFlag = networkFlag;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public boolean isMulticast() {
        return (networkFlag & NetworkFlag.MULTICAST) != 0;
    }

    @Override
    public String toString() {
        return "";
    }

}
