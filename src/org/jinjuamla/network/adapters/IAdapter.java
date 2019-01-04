/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.adapters;

import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.network.enums.ErrCode;

/**
 *
 * @author psammand
 */
public interface IAdapter {

    public ErrCode initialize();

    public ErrCode terminate();

    public ErrCode startAdapter();

    public ErrCode stopAdapter();

    public ErrCode startServer();

    public ErrCode stopServer();

    public ErrCode startListening();

    public ErrCode stopListening();

    public int sendUnicastData(Endpoint end_point, byte[] data, int data_length);

    public int sendMulticastData(Endpoint end_point, byte[] data, int data_length);

    public void readData();

    public int getType();

    public void setAdapterHandler(IAdapterEventHandler adapter_event_handler);

}
