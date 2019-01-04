/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack;

import org.jinjuamla.stack.coap.CoapMsg;

/**
 *
 * @author psammand
 */
public interface IClientCallback
{
  void handleResponse(CoapMsg response, int status);
}
