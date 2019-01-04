/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack;

import org.jinjuamla.network.adapters.AdapterMgr;
import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.stack.coap.CoapMsg;
import org.jinjuamla.stack.coap.CoapMsgCodec;
import org.jinjuamla.stack.coap.CoapMsgPrinter;

/**
 *
 * @author psammand
 */
public class MessageSender
{

  public static void sendEmptyMessage(Endpoint endpoint, int msgType, int msgId)
  {
    byte[] encodedEmptyMsg = CoapMsgCodec.encodeEmptyMsg(msgType, msgId);
    AdapterMgr.Inst().SendUnicastData(endpoint, encodedEmptyMsg, encodedEmptyMsg.length);
  }

  public static void sendMessage(CoapMsg coapMsg)
  {
    CoapMsgPrinter.print(coapMsg, false);

    byte[] encodedCoapMsg = CoapMsgCodec.encode_coap_msg(coapMsg);

    if (encodedCoapMsg == null)
    {
      return;
    }

    if (coapMsg.getEndpoint().isMulticast() && coapMsg.is_request())
    {
      AdapterMgr.Inst().SendMulticastData(coapMsg.getEndpoint(), encodedCoapMsg, encodedCoapMsg.length);
    } else
    {
      AdapterMgr.Inst().SendUnicastData(coapMsg.getEndpoint(), encodedCoapMsg, encodedCoapMsg.length);
    }
  }
}
