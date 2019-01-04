/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack;

import org.jinjuamla.stack.coap.CoapMsg;
import org.jinjuamla.stack.consts.CoapMsgConsts;

/**
 *
 * @author psammand
 */
public class MulticastClientInteraction
{

    IClientCallback _clientCallback = null;
    CoapMsg _clientRequest;
    int _noOfClientInteractions = 0;

    public MulticastClientInteraction(CoapMsg _clientRequest) {
        this._clientRequest = _clientRequest;
    }

    public void addClientInteraction() {
        _noOfClientInteractions++;
    }

    public void deleteClientInteraction() {
        _noOfClientInteractions--;
    }

    public IClientCallback getClientCallback() {
        return _clientCallback;
    }

    public void setClientCallback(IClientCallback _clientCallback) {
        this._clientCallback = _clientCallback;
    }

    public CoapMsg getClientRequest() {
        return _clientRequest;
    }

    public boolean hasClientInteractions() {
        return _noOfClientInteractions != 0;
    }

    public void sendRequest() {
        if (_clientRequest == null || _clientCallback == null) {
            return;
        }

        _clientRequest.set_type(CoapMsgConsts.COAP_MSG_TYPE_NON);
        TokenProvider.assignNextToken(_clientRequest);
        MessageIdProvider.assignMsgId(_clientRequest);
        MessageSender.sendMessage(_clientRequest);
    }

}
