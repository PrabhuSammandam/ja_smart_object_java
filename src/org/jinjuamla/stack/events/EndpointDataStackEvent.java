/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack.events;

import org.jinjuamla.network.datatypes.Endpoint;

/**
 *
 * @author psammand
 */
public class EndpointDataStackEvent extends StackEvent
{

  public EndpointDataStackEvent(Endpoint endpoint, byte[] payload)
  {
    super(TYPE_ENDPOINT_DATA);
    _endpoint = endpoint;
    _payload = payload;
  }

  public Endpoint getEndpoint()
  {
    return _endpoint;
  }

  public byte[] getPayload()
  {
    return _payload;
  }
  
  Endpoint _endpoint;
  byte[] _payload;
}
