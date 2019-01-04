/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.adapters;

import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.network.enums.ErrCode;
import org.jinjuamla.network.enums.consts.AdapterType;

/**
 *
 * @author psammand
 */
public class AdapterEvent {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_PACKET_RECEIVED = 1;
    public static final int TYPE_ERROR = 2;
    public static final int TYPE_ADAPTER_CHANGED = 3;
    public static final int TYPE_CONNECTION_CHANGED = 4;

    public AdapterEvent()
    {
    }
    
    public AdapterEvent(int eventType, Endpoint endpoint, byte[] dataBuf, int dataBufLen)
    {
        this.eventType = eventType;
        this.endpoint  = endpoint;
        this.dataBuf = dataBuf;
        this.dataBufLen = dataBufLen;
    }
    
    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public boolean isIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isIsChanged() {
        return isChanged;
    }

    public void setIsChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public byte[] getDataBuf() {
        return dataBuf;
    }

    public void setDataBuf(byte[] dataBuf) {
        this.dataBuf = dataBuf;
    }

    public int getDataBufLen() {
        return dataBufLen;
    }

    public void setDataBufLen(int dataBufLen) {
        this.dataBufLen = dataBufLen;
    }

    public ErrCode getErrCode() {
        return errCode;
    }

    public void setErrCode(ErrCode errCode) {
        this.errCode = errCode;
    }

    public int getAdapteType() {
        return adapteType;
    }

    public void setAdapteType(int adapteType) {
        this.adapteType = adapteType;
    }

    int eventType = TYPE_NONE;
    boolean isConnected = false;
    boolean isChanged = false;
    Endpoint endpoint = null;
    byte[] dataBuf = null;
    int dataBufLen = 0;
    ErrCode errCode = ErrCode.OK;
    int adapteType = AdapterType.IP;

}
