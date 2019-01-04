/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.client;

import org.jinjuamla.client.BaseRequestInfo;	
import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.stack.IClientCallback;
import org.jinjuamla.stack.consts.CoapMsgConsts;

/**
 *
 * @author psammand
 */
public class GetRequestInfo extends BaseRequestInfo{
    public GetRequestInfo(Endpoint endpoint, String uriPath, String uriQuery, IClientCallback clientCallback, int msgType) {
        super(endpoint, uriPath, uriQuery, clientCallback, msgType);
    }

    public int getAcceptFormat() {
        return _acceptFormat;
    }

    public void setAcceptFormat(int acceptFormat) {
        this._acceptFormat = acceptFormat;
    }

    public int getBlockSize() {
        return _blockSize;
    }

    public void setBlockSize(int blockSize) {
        this._blockSize = blockSize;
    }
    
    int _acceptFormat = CoapMsgConsts.PAYLOAD_FORMAT_APP_CBOR;
    int _blockSize = 0;
}
