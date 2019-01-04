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
public class HeartBeatTimerStackevent extends StackEvent
{

  public HeartBeatTimerStackevent()
  {
    super(StackEvent.TYPE_HEART_BEAT_TIMER);
  }

}
