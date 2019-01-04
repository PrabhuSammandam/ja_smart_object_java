/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.coapresrep;

/**
 *
 * @author psammand
 */
public class IntegerResPropVal extends ResPropValue<Long>
{
  long _value;
  
  public IntegerResPropVal()
  {
    super(ResPropValue.TYPE_INTEGER);
  }

  public IntegerResPropVal(long value)
  {
    super(ResPropValue.TYPE_INTEGER);
    _value = value;
  }

  public long getValue()
  {
    return _value;
  }

  public void setValue(long _value)
  {
    this._value = _value;
  }
  
  
}
