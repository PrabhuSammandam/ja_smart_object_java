/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.adapters.ip.networkinterfaces;

/**
 *
 * @author psammand
 */
public class InterfaceEvent {
    public static final int TYPE_ADDED = 0;
    public static final int TYPE_MODIFIED = 2;
    public static final int TYPE_REMOVE = 1;
    public static final int TYPE_STATE_CHANGED = 3;

    int adapterType;
    int eventType;
    int interfaceStatus;

    public InterfaceEvent(int adapterType, int eventType, int interfaceStatus) {
        this.adapterType = adapterType;
        this.eventType = eventType;
        this.interfaceStatus = interfaceStatus;
    }

    public int getAdapterType() {
        return adapterType;
    }

    public void setAdapterType(int adapterType) {
        this.adapterType = adapterType;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getInterfaceStatus() {
        return interfaceStatus;
    }

    public void setInterfaceStatus(int interfaceStatus) {
        this.interfaceStatus = interfaceStatus;
    }

}
