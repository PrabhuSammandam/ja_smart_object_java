/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack.events;

import org.jinjuamla.stack.IClientCallback;
import org.jinjuamla.stack.coap.CoapMsg;

/**
 *
 * @author psammand
 */
public class ClientSendRequestStackEvent extends StackEvent
{

  public ClientSendRequestStackEvent(CoapMsg request, IClientCallback clientCallback)
  {
    super(StackEvent.TYPE_CLIENT_SEND_REQUEST);
    _request = request;
    _callback = clientCallback;
  }

  public CoapMsg getRequest()
  {
    return _request;
  }

  public IClientCallback getCallback()
  {
    return _callback;
  }

  CoapMsg _request;
  IClientCallback _callback;
}
