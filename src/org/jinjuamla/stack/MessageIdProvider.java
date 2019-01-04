/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack;

import org.jinjuamla.stack.coap.CoapMsg;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author psammand
 */
public class MessageIdProvider
{

  public static int getNextId()
  {
    if (_messageIdCounter == 0)
    {
      byte[] bytes = new byte[2];
      try
      {
        SecureRandom.getInstanceStrong().nextBytes(bytes);
      } catch (NoSuchAlgorithmException ex)
      {
        Logger.getLogger(MessageIdProvider.class.getName()).log(Level.SEVERE, null, ex);
      }

      _messageIdCounter |= (bytes[0] << 8);
      _messageIdCounter |= bytes[1];
    }

    return _messageIdCounter++;
  }

  public static void assignMsgId(CoapMsg coapMsg)
  {
    if (coapMsg.get_id() == 0)
    {
      coapMsg.set_id((getNextId() & 0xFFFF));
    }
  }

  static int _messageIdCounter = 0;
}
