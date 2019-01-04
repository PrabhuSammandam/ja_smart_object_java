/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.client;

import org.jinjuamla.network.IpAdapterConfigMgr;
import org.jinjuamla.network.datatypes.IpAdapterConfig;
import org.jinjuamla.network.enums.consts.AdapterType;
import org.jinjuamla.network.platform.INetworkPlatformFactory;
import org.jinjuamla.network.platform.NetworkPlatformFactory;
import org.jinjuamla.network.platform.NetworkPlatformFactoryMgr;
import org.jinjuamla.stack.MsgStack;
import org.jinjuamla.stack.coap.CoapMsg;
import org.jinjuamla.stack.consts.CoapMsgConsts;
import org.jinjuamla.stack.events.ClientSendRequestStackEvent;

/**
 *
 * @author psammand
 */
public class Client {

    boolean _initialized = false;
    MsgStack _msgStack;

    public void initialize() {
        if (_initialized) {
            return;
        }
        INetworkPlatformFactory networkPlatformFactory = new NetworkPlatformFactory();
        NetworkPlatformFactoryMgr.Inst().setNetworkPlatformFactory(networkPlatformFactory);

        IpAdapterConfig ipAdapterConfig = IpAdapterConfigMgr.Inst().getIpAdapterConfig();

        ipAdapterConfig.setIsIpv4UcastEnabled(true);
        _msgStack = new MsgStack();
        _msgStack.initialize(AdapterType.IP);

    }

    public void get(GetRequestInfo getRequestInfo) {
        CoapMsg clientRequest = new CoapMsg();
        clientRequest.set_type(getRequestInfo.getMsgType());
        clientRequest.set_code(CoapMsgConsts.MSG_CODE_GET);
        clientRequest.setEndpoint(getRequestInfo.getEndpoint());
        clientRequest.get_option_set().setUriPath(getRequestInfo.getUriPath());
        clientRequest.get_option_set().set_uri_query_string(getRequestInfo.getUriQuery());
        clientRequest.get_option_set().set_accept_format(getRequestInfo.getAcceptFormat());

        ClientSendRequestStackEvent requestStackEvent = new ClientSendRequestStackEvent(clientRequest, getRequestInfo.getClientCallback());

        try {
            _msgStack.sendStackEvent(requestStackEvent);
        }
        catch (InterruptedException ex) {
        }

    }

    public void post(PostRequestInfo postRequestInfo) {
        CoapMsg clientRequest = new CoapMsg();
        clientRequest.set_type(postRequestInfo.getMsgType());
        clientRequest.set_code(CoapMsgConsts.MSG_CODE_POST);
        clientRequest.setEndpoint(postRequestInfo.getEndpoint());
        clientRequest.get_option_set().setUriPath(postRequestInfo.getUriPath());
        clientRequest.get_option_set().set_uri_query_string(postRequestInfo.getUriQuery());
        clientRequest.get_option_set().set_accept_format(postRequestInfo.getAcceptFormat());
        clientRequest.get_option_set().set_content_format(postRequestInfo.getContentFormat());
        clientRequest.set_payload(postRequestInfo.getPayload());

        ClientSendRequestStackEvent requestStackEvent = new ClientSendRequestStackEvent(clientRequest, postRequestInfo.getClientCallback());

        try {
            _msgStack.sendStackEvent(requestStackEvent);
        }
        catch (InterruptedException ex) {
        }

    }

}
