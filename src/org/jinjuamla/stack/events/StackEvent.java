/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack.events;

/**
 *
 * @author psammand
 */
public class StackEvent
{

  public static final int TYPE_ENDPOINT_DATA = 0;
  public static final int TYPE_SERVER_SEND_RESPONSE = 1;
  public static final int TYPE_CLIENT_SEND_REQUEST = 2;
  public static final int TYPE_HEART_BEAT_TIMER = 3;

  public StackEvent(int _eventType)
  {
    this._eventType = _eventType;
  }

  public int getEventType()
  {
    return _eventType;
  }

  public void setEventType(int _eventType)
  {
    this._eventType = _eventType;
  }

  int _eventType;
}
