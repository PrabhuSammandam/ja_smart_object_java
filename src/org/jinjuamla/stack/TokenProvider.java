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
public class TokenProvider
{

  public static void assignNextToken(CoapMsg coapMsg)
  {
    if (coapMsg.get_token().get_token() == null || coapMsg.get_token().get_length() == 0)
    {
      byte[] bytes = new byte[8];

      try
      {
        SecureRandom.getInstanceStrong().nextBytes(bytes);
      } catch (NoSuchAlgorithmException ex)
      {
        Logger.getLogger(TokenProvider.class.getName()).log(Level.SEVERE, null, ex);
      }

      coapMsg.get_token().set_token(bytes);
    }
  }
}
