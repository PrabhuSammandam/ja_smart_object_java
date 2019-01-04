/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.client;

import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.stack.IClientCallback;

/**
 *
 * @author psammand
 */
public class BaseRequestInfo {
    Endpoint _endpoint;
    String _uriPath;
    String _uriQuery;
    IClientCallback _clientCallback;
    int _msgType;

    public BaseRequestInfo(Endpoint endpoint, String uriPath, String uriQuery, IClientCallback clientCallback, int msgType) {
        this._endpoint = endpoint;
        this._uriPath = uriPath;
        this._uriQuery = uriQuery;
        this._clientCallback = clientCallback;
        this._msgType = msgType;
    }

    public Endpoint getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this._endpoint = endpoint;
    }

    public String getUriPath() {
        return _uriPath;
    }

    public void setUriPath(String uriPath) {
        this._uriPath = uriPath;
    }

    public String getUriQuery() {
        return _uriQuery;
    }

    public void setUriQuery(String uriQuery) {
        this._uriQuery = uriQuery;
    }

    public IClientCallback getClientCallback() {
        return _clientCallback;
    }

    public void setClientCallback(IClientCallback clientCallback) {
        this._clientCallback = clientCallback;
    }

    public int getMsgType() {
        return _msgType;
    }

    public void setMsgType(int msgType) {
        this._msgType = msgType;
    }

}
