/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack;

import static java.lang.Thread.interrupted;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jinjuamla.network.adapters.AdapterMgr;
import org.jinjuamla.network.adapters.IAdapterMgrDataHandler;
import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.network.enums.ErrCode;
import org.jinjuamla.stack.coap.CoapMsg;
import org.jinjuamla.stack.coap.CoapMsgCodec;
import org.jinjuamla.stack.coap.CoapMsgCodecException;
import org.jinjuamla.stack.coap.CoapMsgPrinter;
import org.jinjuamla.stack.consts.CoapMsgConsts;
import org.jinjuamla.stack.events.ClientSendRequestStackEvent;
import org.jinjuamla.stack.events.EndpointDataStackEvent;
import org.jinjuamla.stack.events.IStackEventHandler;
import org.jinjuamla.stack.events.ServerSendResponseStackEvent;
import org.jinjuamla.stack.events.StackEvent;

/**
 *
 * @author psammand
 */
public class MsgStack implements IStackEventHandler
{

  StackThread stackThread = null;
  BlockingQueue<StackEvent> _blockingQueue;

  public MsgStack()
  {
    this._blockingQueue = new LinkedBlockingQueue<>();
  }

  public ErrCode initialize(int adapterType)
  {
    AdapterMgr adapterMgr = AdapterMgr.Inst();

    adapterMgr.setAdapterMgrDataHandler(new IAdapterMgrDataHandler()
    {
      @Override
      public void handlePacketReceived(Endpoint endpoint, byte[] dataBuf, int dataBufLen)
      {
        try
        {
          handlePacketReceivedCallback(endpoint, dataBuf);
        } catch (InterruptedException ex)
        {
          Logger.getLogger(MsgStack.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

      @Override
      public void handleError(Endpoint endpoint, byte[] dataBuf, int dataBufLen, ErrCode errCode)
      {
        handleErrorCallback(endpoint, dataBuf, errCode);
      }
    });

    ErrCode retStatus;
    retStatus = adapterMgr.InitializeAdapters(adapterType);

    if (retStatus != ErrCode.OK)
    {
      return retStatus;
    }

    retStatus = adapterMgr.StartAdapter(adapterType);

    if (retStatus != ErrCode.OK)
    {
      return retStatus;
    }

    retStatus = adapterMgr.StartServers();

    if (retStatus != ErrCode.OK)
    {
      return retStatus;
    }

    stackThread = new StackThread(_blockingQueue, this);
    stackThread.start();

    return ErrCode.OK;
  }

  public void sendStackEvent(StackEvent stackEvent) throws InterruptedException
  {
    if (stackEvent == null)
    {
      return;
    }

    _blockingQueue.put(stackEvent);
  }

  @Override
  public void receiveStackEvent(StackEvent stackEvent)
  {
    if (stackEvent == null)
    {
      return;
    }

    switch (stackEvent.getEventType())
    {
      case StackEvent.TYPE_ENDPOINT_DATA:
      {
        EndpointDataStackEvent event = (EndpointDataStackEvent) stackEvent;
        handleEndpointDataEvent(event.getEndpoint(), event.getPayload());
      }
      break;
      case StackEvent.TYPE_CLIENT_SEND_REQUEST:
      {
        ClientSendRequestStackEvent event = (ClientSendRequestStackEvent) stackEvent;
        handleClientSendRequestEvent(event.getRequest(), event.getCallback());
      }
      break;
      case StackEvent.TYPE_SERVER_SEND_RESPONSE:
      {
        ServerSendResponseStackEvent event = (ServerSendResponseStackEvent) stackEvent;
        handleServerSendResponseEvent(event.getResponse());
      }
      break;
      default:
        break;
    }
  }

  private void handleClientSendRequestEvent(CoapMsg clientRequest, IClientCallback clientCallback)
  {
    if (clientRequest.getEndpoint().isMulticast())
    {
      MulticastClientInteraction multicastClientInteraction = new MulticastClientInteraction(clientRequest);
      multicastClientInteraction.setClientCallback(clientCallback);
      multicastClientInteraction.sendRequest();
    } else
    {
      ClientInteraction clientInteraction = InteractionStore.Inst().createClientInteraction(clientRequest);
      clientInteraction.setClientCallback(clientCallback);
      clientInteraction.sendRequest();
    }
  }

  private void handleServerSendResponseEvent(CoapMsg serverResponse)
  {

  }

  private void handlePacketReceivedCallback(Endpoint endpoint, byte[] dataBuf) throws InterruptedException
  {
    EndpointDataStackEvent endpointDataStackEvent = new EndpointDataStackEvent(endpoint, dataBuf);
    sendStackEvent(endpointDataStackEvent);
  }

  private void handleErrorCallback(Endpoint endpoint, byte[] dataBuf, ErrCode errCode)
  {
  }

  private void handleEndpointDataEvent(Endpoint endpoint, byte[] payload)
  {
    if (payload == null || payload.length == 0)
    {
      return;
    }

    System.out.println("Received packet of length " + payload.length);

    boolean isMulticast = endpoint.isMulticast();
    CoapMsg newCoapMsg = null;

    try
    {
      newCoapMsg = CoapMsgCodec.decode_coap_msg(payload);
    } catch (CoapMsgCodecException ex)
    {
      if (ex.getErrCode() != ErrCode.MSG_FORMAT_ERROR)
      {
        System.out.println("Erroneous message received");
      } else
      {

      }

      return;
    }

    /* if token is zero length do not process */
    if (!newCoapMsg.is_empty_msg() && newCoapMsg.get_token().get_length() == 0)
    {
      if (!isMulticast)
      {
        MessageSender.sendEmptyMessage(endpoint, CoapMsgConsts.COAP_MSG_TYPE_RST, newCoapMsg.get_id());
      }
      System.out.println("Received message without token");
      return;
    }

    /* COAP PING */
    if (!isMulticast && newCoapMsg.is_con() && newCoapMsg.is_empty_msg())
    {
      MessageSender.sendEmptyMessage(endpoint, CoapMsgConsts.COAP_MSG_TYPE_RST, newCoapMsg.get_id());
      return;
    }

    if (newCoapMsg.is_request() && isMulticast)
    {
      if (newCoapMsg.is_con())
      {
        return;
      }

      if (newCoapMsg.get_code() != CoapMsgConsts.MSG_CODE_GET)
      {
        return;
      }
    }

    newCoapMsg.setEndpoint(endpoint);
    CoapMsgPrinter.print(newCoapMsg, true);

    if (newCoapMsg.is_request())
    {

    } else if (newCoapMsg.is_empty_msg())
    {
      receiveEmptyMessage(newCoapMsg);
    } else
    {
      clientReceiveResponse(newCoapMsg);
    }
  }

  private void clientReceiveResponse(CoapMsg newCoapMsg)
  {
    InteractionStore iStore = InteractionStore.Inst();
    boolean isMulticastResponse = false;
    ClientInteraction clientInteraction = null;

    ArrayList<MulticastClientInteraction> multicastClientInteractions = iStore.getMulticastClientInteraction();

    for (MulticastClientInteraction multicastInteraction : multicastClientInteractions)
    {
      if (multicastInteraction.getClientRequest().get_token().equals(newCoapMsg.get_token()))
      {
        isMulticastResponse = true;

        for (Interaction interaction : iStore.getClientInteractionList())
        {
          if (((ClientInteraction) interaction).getEndpoint().equals(newCoapMsg.getEndpoint()))
          {
            clientInteraction = (ClientInteraction) interaction;
            break;
          }
        }

        if (clientInteraction == null)
        {
          clientInteraction = new ClientInteraction(newCoapMsg);
          clientInteraction.setClientCallback(multicastInteraction.getClientCallback());
          clientInteraction.setToken(multicastInteraction.getClientRequest().get_token());
          clientInteraction.setEndpoint(newCoapMsg.getEndpoint());

          iStore.getClientInteractionList().add(clientInteraction);
          multicastInteraction.addClientInteraction();
        }
        break;
      }
    }

    if (clientInteraction == null)
    {
      clientInteraction = iStore.findClientInteraction(newCoapMsg);
    }

    if (clientInteraction == null)
    {
      System.out.println("Received message for unknown interaction");
    } else
    {
      if (newCoapMsg.is_con())
      {
        Exchange clientExchange = iStore.findClientExchange(newCoapMsg);

        if (clientExchange != null)
        {
          clientExchange.deliver();
          return;
        }
      }

      try
      {
        clientInteraction.receiveResponse(newCoapMsg);
      } catch (CloneNotSupportedException ex)
      {
        Logger.getLogger(MsgStack.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private void receiveEmptyMessage(CoapMsg newCoapMsg)
  {
    if (newCoapMsg == null)
    {
      return;
    }

    InteractionStore iStore = InteractionStore.Inst();

    if (newCoapMsg.is_ack())
    {
      Exchange clientExchange = iStore.findClientExchange(newCoapMsg);

      if (clientExchange != null)
      {
        if (clientExchange.getInteraction() != null)
        {
          clientExchange.getInteraction().deleteExchange(clientExchange);
        }
      }
    } else if (newCoapMsg.is_rst())
    {
      Exchange clientExchange = iStore.findClientExchange(newCoapMsg);

      if (clientExchange != null && clientExchange.getInteraction() != null)
      {
        ((ClientInteraction) clientExchange.getInteraction()).notifyResponse(null, 1);
        clientExchange.getInteraction().deleteExchange(clientExchange);
      }
    }
  }

  class StackThread extends Thread
  {

    BlockingQueue<StackEvent> _blockingQueue = null;
    IStackEventHandler _stackEventHandler = null;

    public StackThread(BlockingQueue<StackEvent> blockingQueue, IStackEventHandler stackEventHandler)
    {
      _blockingQueue = blockingQueue;
      _stackEventHandler = stackEventHandler;
    }

    @Override
    public void run()
    {
      while (true)
      {
        if (interrupted())
        {
          break;
        }

        try
        {
          StackEvent newStackEvent = _blockingQueue.take();

          if (newStackEvent != null && _stackEventHandler != null)
          {
            _stackEventHandler.receiveStackEvent(newStackEvent);
          }
        } catch (InterruptedException ex)
        {
          Logger.getLogger(MsgStack.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

  }
}
