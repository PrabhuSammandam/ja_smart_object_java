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
public interface IAdapterMgrDataHandler {

    public void handlePacketReceived(Endpoint endpoint, byte[] dataBuf, int dataBufLen);

    public void handleError(Endpoint endpoint, byte[] dataBuf, int dataBufLen, ErrCode errCode);
}
