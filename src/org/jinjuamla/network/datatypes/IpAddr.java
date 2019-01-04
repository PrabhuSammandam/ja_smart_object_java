package org.jinjuamla.network.datatypes;

import java.util.Arrays;

public class IpAddr implements Cloneable{

    public static int IPV4 = 0;
    public static int IPV6 = 1;
    public static int SCOPE_ADMIN = 4;
    public static int SCOPE_GLOBAL = 7;
    public static int SCOPE_INTERFACE = 1;
    public static int SCOPE_LINK = 2;
    public static int SCOPE_NONE = 0;
    public static int SCOPE_ORG = 6;
    public static int SCOPE_REALM = 3;
    public static int SCOPE_SITE = 5;
    int _addrFamily = IPV4;
    byte[] _address = null;
    int _scope = SCOPE_NONE;

    public IpAddr(int addrFamily) {
        this._addrFamily = addrFamily;
    }

    public IpAddr(byte[] address, int addrFamily) {
        this._addrFamily = addrFamily;
        this._address = address;
    }

    public IpAddr(int scope, short address) {
        setAddrByScope(scope, address);
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
        final IpAddr other = (IpAddr) obj;
        if (this._addrFamily != other._addrFamily) {
            return false;
        }
        return Arrays.equals(this._address, other._address);
    }

    public byte[] getAddr() {
        return _address;
    }

    public void setAddr(byte[] addr) {
        this._address = addr;
    }

    public int getAddrFamily() {
        return _addrFamily;
    }

    public void setAddrFamily(int addrFamily) {
        this._addrFamily = addrFamily;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    public boolean isIPV4() {
        return _addrFamily == IPV4;
    }

    public boolean isIPV6() {
        return _addrFamily == IPV6;
    }

    public boolean isMulticast() {
        if (_addrFamily == IPV4 && _address[0] >= 224 && _address[0] <= 239) {
            return true;
        }
        else
            if (_addrFamily == IPV6) {
                if (_address[0] == 0xFF) {
                    switch (_address[1]) {
                        case 0x01:
                        case 0x02:
                        case 0x03:
                        case 0x04:
                        case 0x05:
                        case 0x08:
                        case 0x0E: {
                            return (true);
                        }
                    }
                }
            }
        return false;
    }

    public final void setAddrByScope(int scope, short address) {
        Arrays.fill(_address, (byte) 0);

        _address[0] = (byte) 0xFF;
        _address[1] = (byte) (scope & 0xFF);
        _address[14] = (byte) (address >> 8);
        _address[15] = (byte) (address & 0xFF);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Ip Famliy :");

        if (_addrFamily == IPV4) {
            sb.append("IPV4");
        }
        else {
            sb.append("IPV6");
        }

        sb.append("\nIp Addr :");

        if (_address != null) {
            if (_addrFamily == IPV4) {
                sb.append(String.format("%d.%d.%d.%d", _address[0] & 0xFF, _address[1] & 0xFF, _address[2] & 0xFF, _address[3] & 0xFF));
            }
            else {
                sb.append(String.format("%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:", _address[0], _address[1], _address[2], _address[3],
                                        _address[4], _address[5], _address[6], _address[7],
                                        _address[8], _address[9], _address[10], _address[11],
                                        _address[12], _address[13], _address[14], _address[15]));
            }
        }

        return sb.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        IpAddr ipAddr = (IpAddr) super.clone();

        ipAddr._addrFamily = _addrFamily;

        if (_address != null) {
            ipAddr._address = Arrays.copyOf(_address, _address.length);
        }

        return ipAddr;
    }

}
