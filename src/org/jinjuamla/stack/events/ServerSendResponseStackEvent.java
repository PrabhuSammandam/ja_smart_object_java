/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack.events;

import org.jinjuamla.stack.coap.CoapMsg;

/**
 *
 * @author psammand
 */
public class ServerSendResponseStackEvent extends StackEvent
{

  public ServerSendResponseStackEvent(CoapMsg serverResponse)
  {
    super(StackEvent.TYPE_SERVER_SEND_RESPONSE);
    _response = serverResponse;
  }

  public CoapMsg getResponse()
  {
    return _response;
  }

  CoapMsg _response;
}
