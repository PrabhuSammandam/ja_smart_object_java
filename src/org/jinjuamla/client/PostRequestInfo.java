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
public class PostRequestInfo extends BaseRequestInfo {
    byte[] _payload;
    int _contentFormat = CoapMsgConsts.PAYLOAD_FORMAT_APP_CBOR;
    int _acceptFormat = CoapMsgConsts.PAYLOAD_FORMAT_APP_CBOR;

    public PostRequestInfo(Endpoint endpoint, String uriPath, String uriQuery, IClientCallback clientCallback, int msgType) {
        super(endpoint, uriPath, uriQuery, clientCallback, msgType);
    }

    public byte[] getPayload() {
        return _payload;
    }

    public void setPayload(byte[] payload) {
        this._payload = payload;
    }

    public int getContentFormat() {
        return _contentFormat;
    }

    public void setContentFormat(int contentFormat) {
        this._contentFormat = contentFormat;
    }

    public int getAcceptFormat() {
        return _acceptFormat;
    }

    public void setAcceptFormat(int acceptFormat) {
        this._acceptFormat = acceptFormat;
    }

}
