/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.adapters;

import java.util.LinkedList;

import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.network.enums.ErrCode;
import org.jinjuamla.network.enums.consts.AdapterType;
import org.jinjuamla.network.platform.INetworkPlatformFactory;
import org.jinjuamla.network.platform.NetworkPlatformFactoryMgr;	

/**
 *
 * @author psammand
 */
public class AdapterMgr implements IAdapterEventHandler {

    static AdapterMgr instance = null;

    IAdapterMgrDataHandler adapterMgrDataHandler = null;
    LinkedList<IAdapter> adaptersList = new LinkedList<>();
    int selectedAdapters = 0;

    public static AdapterMgr Inst() {
        if (instance == null) {
            instance = new AdapterMgr();
        }

        return instance;
    }

    private AdapterMgr() {

    }

    public ErrCode InitializeAdapters(int adapterTypes) {

        if (NetworkPlatformFactoryMgr.Inst().getNetworkPlatformFactory() == null) {
            return ErrCode.ERR;
        }

        return initAdapter(adapterTypes, AdapterType.IP);
    }

    public ErrCode ReadData() {
        if ((selectedAdapters & 0xFF) == AdapterType.DEFAULT) {
            return ErrCode.OK;
        }

        for (int i = 0; i < AdapterType.MAX; ++i) {
            IAdapter adapter = getAdapterForType(1 << i);

            if (adapter != null) {
                adapter.readData();
            }
        }

        return ErrCode.OK;
    }

    public ErrCode SendMulticastData(Endpoint endPoint, byte[] data, int data_length) {
        if ((selectedAdapters & 0xFF) == AdapterType.DEFAULT) {
            return ErrCode.SEND_DATA_FAILED;
        }

        for (int i = 0; i < AdapterType.MAX; ++i) {
            IAdapter adapter = getAdapterForType(1 << i);

            if (adapter != null) {
                int sendBytes = adapter.sendMulticastData(endPoint, data, data_length);

                if (0 < sendBytes || sendBytes != data_length) {
                    return ErrCode.SEND_DATA_FAILED;
                }
            }
        }

        return ErrCode.OK;
    }

    public ErrCode SendUnicastData(Endpoint end_point, byte[] data, int data_length) {
        if ((selectedAdapters & 0xFF) == AdapterType.DEFAULT) {
            return ErrCode.SEND_DATA_FAILED;
        }

        for (int i = 0; i < AdapterType.MAX; ++i) {
            IAdapter adapter = getAdapterForType(1 << i);

            if (adapter != null) {
                int sendBytes = adapter.sendUnicastData(end_point, data, data_length);

                if (0 < sendBytes || sendBytes != data_length) {
                    return ErrCode.SEND_DATA_FAILED;
                }
            }
        }

        return ErrCode.OK;
    }

    public ErrCode StartAdapter(int adapterType) {
        ErrCode errCode = ErrCode.OK;

        if ((selectedAdapters & adapterType) != 0) {
            return errCode;
        }

        selectedAdapters |= adapterType;

        IAdapter adapter = getAdapterForType(adapterType);

        if (adapter != null) {
            errCode = adapter.startAdapter();
        }

        return errCode;
    }

    public ErrCode StartServers() {
        boolean errFound = false;
        ErrCode errCode = ErrCode.OK;

        if ((selectedAdapters & 0xFF) == AdapterType.DEFAULT) {
            return errCode;
        }

        for (int i = 0; i < AdapterType.MAX; ++i) {
            IAdapter adapter = getAdapterForType(1 << i);

            if (adapter != null) {
                errCode = adapter.startServer();

                if (errCode != ErrCode.OK) {
                    errFound = true;
                }
            }
        }

        return errFound == true ? ErrCode.ERR : ErrCode.OK;
    }

    public ErrCode StopAdapter(int adapterType) {
        ErrCode errCode = ErrCode.OK;

        if (adapterType == AdapterType.DEFAULT || adapterType == AdapterType.ALL) {
            return errCode;
        }

        selectedAdapters &= (~adapterType);

        IAdapter adapter = getAdapterForType(adapterType);

        if (adapter != null) {
            errCode = adapter.stopAdapter();
        }

        return errCode;
    }

    public ErrCode StopServers() {
        boolean errFound = false;
        ErrCode errCode = ErrCode.OK;

        if ((selectedAdapters & 0xFF) == AdapterType.DEFAULT) {
            return errCode;
        }

        for (int i = 0; i < AdapterType.MAX; ++i) {
            IAdapter adapter = getAdapterForType(1 << i);

            if (adapter != null) {
                errCode = adapter.stopServer();

                if (errCode != ErrCode.OK) {
                    errFound = true;
                }
            }
        }

        return errFound == true ? ErrCode.ERR : ErrCode.OK;
    }

    public ErrCode TerminateAdapters() {
        adaptersList.forEach(IAdapter::terminate);

        adaptersList.clear();
        adapterMgrDataHandler = null;

        return ErrCode.OK;
    }

    public IAdapterMgrDataHandler getAdapterMgrDataHandler() {
        return adapterMgrDataHandler;
    }

    public void setAdapterMgrDataHandler(IAdapterMgrDataHandler adapterMgrDataHandler) {
        this.adapterMgrDataHandler = adapterMgrDataHandler;
    }

    public int getSelectedAdapters() {
        return selectedAdapters;
    }

    public void setSelectedAdapters(int selectedAdapters) {
        this.selectedAdapters = selectedAdapters;
    }

    @Override
    public void handleAdapterEvent(AdapterEvent adapaterEvent) {
        if (adapaterEvent == null) {
            return;
        }

        switch (adapaterEvent.getEventType()) {
            case AdapterEvent.TYPE_PACKET_RECEIVED: {
                if (adapterMgrDataHandler != null) {
                    adapterMgrDataHandler.handlePacketReceived(adapaterEvent.getEndpoint(), adapaterEvent.getDataBuf(), adapaterEvent.getDataBufLen());
                }
            }
            break;
            case AdapterEvent.TYPE_ERROR: {
                if (adapterMgrDataHandler != null) {
                    adapterMgrDataHandler.handleError(adapaterEvent.getEndpoint(), adapaterEvent.getDataBuf(), adapaterEvent.getDataBufLen(), adapaterEvent.getErrCode());
                }
            }
            break;
            default:
                break;
        }
    }

    private IAdapter getAdapterForType(int adapterType) {
        IAdapter foundAdapter = null;

        for (IAdapter adapter : adaptersList) {
            if (adapter.getType() == adapterType) {
                foundAdapter = adapter;
                break;
            }
        }

        return foundAdapter;
    }

    private ErrCode initAdapter(int adapterTypes, int toInitAdaptetType) {
        ErrCode errCode = ErrCode.OK;

        INetworkPlatformFactory nwkPlatFactory = NetworkPlatformFactoryMgr.Inst().getNetworkPlatformFactory();

        if ((adapterTypes == AdapterType.DEFAULT)
                || ((adapterTypes & AdapterType.ALL) == AdapterType.ALL)
                || ((adapterTypes & toInitAdaptetType) == toInitAdaptetType)) {
            IAdapter adapter = nwkPlatFactory.getAdapter(toInitAdaptetType);

            if (adapter == null) {
                return ErrCode.INVALID_PARAMS;
            }

            adapter.setAdapterHandler(this);

            errCode = adapter.initialize();

            if (errCode == ErrCode.OK) {
                adaptersList.add(adapter);
            }
        }
        return errCode;
    }

}
