/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack;

import org.jinjuamla.stack.coap.CoapMsg;
import java.util.ArrayList;

/**
 *
 * @author psammand
 */
public class InteractionStore
{

  public static InteractionStore Inst()
  {
    if (_instance == null)
    {
      _instance = new InteractionStore();
    }
    return _instance;
  }

  public Exchange findClientExchange(CoapMsg coapMsg)
  {
    if (coapMsg == null)
    {
      return null;
    }

    for (Exchange ex : _clientExchangeList)
    {
      if (ex.isMsgMatched(coapMsg))
      {
        return ex;
      }
    }
    return null;
  }

  public boolean addClientExchange(Exchange exchange)
  {
    if (exchange != null)
    {
      _clientExchangeList.add(exchange);
      return true;
    }
    return false;
  }

  public boolean deleteClientExchange(Exchange exchange)
  {
    if (exchange != null)
    {
      return _clientExchangeList.remove(exchange);
    }
    return false;
  }

  public ClientInteraction findClientInteraction(CoapMsg coapMsg)
  {
    for (Interaction interaction : _clientInteractionList)
    {
      ClientInteraction clientInteraction = (ClientInteraction) interaction;

      if (clientInteraction.isMsgMatched(coapMsg))
      {
        return clientInteraction;
      }
    }
    return null;
  }

  public ClientInteraction createClientInteraction(CoapMsg coapMsg)
  {
    if (coapMsg != null)
    {
      ClientInteraction clientInteraction = new ClientInteraction(coapMsg);
      _clientInteractionList.add(clientInteraction);
      return clientInteraction;
    }
    return null;
  }

  ArrayList<Exchange> getClientExchangeList()
  {
    return _clientExchangeList;
  }

  ArrayList<Interaction> getClientInteractionList()
  {
    return _clientInteractionList;
  }

  public ArrayList<MulticastClientInteraction> getMulticastClientInteraction()
  {
    return _multicastClientInteraction;
  }
  
  ArrayList<Exchange> _clientExchangeList = new ArrayList<>();
  ArrayList<Interaction> _clientInteractionList = new ArrayList<>();
  ArrayList<MulticastClientInteraction> _multicastClientInteraction = new ArrayList<>();
  static InteractionStore _instance = null;
}
